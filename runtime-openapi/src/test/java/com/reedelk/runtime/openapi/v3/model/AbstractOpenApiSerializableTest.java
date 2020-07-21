package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.OpenApiJsons;
import com.reedelk.runtime.openapi.v3.OpenApiSerializable;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public abstract class AbstractOpenApiSerializableTest {

    protected void assertSerializedCorrectly(OpenApiSerializable serializable, OpenApiJsons.Provider expected) {
        ComponentsObject componentsObject = new ComponentsObject();
        OpenApiSerializableContext context = new OpenApiSerializableContext(componentsObject);
        String actualJson = serializable.toJson(context);
        assertSerializedCorrectly(actualJson, expected);
    }

    protected void assertSerializedCorrectly(OpenApiSerializableContext context, OpenApiSerializable serializable, OpenApiJsons.Provider expected) {
        String actualJson = serializable.toJson(context);
        assertSerializedCorrectly(actualJson, expected);
    }

    protected void assertSerializedCorrectly(String actual, OpenApiJsons.Provider expected) {
        String expectedJson = expected.string();
        JSONAssert.assertEquals(expectedJson, actual, JSONCompareMode.STRICT);
    }
}
