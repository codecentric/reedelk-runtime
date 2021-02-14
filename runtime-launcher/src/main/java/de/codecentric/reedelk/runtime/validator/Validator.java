package de.codecentric.reedelk.runtime.validator;

public interface Validator {

    boolean validate();

    String error();
}
