package com.reedelk.openapi.v3.model;

import com.reedelk.openapi.OpenApiSerializable1;
import com.reedelk.openapi.OpenApiSerializableContext1;
import com.reedelk.openapi.OpenApiSerializer;
import com.reedelk.openapi.v3.Fixture;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractOpenApiSerializableTest {

    protected void assertSerializeJSON(OpenApiSerializable1 serializable, Fixture.Provider expected) {
        OpenApiSerializableContext1 context = new OpenApiSerializableContext1();
        String actualJson = OpenApiSerializer.toJson(serializable, context);
        assertSerializeJSON(actualJson, expected);
    }

    protected void assertSerializeYAML(OpenApiSerializable1 serializable, Fixture.Provider expected) {
        OpenApiSerializableContext1 context = new OpenApiSerializableContext1();
        String actualYaml = OpenApiSerializer.toYaml(serializable, context);
        String expectedYaml = expected.string();
        assertThat(actualYaml).isEqualToNormalizingNewlines(expectedYaml);
    }

    protected void assertSerializeJSON(OpenApiSerializableContext1 context, OpenApiSerializable1 serializable, Fixture.Provider expected) {
        String actualJson = OpenApiSerializer.toJson(serializable, context);
        assertSerializeJSON(actualJson, expected);
    }

    protected void assertSerializeJSON(String actual, Fixture.Provider expected) {
        String expectedJson = expected.string();
        JSONAssert.assertEquals(expectedJson, actual, JSONCompareMode.STRICT);
    }
}
