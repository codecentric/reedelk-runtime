package de.codecentric.reedelk.module.descriptor.analyzer.type;

import de.codecentric.reedelk.runtime.api.annotation.Type;
import de.codecentric.reedelk.runtime.api.annotation.TypeFunction;
import de.codecentric.reedelk.runtime.api.annotation.TypeProperty;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;

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
