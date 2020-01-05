package com.reedelk.runtime.converter.types.scriptobjectmirrortype;

import com.reedelk.runtime.converter.types.ValueConverter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

class AsByteArray implements ValueConverter<ScriptObjectMirror,byte[]> {

    @Override
    public byte[] from(ScriptObjectMirror value) {
        return value.toString().getBytes();
    }
}
