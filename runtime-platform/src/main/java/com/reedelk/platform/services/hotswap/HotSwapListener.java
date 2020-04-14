package com.reedelk.platform.services.hotswap;

public interface HotSwapListener {

    void hotSwap(long moduleId, String resourcesRootDirectory);

}
