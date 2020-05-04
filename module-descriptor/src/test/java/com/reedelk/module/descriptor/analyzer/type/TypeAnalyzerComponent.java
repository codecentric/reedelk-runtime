package com.reedelk.module.descriptor.analyzer.type;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;
import com.reedelk.runtime.api.annotation.TypeProperty;
import com.reedelk.runtime.api.message.MessageAttributes;

import java.util.Map;

@Type(global = true,
        description = "TypeAnalyzerComponent description",
        displayName = "MyTypeAnalyzerComponent",
        listItemType = Map.class)
public class TypeAnalyzerComponent extends MessageAttributes {

    @TypeProperty
    public String property1;

    @TypeFunction
    public String method0(String value) {
        return "test";
    }
}
