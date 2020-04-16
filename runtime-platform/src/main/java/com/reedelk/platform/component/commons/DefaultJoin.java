package com.reedelk.platform.component.commons;

import com.reedelk.runtime.api.component.Join;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class DefaultJoin implements Join {

    @Override
    public Message apply(FlowContext flowContext, List<Message> messagesToJoin) {
        List<Object> results = messagesToJoin.stream()
                .map(Message::payload)
                .collect(toList());

        return MessageBuilder.get(DefaultJoin.class)
                .withList(results, Object.class)
                .build();
    }
}
