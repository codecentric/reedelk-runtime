package com.reedelk.openapi;

import java.util.Map;

public interface OpenApiSerializable1 {

    /**
     * Open API serialize.
     */
    Map<String,Object> serialize(OpenApiSerializableContext1 context);

    /**
     * Open API deserialize.
     */
    void deserialize(Map<String,Object> serialized);

}
