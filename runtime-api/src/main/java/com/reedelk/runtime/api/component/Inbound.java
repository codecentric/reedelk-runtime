package com.reedelk.runtime.api.component;


public interface Inbound extends Component {

    void onStart();

    void onShutdown();

    void removeEventListener();

    void addEventListener(InboundEventListener listener);

}
