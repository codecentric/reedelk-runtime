package com.reedelk.module.descriptor.analyzer.commons;

import com.reedelk.runtime.api.commons.FormattedMessage;

public class Messages {

    private Messages() {
    }

    public enum Scan implements FormattedMessage {

        ERROR_SCAN_COMPONENT("Error while processing component definition with qualified name=[%s]: %s");

        private final String message;

        Scan(String message) {
            this.message = message;
        }

        @Override
        public String template() {
            return message;
        }
    }
}
