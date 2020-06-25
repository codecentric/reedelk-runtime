package com.reedelk.runtime.system.api;

public interface SystemProperty {

    String configDirectory();

    String modulesDirectory();

    String homeDirectory();

    String version();

    String qualifier();

}
