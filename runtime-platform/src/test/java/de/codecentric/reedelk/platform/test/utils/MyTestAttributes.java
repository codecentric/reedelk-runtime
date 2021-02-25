package de.codecentric.reedelk.platform.test.utils;

import de.codecentric.reedelk.runtime.api.message.MessageAttributes;

import java.io.Serializable;
import java.util.Map;

public class MyTestAttributes extends MessageAttributes {

    public MyTestAttributes(Map<String, ? extends Serializable> attributes) {
        attributes.forEach(this::put);
    }
}
