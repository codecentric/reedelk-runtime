package com.reedelk.runtime.converter.types.scriptobjectmirrortype;

import com.reedelk.runtime.converter.types.ValueConverter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

class AsString implements ValueConverter<ScriptObjectMirror,String> {

    @Override
    public String from(ScriptObjectMirror value) {
        // TODO: No, this one should be converted
        //  to Java object (e.g list or map) and then to string.
        return value.toString();
    }
}
