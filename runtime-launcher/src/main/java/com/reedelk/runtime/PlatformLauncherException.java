package com.reedelk.runtime;

public class PlatformLauncherException extends RuntimeException {

    public PlatformLauncherException(String message, Exception e) {
        super(message, e);
    }
}
