package com.reedelk.module.descriptor.analyzer.autocomplete;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.model.AutocompleteItemDescriptor;
import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteItems;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;
import com.reedelk.runtime.api.commons.StringUtils;
import io.github.classgraph.*;

import java.util.ArrayList;
import java.util.List;

import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.*;
import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.FUNCTION;
import static java.util.stream.Collectors.toList;

public class AutocompleteAnalyzer {

    private final ScanResult scanResult;

    public AutocompleteAnalyzer(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public List<AutocompleteItemDescriptor> analyzeAutocompleteItems() {
        ClassInfoList classesWithAutocompleteTypeAnnotations = scanResult.getClassesWithAnnotation(AutocompleteType.class.getName());
        List<AutocompleteItemDescriptor> autocomplete = new ArrayList<>();
        classesWithAutocompleteTypeAnnotations.forEach(classInfo -> {
            String type = ScannerUtils.annotationValueOrDefaultFrom(classInfo, AutocompleteType.class, classInfo.getSimpleName());
            if (AutocompleteType.USE_DEFAULT_TYPE.equals(type)) type = classInfo.getSimpleName();

            Boolean global = ScannerUtils.annotationParameterValueOrDefaultFrom(classInfo, AutocompleteType.class, "global", false);
            if (global) {
                String description = ScannerUtils.annotationParameterValueOrDefaultFrom(classInfo, AutocompleteType.class, "description", StringUtils.EMPTY);
                AutocompleteItemDescriptor globalItemDescriptor = AutocompleteItemDescriptor.create()
                        .type(type)
                        .token(type)
                        .global(true)
                        .returnType(type)
                        .replaceValue(type)
                        .itemType(VARIABLE)
                        .description(description)
                        .build();
                autocomplete.add(globalItemDescriptor);
            }

            List<AutocompleteItemDescriptor> descriptorsForClass = analyzeAutocompleteItems(type, classInfo);
            autocomplete.addAll(descriptorsForClass);
        });
        return autocomplete;
    }

    private List<AutocompleteItemDescriptor> analyzeAutocompleteItems(String type, ClassInfo classWithAutocompleteType) {
        List<AnnotationInfo> annotationInfos = ScannerUtils.repeatableAnnotation(classWithAutocompleteType, AutocompleteItem.class, AutocompleteItems.class);

        // Annotations defined in the class
        List<AutocompleteItemDescriptor> classAutocompleteItems = annotationInfos.stream().map(annotationInfo -> {
            String token = ScannerUtils.stringParameterValueFrom(annotationInfo, "token");
            String returnType = getReturnTypeFrom(annotationInfo, Void.class.getSimpleName());
            String description = ScannerUtils.stringParameterValueFrom(annotationInfo, "description");
            String replaceValue = ScannerUtils.stringParameterValueFrom(annotationInfo, "replaceValue");
            int cursorOffset = ScannerUtils.getParameterValue("cursorOffset", 0, annotationInfo);
            AutocompleteItemType itemType = ScannerUtils.getParameterValue("itemType", FUNCTION, annotationInfo);

            return AutocompleteItemDescriptor.create()
                    .itemType(itemType)
                    .type(type)
                    .token(token)
                    .returnType(returnType)
                    .description(description)
                    .replaceValue(replaceValue)
                    .cursorOffset(cursorOffset)
                    .build();
        }).collect(toList());


        List<AutocompleteItemDescriptor> collect = classWithAutocompleteType.getMethodInfo()
                .filter(methodInfo -> methodInfo.hasAnnotation(AutocompleteItem.class.getName()))
                .stream()
                .map(methodInfo -> {
                    AnnotationInfo annotationInfo = methodInfo.getAnnotationInfo(AutocompleteItem.class.getName());
                    String token = ScannerUtils.stringParameterValueFrom(annotationInfo, "token", AutocompleteItem.USE_DEFAULT_TOKEN);
                    if (AutocompleteItem.USE_DEFAULT_TOKEN.equals(token)) token = methodInfo.getName();

                    String returnType = getReturnTypeFrom(annotationInfo, getReturnType(methodInfo));

                    String description = ScannerUtils.stringParameterValueFrom(annotationInfo, "description", "");
                    String replaceValue = ScannerUtils.stringParameterValueFrom(annotationInfo, "replaceValue", token + "()");

                    int cursorOffset = ScannerUtils.getParameterValue("cursorOffset", 0, annotationInfo);

                    return AutocompleteItemDescriptor.create()
                            .itemType(FUNCTION) // Must be a function because it is defined above a method.
                            .type(type)
                            .token(token)
                            .returnType(returnType)
                            .description(description)
                            .replaceValue(replaceValue)
                            .cursorOffset(cursorOffset)
                            .build();
                }).collect(toList());
        classAutocompleteItems.addAll(collect);
        return classAutocompleteItems;
    }

    private String getReturnTypeFrom(AnnotationInfo annotationInfo, String defaultReturnType) {
        String returnType = ScannerUtils.getParameterValue("returnType", AutocompleteItem.UseDefaultReturnType.class.getName(), annotationInfo);
        String realReturnType;
        if (AutocompleteItem.UseDefaultReturnType.class.getName().equals(returnType)) {
            realReturnType = defaultReturnType;
        } else {
            String[] split = returnType.split("\\.");
            realReturnType = split[split.length - 1]; // We just keep the simple name of the Fully Qualified Class Name.
        }
        return realReturnType;
    }

    private String getReturnType(MethodInfo methodInfo) {
        TypeSignature resultType = methodInfo.getTypeDescriptor().getResultType();
        return resultType.toStringWithSimpleNames();
    }
}
