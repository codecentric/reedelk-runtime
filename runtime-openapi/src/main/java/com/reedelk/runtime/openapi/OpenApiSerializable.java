package com.reedelk.runtime.openapi;

import org.json.JSONObject;

public interface OpenApiSerializable {

    JSONObject serialize(OpenApiSerializableContext context);

}
