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
import java.util.Map;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.annotationParameterValueOrDefaultFrom;
import static com.reedelk.runtime.api.commons.StringUtils.EMPTY;
import static java.util.stream.Collectors.toList;

public class TypeAnalyzer {

    private final ScanResult scanResult;

    public TypeAnalyzer(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public List<TypeDescriptor> analyze() {

        ClassInfoList types = scanResult.getClassesWithAnnotation(Type.class.getName());
        return types.stream().map(classInfo -> {

            boolean global = annotationParameterValueOrDefaultFrom(classInfo, Type.class, "global", false);
            String description = annotationParameterValueOrDefaultFrom(classInfo, Type.class, "description", EMPTY);
            String displayName = displayNameFrom(classInfo);
            String listItemType = getListItemType(classInfo);

            TypeFunctionAnalyzer functionAnalyzer = new TypeFunctionAnalyzer(classInfo);
            List<TypeFunctionDescriptor> functions = functionAnalyzer.analyze();

            TypePropertyAnalyzer propertyAnalyzer = new TypePropertyAnalyzer(classInfo);
            List<TypePropertyDescriptor> properties = propertyAnalyzer.analyze();

            TypeDescriptor descriptor = new TypeDescriptor();
            descriptor.setFullyQualifiedName(classInfo.getName());
            descriptor.setListItemType(listItemType);
            descriptor.setDisplayName(displayName);
            descriptor.setDescription(description);
            descriptor.setProperties(properties);
            descriptor.setFunctions(functions);
            descriptor.setGlobal(global);
            return descriptor;

        }).collect(toList());
    }

    // We don't want to confuse the user to keep track of lots of objects implementing just Map.
    // Map types are displayed as maps, so that it is less confusing for the user.
    // We can bind to the script engine global functions with a different name than the fully qualified
    // class name. Therefore we can use the Display name to let the user know they can use the functions
    // using MyDisplayName.myFunction().
    private String displayNameFrom(ClassInfo classInfo) {
        String displayName = annotationParameterValueOrDefaultFrom(classInfo, Type.class, "displayName", Type.USE_DEFAULT_DISPLAY_NAME);
        if (Type.USE_DEFAULT_DISPLAY_NAME.equals(displayName)) {
            Class<?> aClass = classInfo.loadClass();
            if (Map.class.isAssignableFrom(aClass)) return Map.class.getSimpleName();
            if (List.class.isAssignableFrom(aClass)) return List.class.getSimpleName();
            return aClass.getSimpleName();
        } else {
            return displayName;
        }
    }

    private String getListItemType(ClassInfo classInfo) {
        String listItemType = annotationParameterValueOrDefaultFrom(classInfo, Type.class, "listItemType", UseDefaultType.class.getName());
        if (UseDefaultType.class.getName().equals(listItemType)) {
            Class<?> aClass = classInfo.loadClass();
            return List.class.isAssignableFrom(aClass) ?
                    // We only set it if the class is a list.
                    Object.class.getName() : null;
        } else {
            return listItemType;
        }
    }
}
