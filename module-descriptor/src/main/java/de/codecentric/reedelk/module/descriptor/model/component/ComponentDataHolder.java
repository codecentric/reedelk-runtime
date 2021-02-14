package de.codecentric.reedelk.module.descriptor.model.component;

import java.util.List;

public interface ComponentDataHolder {

    List<String> keys();

    <T> T get(String key);

    void set(String propertyName, Object propertyValue);

    boolean has(String key);

}
