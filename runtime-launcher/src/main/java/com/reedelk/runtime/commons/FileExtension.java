package com.reedelk.runtime.commons;

import java.io.File;

public enum FileExtension {

    JAR(".jar");

    public final String val;

    FileExtension(String val) {
        this.val = val;
    }

    public boolean is(File file) {
        return file != null && is(file.getName());
    }

    public boolean is(String filename) {
        return filename.endsWith(val);
    }
}
