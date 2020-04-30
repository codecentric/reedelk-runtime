package com.reedelk.module.descriptor.analyzer.commons;

public class Utils {

    public static String simpleNameFrom(String fullyQualifiedName) {
        String[] split = fullyQualifiedName.split("\\.");
        // We just keep the simple name of the Fully Qualified Class Name.
        return split[split.length - 1];
    }
}
