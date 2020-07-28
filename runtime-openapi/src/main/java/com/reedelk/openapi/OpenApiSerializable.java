package com.reedelk.openapi;

import java.util.Map;

public interface OpenApiSerializable {

    /**
     * Open API serialize.
     */
    Map<String,Object> serialize(OpenApiSerializableContext context);

    /**
     * Open API deserialize.
     */
    void deserialize(Map<String,Object> serialized);

}
