package com.reedelk.runtime.converter.types.scriptobjectmirrortype;

import com.reedelk.runtime.AbstractScriptEngineTest;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.jupiter.api.Test;

import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AsObjectTest extends AbstractScriptEngineTest {

    private AsObject converter = new AsObject();

    @SuppressWarnings("unchecked")
    @Test
    void shouldConvertJavascriptObjectToMap() throws ScriptException {
        // Given
        ScriptObjectMirror result = (ScriptObjectMirror) engine.eval("JSON.parse('{ \"name\":\"Mark\"}')");

        // When
        Map<String, Object> actual = (Map<String, Object>) converter.from(result);

        // Then
        assertThat(actual).containsEntry("name", "Mark");
        assertThat(actual).hasSize(1);
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldConvertJavascriptArrayToList() throws ScriptException {
        // Given
        ScriptObjectMirror result = (ScriptObjectMirror) engine.eval("['one','two','three']");

        // When
        List<Object> actual = (List<Object>) converter.from(result);

        // Then
        assertThat(actual).containsExactly("one", "two", "three");
        assertThat(actual).hasSize(3);
    }

    @Test
    void shouldNotConvertPrimitiveType() throws ScriptException {
        // Given
        String expected = "'test'";

        // When
        String result = (String) engine.eval(expected);

        // Then
        assertThat(result).isEqualTo("test");
    }
}
