package com.reedelk.runtime.api.message;

import java.io.Serializable;
import java.util.Map;

public interface MessageAttributes extends Map<String, Serializable> {

    <T> T get(String key);

    boolean contains(String key);
}
