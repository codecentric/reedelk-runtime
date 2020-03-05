package com.reedelk.esb.component.commons;

import com.reedelk.runtime.api.component.Join;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.DefaultMessageAttributes;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class DefaultJoin implements Join {

    @Override
    public Message apply(FlowContext flowContext, List<Message> messagesToJoin) {
        Map<String, Serializable> attributes = new HashMap<>();
        MessageAttributes emptyJoinAttributes = new DefaultMessageAttributes(DefaultJoin.class, attributes);

        List<Object> results = messagesToJoin.stream()
                .map(Message::payload)
                .collect(toList());

        return MessageBuilder.get()
                .withJavaCollection(results, Object.class)
                .attributes(emptyJoinAttributes)
                .build();
    }
}
