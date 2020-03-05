package com.reedelk.runtime.converter.types.scriptobjectmirrortype;

import com.reedelk.runtime.converter.types.ValueConverter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

class AsString implements ValueConverter<ScriptObjectMirror,String> {

    private AsObject asObject = new AsObject();

    @Override
    public String from(ScriptObjectMirror value) {
        // We first must convert the Script Object to a java Primitive Type,
        // Map or Array and then we can convert it to string.
        Object javascriptObject = asObject.from(value);
        return javascriptObject.toString();
    }
}
