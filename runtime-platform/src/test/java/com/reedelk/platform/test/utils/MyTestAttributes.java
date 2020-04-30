package com.reedelk.platform.test.utils;

import com.reedelk.runtime.api.message.MessageAttributes;

import java.io.Serializable;
import java.util.Map;

public class MyTestAttributes extends MessageAttributes {

    public MyTestAttributes(Map<String, ? extends Serializable> attributes) {
        attributes.forEach(this::put);
    }
}
