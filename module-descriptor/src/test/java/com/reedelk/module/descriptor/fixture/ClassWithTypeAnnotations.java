package com.reedelk.module.descriptor.fixture;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;
import com.reedelk.runtime.api.annotation.TypeProperty;

@Type(global = true, description = "My description", displayName = "ClassWithTypeAnnotations")
@TypeProperty(name = "correlationId", type = String.class, description = "Contains the flow correlation id.")
public class ClassWithTypeAnnotations {

    @TypeProperty
    private String myTypeProperty;

    @TypeFunction(signature = "attributes()", description = "Returns the attributes", example = "message.attributes()")
    public String attributes() {
        return "My attributes";
    }

    @TypeFunction(cursorOffset = 2, signature = "contains('')", description = "Return the message typed content containing metadata")
    public boolean contains(String key) {
        return true;
    }

    @TypeFunction(description = "My description")
    public ClassWithTypeAnnotations builderMethod(String value) {
        return this;
    }

    @TypeFunction(description = "My description")
    public int getValue(String key, int defaultValue) {
        return defaultValue;
    }

    @TypeFunction(cursorOffset = 2, signature = "info('')", description = "Logs a message with INFO level")
    public void info(String message) {
        // nothing
    }
}
