package de.codecentric.reedelk.runtime.commons;

public class ModuleProperties {

    private ModuleProperties() {
    }

    public static class System {

        private System() {
        }

        // The name of the core runtime modules. The system components under this
        // name are all the ones defined in com.reedelk.runtime.component package.
        public static final String MODULE_NAME = "flow-control";
    }

    public static class Bundle {

        private Bundle() {
        }

        public static final String BUILT_BY = "Built-By";
        public static final String BUILD_JDK = "Build-Jdk";
        public static final String CREATED_BY = "Created-By";
        public static final String BUNDLE_NAME = "Bundle-Name";
        public static final String MODULE_VERSION = "Bundle-Version";
        public static final String BND_LAST_MODIFIED = "Bnd-LastModified";
        public static final String MODULE_SYMBOLIC_NAME = "Bundle-SymbolicName";
        public static final String BUNDLE_MANIFEST_VERSION = "Bundle-ManifestVersion";
        public static final String INTEGRATION_MODULE_HEADER = "Integration-Module";
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

    public static class Assets {

        private Assets() {
        }

        public static final String RESOURCE_DIRECTORY = "/assets";
    }

    public static class Resource {

        private Resource() {
        }

        // Resources are all the files in the src/main/resources folder
        public static final String RESOURCE_DIRECTORY = "/";
    }
}
