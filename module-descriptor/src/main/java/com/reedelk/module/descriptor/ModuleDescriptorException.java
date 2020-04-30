package com.reedelk.module.descriptor;

// TODO: Should it be renamed to Analyzer exception!??
public class ModuleDescriptorException extends RuntimeException {

    public ModuleDescriptorException(String error) {
        super(error);
    }

    public ModuleDescriptorException(Throwable cause) {
        super(cause);
    }

    public ModuleDescriptorException(String message, Throwable cause) {
        super(message, cause);
    }
}
