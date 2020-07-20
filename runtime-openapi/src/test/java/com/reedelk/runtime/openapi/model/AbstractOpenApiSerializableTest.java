package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.openapi.OpenApiJsons;
import com.reedelk.runtime.openapi.OpenApiSerializable;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public abstract class AbstractOpenApiSerializableTest {

    protected void assertSerializedCorrectly(OpenApiSerializable serializable, OpenApiJsons.Provider expected) {
        ComponentsObject componentsObject = new ComponentsObject();
        OpenApiSerializableContext context = new OpenApiSerializableContext(componentsObject);
        String actualJson = serializable.serialize(context).toString(2);
        assertSerializedCorrectly(actualJson, expected);
    }

    protected void assertSerializedCorrectly(OpenApiSerializableContext context, OpenApiSerializable serializable, OpenApiJsons.Provider expected) {
        String actualJson = serializable.serialize(context).toString(2);
        assertSerializedCorrectly(actualJson, expected);
    }

    protected void assertSerializedCorrectly(String actual, OpenApiJsons.Provider expected) {
        String expectedJson = expected.string();
        JSONAssert.assertEquals(expectedJson, actual, JSONCompareMode.STRICT);
    }
}
