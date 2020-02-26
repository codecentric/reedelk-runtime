package com.reedelk.runtime.api.flow;

import java.io.Serializable;
import java.util.Map;

public interface FlowContext extends Map<String, Serializable>, Disposable {

    String TYPE = "FlowContext";

    /**
     * Registers a disposable object in the context so that it can
     * be disposed when the flow execution ends. This might be
     * used to cleanup Database cursors or other resources which needs
     * to stay open across the whole flow execution.
     *
     * @param disposable the object to be disposed when the flow ends.
     */
    void register(Disposable disposable);
}
