package com.reedelk.platform.exception;

import com.reedelk.runtime.api.exception.PlatformException;
import com.reedelk.runtime.api.script.ScriptBlock;

import static com.reedelk.platform.commons.Messages.Script.SCRIPT_EXECUTION_ERROR;
import static com.reedelk.runtime.api.commons.StackTraceUtils.rootCauseMessageOf;

public class ScriptExecutionException extends PlatformException {

    public ScriptExecutionException(ScriptBlock scriptBlock, Throwable cause) {
        super(messageFrom(scriptBlock, cause), cause);
    }

    private static String messageFrom(ScriptBlock scriptBlock, Throwable cause) {
        String body = scriptBlock.body();
        String error = rootCauseMessageOf(cause);
        return SCRIPT_EXECUTION_ERROR.format(error, body);
    }
}
