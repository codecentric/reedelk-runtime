package com.reedelk.runtime.converter.types.scriptobjectmirrortype;

import com.reedelk.runtime.converter.types.ValueConverter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AsObject implements ValueConverter<ScriptObjectMirror,Object> {

    @Override
    public Object from(ScriptObjectMirror scriptObj) {
        return toJavaObject(scriptObj);
    }

    private Object toJavaObject(ScriptObjectMirror scriptObj) {
        if (scriptObj != null) {
            if (scriptObj.isArray()) {
                // It is a Javascript array which maps to a Java List type.
                return toList(scriptObj);
            } else {
                // It is a Javascript object which maps to a Java Map type.
                return toMap(scriptObj);
            }
        } else {
            // It is a Javascript primitive type.
            return scriptObj;
        }
    }

    private Object toMap(ScriptObjectMirror scriptObj) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : scriptObj.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof ScriptObjectMirror) {
                map.put(entry.getKey(), toJavaObject((ScriptObjectMirror) entry.getValue()));
            } else {
                map.put(entry.getKey(), value);
            }
        }
        return map;
    }

    private Object toList(ScriptObjectMirror scriptObj) {
        List<Object> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : scriptObj.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof ScriptObjectMirror) {
                list.add(toJavaObject((ScriptObjectMirror) value));
            } else {
                list.add(value);
            }
        }
        return list;
    }
}
