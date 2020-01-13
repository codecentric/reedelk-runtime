package com.reedelk.module.descriptor.analyzer.component;

import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class ComponentAnalyzerContext {

    private final ScanResult scanResult;

    public ComponentAnalyzerContext(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public ClassInfo getClassInfo(String fullyQualifiedClassName) {
        return scanResult.getClassInfo(fullyQualifiedClassName);
    }
}
