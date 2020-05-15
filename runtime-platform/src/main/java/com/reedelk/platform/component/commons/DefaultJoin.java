package com.reedelk.platform.component.commons;

import com.reedelk.runtime.api.component.Join;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

public class DefaultJoin implements Join {

    @Override
    public Message apply(FlowContext flowContext, List<Message> messagesToJoin) {
        List<Object> results = new ArrayList<>();

        MessageAttributes mergedAttributes = new MessageAttributes();

        for (Message message : messagesToJoin) {
            // Add all the payload results in the list
            Object payload = message.payload();
            results.add(payload);

            // Merge all attributes from all the messages
            MessageAttributes messageAttributes = message.attributes();
            mergedAttributes.putAll(messageAttributes);
        }

        return MessageBuilder.get(DefaultJoin.class)
                .withList(results, Object.class)
                .attributes(mergedAttributes)
                .build();
    }
}
