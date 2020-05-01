package com.reedelk.module.descriptor.analyzer.type;

import com.reedelk.runtime.api.annotation.TypeProperty;

@TypeProperty(name = TypePropertyComponent.PROPERTY1, type = String.class)
@TypeProperty(name = TypePropertyComponent.PROPERTY2, type = long.class)
public class TypePropertyComponent {

    static final String PROPERTY1 = "property1";
    static final String PROPERTY2 = "property2";

    @TypeProperty
    public String correlationId;

}
