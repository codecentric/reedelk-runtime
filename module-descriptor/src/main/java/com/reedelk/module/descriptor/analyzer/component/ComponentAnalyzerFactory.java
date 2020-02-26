package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.analyzer.property.PropertyAnalyzer;
import io.github.classgraph.ScanResult;

public class ComponentAnalyzerFactory {

    private ComponentAnalyzerFactory() {
    }

    public static ComponentAnalyzer get(ScanResult scanResult) {
        ComponentAnalyzerContext context = new ComponentAnalyzerContext(scanResult);
        PropertyAnalyzer propertyAnalyzer = new PropertyAnalyzer(context);
        return new ComponentAnalyzer(propertyAnalyzer);
    }
}
