package com.reedelk.module.descriptor.analyzer;

import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class AnalyzerTestUtils {

    public static ClassInfo classInfoOf(Class<?> target) {
        ScanResult scanResult = ScannerTestUtils.scanWithResult(target);
        return scanResult.getClassInfo(target.getName());
    }
}
