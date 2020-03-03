package com.reedelk.runtime.converter.types.scriptobjectmirrortype;

import com.reedelk.runtime.converter.types.ValueConverter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: Test me very well
class AsObject implements ValueConverter<ScriptObjectMirror,Object> {

    @Override
    public Object from(ScriptObjectMirror scriptObj) {
        return convertIntoJavaObject(scriptObj);
    }

    private Object convertIntoJavaObject(ScriptObjectMirror scriptObj) {
        if (scriptObj != null) {
            if (scriptObj.isArray()) {
                List<Object> list = new ArrayList<>();
                for (Map.Entry<String, Object> entry : scriptObj.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof ScriptObjectMirror) {
                        list.add(convertIntoJavaObject((ScriptObjectMirror) value));
                    } else {
                        list.add(value);
                    }
                }
                return list;
            } else {
                Map<String, Object> map = new HashMap<>();
                for (Map.Entry<String, Object> entry : scriptObj.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof ScriptObjectMirror) {
                        map.put(entry.getKey(), convertIntoJavaObject((ScriptObjectMirror) entry.getValue()));
                    } else {
                        map.put(entry.getKey(), value);
                    }
                }
                return map;
            }
        } else {
            return scriptObj;
        }
    }
}
