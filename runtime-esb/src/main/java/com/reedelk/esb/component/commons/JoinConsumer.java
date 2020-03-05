package com.reedelk.esb.component.commons;

import com.reedelk.esb.execution.MessageAndContext;
import com.reedelk.runtime.api.component.Join;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.message.Message;
import reactor.core.publisher.MonoSink;

import java.util.List;
import java.util.function.Consumer;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class JoinConsumer implements Consumer<MonoSink<MessageAndContext>> {

    private final Join join;
    private final MessageAndContext context;
    private final MessageAndContext[] messages;

    public JoinConsumer(MessageAndContext originalMessage, MessageAndContext[] messagesToJoin, Join join) {
        this.join = join;
        this.context = originalMessage;
        this.messages = messagesToJoin;
    }

    @Override
    public void accept(MonoSink<MessageAndContext> sink) {
        try {
            List<Message> collect = stream(messages)
                    .map(MessageAndContext::getMessage)
                    .collect(toList());

            Message outMessage = join.apply(context.getFlowContext(), collect);

            context.replaceWith(outMessage);

            sink.success(context);

        } catch (Exception exception) {
            // Propagate the error occurred while applying the join
            sink.error(exception);

        } catch (Throwable throwable) {
            // If Throwable is 'NoClassDefFoundError' it means that the processor uses a component
            // which uses a class not imported in the module bundle it belongs to.
            // The module bundle should correctly add the missing import in the pom.xml configuration:
            // maven-bundle-plugin > configuration > instructions > Import-Package xml node.
            // IMPORTANT: This exception must be wrapped because Throwable exceptions are NOT
            // caught in the com.reedelk.esb.execution.FlowExecutorEngine -> doOnError callback.
            // They are normally caught at subscribe time however it does not apply because
            // the FlowExecutorEngine the stream on publishOn(SchedulerProvider.flow())
            // - i.e on a different thread. Therefore we must wrap 'NoClassDefFoundError' exception.
            ESBException wrapped = new ESBException(throwable);
            sink.error(wrapped);
        }
    }
}
