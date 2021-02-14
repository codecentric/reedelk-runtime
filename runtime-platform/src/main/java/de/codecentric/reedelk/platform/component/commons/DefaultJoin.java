package de.codecentric.reedelk.platform.component.commons;

import de.codecentric.reedelk.runtime.api.commons.AttributesUtils;
import de.codecentric.reedelk.runtime.api.component.Join;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class DefaultJoin implements Join {

    @Override
    public Message apply(FlowContext flowContext, List<Message> messagesToJoin) {

        List<Object> results = messagesToJoin.stream()
                .map(Message::payload)
                .collect(toList());

        MessageAttributes mergedAttributes = AttributesUtils.merge(messagesToJoin);

        return MessageBuilder.get(DefaultJoin.class)
                .withList(results, Object.class)
                .attributes(mergedAttributes)
                .build();
    }
}
