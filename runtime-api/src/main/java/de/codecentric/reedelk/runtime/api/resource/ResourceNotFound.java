package de.codecentric.reedelk.runtime.api.resource;

public class ResourceNotFound extends RuntimeException {

    public ResourceNotFound(String message) {
        super(message);
    }
}
