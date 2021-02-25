package de.codecentric.reedelk.module.descriptor;

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
