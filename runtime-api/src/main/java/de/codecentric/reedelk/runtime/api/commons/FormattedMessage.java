package de.codecentric.reedelk.runtime.api.commons;

public interface FormattedMessage {

    String template();

    default String format(Object ...args) {
        return String.format(template(), args);
    }
}
