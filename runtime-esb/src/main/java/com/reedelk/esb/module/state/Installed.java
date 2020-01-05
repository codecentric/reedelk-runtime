package com.reedelk.esb.module.state;

public class Installed extends AbstractState {

    @Override
    public ModuleState state() {
        return ModuleState.INSTALLED;
    }
}
