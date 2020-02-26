package com.reedelk.module.descriptor.analyzer.autocomplete;

import com.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import com.reedelk.module.descriptor.model.AutocompleteItemDescriptor;
import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;
import io.github.classgraph.*;

import java.util.ArrayList;
import java.util.List;

import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.FUNCTION;
import static java.util.stream.Collectors.toList;

public class AutocompleteAnalyzer {

    private final ScanResult scanResult;

    public AutocompleteAnalyzer(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public List<AutocompleteItemDescriptor> analyze() {
        ClassInfoList classesWithAutocompleteTypeAnnotations = scanResult.getClassesWithAnnotation(AutocompleteType.class.getName());
        List<AutocompleteItemDescriptor> autocomplete = new ArrayList<>();
        classesWithAutocompleteTypeAnnotations.forEach(classInfo -> {
            String type = ScannerUtils.annotationValueOrDefaultFrom(classInfo, AutocompleteType.class, classInfo.getSimpleName());
            if (AutocompleteType.USE_DEFAULT_TYPE.equals(type)) type = classInfo.getSimpleName();
            List<AutocompleteItemDescriptor> descriptorsForClass = analyze(type, classInfo);
            autocomplete.addAll(descriptorsForClass);
        });
        return autocomplete;
    }

    private List<AutocompleteItemDescriptor> analyze(String type, ClassInfo classWithAutocompleteType) {
        AnnotationInfoList list = classWithAutocompleteType.getAnnotationInfoRepeatable(AutocompleteItem.class.getName());
        // Annotations defined in the class
        List<AutocompleteItemDescriptor> classAutocompleteItems = list.stream().map(annotationInfo -> {
            String token = ScannerUtils.stringParameterValueFrom(annotationInfo, "token");
            String returnType = ScannerUtils.stringParameterValueFrom(annotationInfo, "returnType");
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

        List<AutocompleteItemDescriptor> collect = classWithAutocompleteType.getMethodInfo().stream().map(methodInfo -> {
            AnnotationInfo annotationInfo = methodInfo.getAnnotationInfo(AutocompleteItem.class.getName());

            String token = ScannerUtils.stringParameterValueFrom(annotationInfo, "token", AutocompleteItem.USE_DEFAULT_TOKEN);
            if (AutocompleteItem.USE_DEFAULT_TOKEN.equals(token)) token = methodInfo.getName();

            String returnType = ScannerUtils.stringParameterValueFrom(annotationInfo, "returnType", AutocompleteItem.USE_DEFAULT_RETURN_TYPE);
            if (AutocompleteItem.USE_DEFAULT_RETURN_TYPE.equals(returnType)) returnType = getReturnType(methodInfo);

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

    private String getReturnType(MethodInfo methodInfo) {
        TypeSignature resultType = methodInfo.getTypeDescriptor().getResultType();
        return resultType.toStringWithSimpleNames();
    }
}
