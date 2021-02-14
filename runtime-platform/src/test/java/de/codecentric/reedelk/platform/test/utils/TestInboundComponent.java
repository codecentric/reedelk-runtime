package de.codecentric.reedelk.platform.test.utils;

import de.codecentric.reedelk.runtime.api.component.Inbound;
import de.codecentric.reedelk.runtime.api.component.InboundEventListener;

public class TestInboundComponent implements Inbound {

    private String message;

    @Override
    public void onStart() {
        throw new UnsupportedOperationException("Test Only Inbound");
    }

    @Override
    public void onShutdown() {
        throw new UnsupportedOperationException("Test Only Inbound");
    }

    @Override
    public void removeEventListener() {
        throw new UnsupportedOperationException("Test Only Inbound");
    }

    @Override
    public void addEventListener(InboundEventListener listener) {
        throw new UnsupportedOperationException("Test Only Inbound");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
