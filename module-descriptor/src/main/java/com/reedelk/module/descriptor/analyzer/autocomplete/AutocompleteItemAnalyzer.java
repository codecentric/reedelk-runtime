package com.reedelk.module.descriptor.analyzer.autocomplete;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.model.AutocompleteItemDescriptor;
import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteItems;
import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;
import io.github.classgraph.*;

import java.util.ArrayList;
import java.util.List;

import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.FUNCTION;
import static java.util.stream.Collectors.toList;

// Only classes with @AutocompleteType annotation are scanned for @AutocompleteItem annotation.
// This is mandatory so that we can link an @AutocompleteItem to a type.
public class AutocompleteItemAnalyzer {

    private final ScanResult scanResult;

    public AutocompleteItemAnalyzer(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public List<AutocompleteItemDescriptor> analyze() {
        ClassInfoList autocompleteTypes = scanResult.getClassesWithAnnotation(com.reedelk.runtime.api.annotation.AutocompleteType.class.getName());
        List<AutocompleteItemDescriptor> autocomplete = new ArrayList<>();
        autocompleteTypes.forEach(classInfo -> {
            String type = ScannerUtils.annotationValueOrDefaultFrom(classInfo, com.reedelk.runtime.api.annotation.AutocompleteType.class, classInfo.getSimpleName());
            if (com.reedelk.runtime.api.annotation.AutocompleteType.USE_DEFAULT_TYPE.equals(type)) type = classInfo.getSimpleName();

            List<AutocompleteItemDescriptor> descriptorsForClass = analyze(type, classInfo);
            autocomplete.addAll(descriptorsForClass);
        });
        return autocomplete;
    }

    private List<AutocompleteItemDescriptor> analyze(String type, ClassInfo classWithAutocompleteType) {
        List<AnnotationInfo> annotationInfos = ScannerUtils.repeatableAnnotation(classWithAutocompleteType, AutocompleteItem.class, AutocompleteItems.class);

        // Annotations defined in the class
        List<AutocompleteItemDescriptor> classAutocompleteItems = annotationInfos.stream().map(annotationInfo -> {
            String token = ScannerUtils.stringParameterValueFrom(annotationInfo, "token");
            String returnType = getReturnTypeFrom(annotationInfo, Void.class.getSimpleName());
            String description = ScannerUtils.stringParameterValueFrom(annotationInfo, "description");
            String example = ScannerUtils.stringParameterValueFrom(annotationInfo, "example");
            String replaceValue = ScannerUtils.stringParameterValueFrom(annotationInfo, "replaceValue");
            int cursorOffset = ScannerUtils.getParameterValue("cursorOffset", 0, annotationInfo);
            AutocompleteItemType itemType = ScannerUtils.getParameterValue("itemType", FUNCTION, annotationInfo);

            return AutocompleteItemDescriptor.create()
                    .itemType(itemType)
                    .type(type)
                    .token(token)
                    .example(example)
                    .returnType(returnType)
                    .description(description)
                    .replaceValue(replaceValue)
                    .cursorOffset(cursorOffset)
                    .build();
        }).collect(toList());


        List<AutocompleteItemDescriptor> methodsAutocompleteItems = classWithAutocompleteType.getMethodInfo()
                .filter(methodInfo -> methodInfo.hasAnnotation(AutocompleteItem.class.getName()))
                .stream()
                .map(methodInfo -> {
                    AnnotationInfo annotationInfo = methodInfo.getAnnotationInfo(AutocompleteItem.class.getName());
                    String token = ScannerUtils.stringParameterValueFrom(annotationInfo, "token", AutocompleteItem.USE_DEFAULT_TOKEN);
                    if (AutocompleteItem.USE_DEFAULT_TOKEN.equals(token)) token = methodInfo.getName();

                    String returnType = getReturnTypeFrom(annotationInfo, getReturnType(methodInfo));
                    String description = ScannerUtils.stringParameterValueFrom(annotationInfo, "description", "");
                    String example = ScannerUtils.stringParameterValueFrom(annotationInfo, "example");
                    String replaceValue = ScannerUtils.stringParameterValueFrom(annotationInfo, "replaceValue", token + "()");
                    int cursorOffset = ScannerUtils.getParameterValue("cursorOffset", 0, annotationInfo);

                    return AutocompleteItemDescriptor.create()
                            .itemType(FUNCTION) // Must be a function because it is defined above a method.
                            .type(type)
                            .token(token)
                            .example(example)
                            .returnType(returnType)
                            .description(description)
                            .replaceValue(replaceValue)
                            .cursorOffset(cursorOffset)
                            .build();
                }).collect(toList());

        classAutocompleteItems.addAll(methodsAutocompleteItems);
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
