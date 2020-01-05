package com.reedelk.runtime.converter.types.scriptobjectmirrortype;

import com.reedelk.runtime.converter.types.ValueConverter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

class AsString implements ValueConverter<ScriptObjectMirror,String> {

    @Override
    public String from(ScriptObjectMirror value) {
        return value.toString();
    }
}
