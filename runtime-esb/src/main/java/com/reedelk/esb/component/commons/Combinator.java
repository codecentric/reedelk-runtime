package com.reedelk.esb.component.commons;

import com.reedelk.esb.execution.MessageAndContext;

import java.util.function.Function;

public class Combinator {

    private Combinator() {
    }

    public static Function<Object[], MessageAndContext[]> messageAndContext() {
        return objects -> {
            MessageAndContext[] messageAndContexts = new MessageAndContext[objects.length];
            for (int i = 0; i < objects.length; i++) {
                messageAndContexts[i] = (MessageAndContext) objects[i];
            }
            return messageAndContexts;
        };
    }
}
