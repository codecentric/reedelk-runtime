package de.codecentric.reedelk.module.descriptor.analyzer.type;

import de.codecentric.reedelk.runtime.api.annotation.TypeProperty;

@TypeProperty(name = "property1", example = "Util.property1", description = "Property1 description", type = String.class)
@TypeProperty(name = "property2", example = "Util.property2", description = "Property2 description", type = long.class)
public class TypePropertyComponent {

    @TypeProperty(example = "context.correlationId", description = "Returns the current flow correlation id.")
    public String correlationId;

}
