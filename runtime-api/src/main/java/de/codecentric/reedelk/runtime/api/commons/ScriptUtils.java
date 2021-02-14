package de.codecentric.reedelk.runtime.api.commons;

import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

import java.util.Arrays;
import java.util.Collection;

public class ScriptUtils {

    public static final String EMPTY_SCRIPT = "#[]";
    public static final String EVALUATE_PAYLOAD = "#[message.payload()]";

    // payload is just an alias for 'message.content.data()'
    private static final Collection<String> EVALUATE_PAYLOAD_TOKENS =
            Arrays.asList("message.payload()", "message.content().data()");

    public static boolean isScript(Object value) {
        return value instanceof String && isScript((String) value);
    }

    public static boolean isScript(String value) {
        if (value != null) {
            String trimmedValue = value.trim();
            return trimmedValue.startsWith("#[") && trimmedValue.endsWith("]");
        }
        return false;
    }

    // Remove:#[ and ] from the  beginning and the end of the text
    public static String unwrap(String value) {
        return isScript(value) ?
                value.substring(2, value.length() - 1) :
                value;
    }

    public static String asScript(String value) {
        return value == null ?
                "#[]" :
                "#[" + value + "]";
    }

    /**
     * Tests whether the DynamicValue contains only the message payload.
     * Note: 'message.content.data' is the extended version of 'payload' and
     *  'payload' is the shortcut of 'message.content.data'
     * @return true if the script evaluates the message 'payload', false otherwise.
     */
    public static boolean isEvaluateMessagePayload(DynamicValue<?> dynamicValue) {
        if (dynamicValue == null) {
            return false;
        } else if (dynamicValue.isScript() && StringUtils.isNotBlank(dynamicValue.body())) {
            String unwrappedScript = unwrap(dynamicValue.body());
            return EVALUATE_PAYLOAD_TOKENS.contains(StringUtils.trim(unwrappedScript));
        } else {
            return false;
        }
    }

    public static boolean isBlank(String script) {
        String unwrappedScript = ScriptUtils.unwrap(script);
        return StringUtils.isBlank(unwrappedScript);
    }

    public static boolean isNotBlank(String script) {
        String unwrappedScript = ScriptUtils.unwrap(script);
        return StringUtils.isNotBlank(unwrappedScript);
    }
}
