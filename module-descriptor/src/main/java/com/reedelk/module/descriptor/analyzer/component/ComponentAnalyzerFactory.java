package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.analyzer.property.ComponentPropertyAnalyzer;
import io.github.classgraph.ScanResult;

public class ComponentAnalyzerFactory {

    private ComponentAnalyzerFactory() {
    }

    public static ComponentAnalyzer get(ScanResult scanResult) {
        ComponentAnalyzerContext context = new ComponentAnalyzerContext(scanResult);
        ComponentPropertyAnalyzer propertyAnalyzer = new ComponentPropertyAnalyzer(context);
        return new ComponentAnalyzer(propertyAnalyzer);
    }
}
