package com.reedelk.runtime.converter.types.scriptobjectmirrortype;

import com.reedelk.runtime.converter.types.ValueConverter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

class AsByteArray implements ValueConverter<ScriptObjectMirror,byte[]> {

    private AsString asString = new AsString();

    @Override
    public byte[] from(ScriptObjectMirror value) {
        // We first must convert the Script Object to a java String
        // and then we can convert it to bytes.
        String javascriptObjectAsString = asString.from(value);
        return javascriptObjectAsString.getBytes();
    }
}
