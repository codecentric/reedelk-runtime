package de.codecentric.reedelk.module.descriptor.analyzer.type;

import de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils;
import de.codecentric.reedelk.module.descriptor.model.type.TypeDescriptor;
import de.codecentric.reedelk.module.descriptor.model.type.TypeFunctionDescriptor;
import de.codecentric.reedelk.module.descriptor.model.type.TypePropertyDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.Type;
import de.codecentric.reedelk.runtime.api.annotation.UseDefaultType;
import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.List;
import java.util.Map;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.annotationParameterValueFrom;
import static java.util.stream.Collectors.toList;

public class TypeAnalyzer {

    private final ScanResult scanResult;

    public TypeAnalyzer(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public List<TypeDescriptor> analyze() {

        ClassInfoList types = scanResult.getClassesWithAnnotation(Type.class.getName());
        return types.stream().map(classInfo -> {

            boolean global = ScannerUtils.annotationParameterValueFrom(classInfo, Type.class, "global", false);
            String description = ScannerUtils.annotationParameterValueFrom(classInfo, Type.class, "description", StringUtils.EMPTY);
            String mapValueType = getMapValueType(classInfo);
            String listItemType = getListItemType(classInfo);
            String displayName = getDisplayName(classInfo);
            String extendsType = superClassOf(classInfo);
            String mapKeyType = getMapKeyType(classInfo);

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
            descriptor.setMapValueType(mapValueType);
            descriptor.setMapKeyType(mapKeyType);
            descriptor.setProperties(properties);
            descriptor.setFunctions(functions);
            descriptor.setGlobal(global);
            return descriptor;

        }).collect(toList());
    }

    private String getDisplayName(ClassInfo classInfo) {
        String displayName = ScannerUtils.annotationParameterValueFrom(classInfo, Type.class, "displayName", StringUtils.EMPTY);
        return StringUtils.isNotBlank(displayName) ? displayName : null;
    }

    private String superClassOf(ClassInfo classInfo) {
        return classInfo.getSuperclass() != null ?
                classInfo.getSuperclass().getName() : null;
    }

    private String getListItemType(ClassInfo classInfo) {
        String listItemType = ScannerUtils.annotationParameterValueFrom(classInfo, Type.class, "listItemType", UseDefaultType.class.getName());
        if (UseDefaultType.class.getName().equals(listItemType)) {
            Class<?> aClass = classInfo.loadClass();
            return List.class.isAssignableFrom(aClass) ?
                    // We only set it if the class is a list.
                    Object.class.getName() : null;
        } else {
            return listItemType; // Fully qualified name.
        }
    }

    private String getMapKeyType(ClassInfo classInfo) {
        String mapKeyType = ScannerUtils.annotationParameterValueFrom(classInfo, Type.class, "mapKeyType", UseDefaultType.class.getName());
        if (UseDefaultType.class.getName().equals(mapKeyType)) {
            Class<?> aClass = classInfo.loadClass();
            return Map.class.isAssignableFrom(aClass) ?
                    // We only set it if the class is a map.
                    Object.class.getName() : null;
        } else {
            return mapKeyType; // Fully qualified name.
        }
    }

    private String getMapValueType(ClassInfo classInfo) {
        String mapValueType = ScannerUtils.annotationParameterValueFrom(classInfo, Type.class, "mapValueType", UseDefaultType.class.getName());
        if (UseDefaultType.class.getName().equals(mapValueType)) {
            Class<?> aClass = classInfo.loadClass();
            return Map.class.isAssignableFrom(aClass) ?
                    // We only set it if the class is a map.
                    Object.class.getName() : null;
        } else {
            return mapValueType; // Fully qualified name.
        }
    }
}
