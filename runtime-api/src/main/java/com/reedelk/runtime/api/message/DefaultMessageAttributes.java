package com.reedelk.runtime.api.message;

import com.reedelk.runtime.api.component.Component;

import java.io.Serializable;
import java.util.*;

public final class DefaultMessageAttributes implements MessageAttributes, Serializable {

    public static MessageAttributes empty() {
        return new DefaultMessageAttributes();
    }

    private final TreeMap<String,Serializable> data = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private DefaultMessageAttributes() {
    }

    public DefaultMessageAttributes(Class<? extends Component> componentSettingAttributes, Map<String,Serializable> attributes) {
        data.putAll(attributes);
        data.put(MessageAttributeKey.COMPONENT_NAME, componentSettingAttributes.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        return (T) data.get(key);
    }

    @Override
    public boolean contains(String key) {
        return data.containsKey(key);
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    @Override
    public Serializable get(Object key) {
        return data.get(key);
    }

    @Override
    public Set<String> keySet() {
        return data.keySet();
    }

    @Override
    public Collection<Serializable> values() {
        return data.values();
    }

    @Override
    public Set<Entry<String, Serializable>> entrySet() {
        return data.entrySet();
    }

    @Override
    public String toString() {
        Iterator<Entry<String,Serializable>> i = entrySet().iterator();
        if (! i.hasNext()) {
            return this.getClass().getSimpleName() + "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(MessageAttributes.class.getSimpleName()).append('{');
        for (;;) {
            Entry<String,Serializable> e = i.next();
            String key = e.getKey();
            Serializable value = e.getValue();
            sb.append(key);
            sb.append('=');
            sb.append(value);
            if (! i.hasNext()) {
                return sb.append('}').toString();
            }
            sb.append(',').append(' ');
        }
    }

    @Override
    public Serializable put(String key, Serializable value) {
        throw new UnsupportedOperationException("Attributes are read only");
    }

    @Override
    public Serializable remove(Object key) {
        throw new UnsupportedOperationException("Attributes are read only");
    }

    @Override
    public void putAll(Map<? extends String, ? extends Serializable> m) {
        throw new UnsupportedOperationException("Attributes are read only");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Attributes are read only");
    }

}