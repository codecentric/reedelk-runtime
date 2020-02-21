package com.reedelk.runtime.properties;

public class SharedSystemPackages {

    private SharedSystemPackages() {
    }

    public static final String DEFAULT =
            "org.osgi.service.cm," +
            "sun.misc," +
            "jdk.internal.misc," +
            "jdk.internal.miscjavax.net," +
            "jdk.nashorn.api.scripting," +
            "javax.security.cert," +
            "javax.net.ssl," +
            "javax.net," +
            "javax.crypto," +
            "javax.naming," +
            "javax.annotation," +
            "javax.annotation.meta," +
            "com.intellij.rt.debugger.agent";
}
