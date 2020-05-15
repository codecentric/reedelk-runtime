package com.reedelk.module.descriptor.analyzer.type;

import com.reedelk.module.descriptor.model.type.TypeDescriptor;
import com.reedelk.module.descriptor.model.type.TypeFunctionDescriptor;
import com.reedelk.module.descriptor.model.type.TypePropertyDescriptor;
import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.UseDefaultType;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.List;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.annotationParameterValueFrom;
import static com.reedelk.runtime.api.commons.StringUtils.EMPTY;
import static com.reedelk.runtime.api.commons.StringUtils.isNotBlank;
import static java.util.stream.Collectors.toList;

public class TypeAnalyzer {

    private final ScanResult scanResult;

    public TypeAnalyzer(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public List<TypeDescriptor> analyze() {

        ClassInfoList types = scanResult.getClassesWithAnnotation(Type.class.getName());
        return types.stream().map(classInfo -> {

            boolean global = annotationParameterValueFrom(classInfo, Type.class, "global", false);
            String description = annotationParameterValueFrom(classInfo, Type.class, "description", EMPTY);
            String listItemType = getListItemType(classInfo);
            String displayName = getDisplayName(classInfo);
            String extendsType = superClassOf(classInfo);

            TypeFunctionAnalyzer functionAnalyzer = new TypeFunctionAnalyzer(classInfo);
            List<TypeFunctionDescriptor> functions = functionAnalyzer.analyze();

            TypePropertyAnalyzer propertyAnalyzer = new TypePropertyAnalyzer(classInfo);
            List<TypePropertyDescriptor> properties = propertyAnalyzer.analyze();

            TypeDescriptor descriptor = new TypeDescriptor();
            descriptor.setListItemType(listItemType);
            descriptor.setType(classInfo.getName());
            descriptor.setExtendsType(extendsType);
            descriptor.setDisplayName(displayName);
            descriptor.setDescription(description);
            descriptor.setProperties(properties);
            descriptor.setFunctions(functions);
            descriptor.setGlobal(global);
            return descriptor;

        }).collect(toList());
    }

    private String getDisplayName(ClassInfo classInfo) {
        String displayName = annotationParameterValueFrom(classInfo, Type.class, "displayName", EMPTY);
        return isNotBlank(displayName) ? displayName : null;
    }

    private String superClassOf(ClassInfo classInfo) {
        return classInfo.getSuperclass() != null ?
                classInfo.getSuperclass().getName() : null;
    }

    private String getListItemType(ClassInfo classInfo) {
        String listItemType = annotationParameterValueFrom(classInfo, Type.class, "listItemType", UseDefaultType.class.getName());
        if (UseDefaultType.class.getName().equals(listItemType)) {
            Class<?> aClass = classInfo.loadClass();
            return List.class.isAssignableFrom(aClass) ?
                    // We only set it if the class is a list.
                    Object.class.getName() : null;
        } else {
            return listItemType; // Fully qualified name.
        }
    }
}
