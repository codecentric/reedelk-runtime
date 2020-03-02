package com.reedelk.module.descriptor.analyzer.autocomplete;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.model.AutocompleteTypeDescriptor;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.commons.StringUtils;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteTypeAnalyzer {

    private final ScanResult scanResult;

    public AutocompleteTypeAnalyzer(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public List<AutocompleteTypeDescriptor> analyze() {
        ClassInfoList autocompleteTypes = scanResult.getClassesWithAnnotation(AutocompleteType.class.getName());
        List<AutocompleteTypeDescriptor> autocomplete = new ArrayList<>();
        autocompleteTypes.forEach(classInfo -> {
            boolean global = ScannerUtils.annotationParameterValueOrDefaultFrom(classInfo, AutocompleteType.class, "global", false);
            String description = ScannerUtils.annotationParameterValueOrDefaultFrom(classInfo, AutocompleteType.class, "description", StringUtils.EMPTY);
            String type = ScannerUtils.annotationValueOrDefaultFrom(classInfo, AutocompleteType.class, classInfo.getSimpleName());
            if (AutocompleteType.USE_DEFAULT_TYPE.equals(type)) {
                type = classInfo.getSimpleName();
            }

            AutocompleteTypeDescriptor descriptor = AutocompleteTypeDescriptor.create()
                    .description(description)
                    .global(global)
                    .type(type)
                    .build();
            autocomplete.add(descriptor);
        });
        return autocomplete;
    }
}
