package com.reedelk.runtime.commons;

public class ModuleProperties {

    private ModuleProperties() {
    }

    public static class Bundle {

        private Bundle() {
        }

        public static final String MODULE_HEADER_NAME = "ESB-Module";
        public static final String MODULE_SYMBOLIC_NAME = "Bundle-SymbolicName";
        public static final String MODULE_VERSION = "Bundle-Version";
    }

    public static class Flow {

        private Flow() {
        }

        public static final String RESOURCE_DIRECTORY = "/flows";
        public static final String ROOT_PROPERTY = "flow";
    }

    public static class Subflow {

        private Subflow() {
        }

        public static final String RESOURCE_DIRECTORY = "/flows";
        public static final String ROOT_PROPERTY = "subflow";
    }

    public static class Config {

        private Config() {
        }

        public static final String RESOURCE_DIRECTORY = "/configs";
    }

    public static class Script {

        private Script() {
        }

        public static final String RESOURCE_DIRECTORY = "/scripts";
    }

    public static class Resource {

        private Resource() {
        }

        // Resources are all the files in the src/main/resources folder
        public static final String RESOURCE_DIRECTORY = "/";
    }
}