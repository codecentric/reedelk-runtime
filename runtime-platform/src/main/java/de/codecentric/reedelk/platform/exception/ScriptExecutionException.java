package de.codecentric.reedelk.platform.exception;

import de.codecentric.reedelk.platform.commons.Messages;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.script.ScriptBlock;
import de.codecentric.reedelk.runtime.api.commons.StackTraceUtils;

public class ScriptExecutionException extends PlatformException {

    public ScriptExecutionException(ScriptBlock scriptBlock, Throwable cause) {
        super(messageFrom(scriptBlock, cause), cause);
    }

    private static String messageFrom(ScriptBlock scriptBlock, Throwable cause) {
        String body = scriptBlock.body();
        String error = StackTraceUtils.rootCauseMessageOf(cause);
        return Messages.Script.SCRIPT_EXECUTION_ERROR.format(error, body);
    }
}
