package com.reedelk.module.descriptor.analyzer.type;

import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.model.type.TypePropertyDescriptor;
import com.reedelk.runtime.api.annotation.TypeProperties;
import com.reedelk.runtime.api.annotation.TypeProperty;
import com.reedelk.runtime.api.annotation.UseDefaultType;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;

import java.util.List;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;
import static java.util.stream.Collectors.toList;

// Only classes with @Type annotation are scanned for @TypeProperty annotations.
// This is needed in order to link a property to a type.
public class TypePropertyAnalyzer {

    private final ClassInfo classInfo;

    public TypePropertyAnalyzer(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    public List<TypePropertyDescriptor> analyze() {
        // We must return definitions for @TypeProperty annotations on top of the class name
        // and on top of each field in the class with @TypeFunction annotation on it.
        List<TypePropertyDescriptor> classLevelTypeProperties = classLevelTypeProperties();
        List<TypePropertyDescriptor> fieldLevelTypeProperties = fieldLevelTypeProperties();
        classLevelTypeProperties.addAll(fieldLevelTypeProperties);
        return classLevelTypeProperties;
    }

    private List<TypePropertyDescriptor> classLevelTypeProperties() {
        return repeatableAnnotation(classInfo, TypeProperty.class, TypeProperties.class).stream().map(annotationInfo -> {
            String description = stringParameterValueFrom(annotationInfo, "description");
            String example = stringParameterValueFrom(annotationInfo, "example");
            String name = stringParameterValueFrom(annotationInfo, "name");

            if (TypeProperty.USE_DEFAULT_NAME.equals(name)) {
                String error = String.format("Type property name must be defined for class level @TypeProperty annotations (class: %s).", classInfo.getName());
                throw new ModuleDescriptorException(error);
            }

            // Return type is mandatory for class level @TypeProperty definitions. From class fields we can
            // infer the return type from the field definition but not in this case.
            String type = getTypeFromOrThrowWhenDefault(annotationInfo);

            TypePropertyDescriptor descriptor = new TypePropertyDescriptor();
            descriptor.setDescription(description);
            descriptor.setExample(example);
            descriptor.setName(name);
            descriptor.setType(type);
            return descriptor;

        }).collect(toList());
    }

    private List<TypePropertyDescriptor> fieldLevelTypeProperties() {
        return classInfo.getFieldInfo()
                .filter(fieldInfo -> fieldInfo.hasAnnotation(TypeProperty.class.getName()))
                .stream()
                .map(fieldInfo -> {
                    AnnotationInfo annotationInfo = fieldInfo.getAnnotationInfo(TypeProperty.class.getName());
                    String description = stringParameterValueFrom(annotationInfo, "description");
                    String example = stringParameterValueFrom(annotationInfo, "example");
                    String name = stringParameterValueFrom(annotationInfo, "name");

                    String realName = TypeProperty.USE_DEFAULT_NAME.equals(name) ? fieldInfo.getName() : name;
                    String fieldType = getTypeFrom(annotationInfo, fieldInfo); // The field type is inferred from the field definition.

                    TypePropertyDescriptor descriptor = new TypePropertyDescriptor();
                    descriptor.setDescription(description);
                    descriptor.setExample(example);
                    descriptor.setType(fieldType);
                    descriptor.setName(realName);
                    return descriptor;

                }).collect(toList());
    }

    private String getTypeFromOrThrowWhenDefault(AnnotationInfo annotationInfo) {
        String type = getParameterValue("type", UseDefaultType.class.getName(), annotationInfo);
        if (UseDefaultType.class.getName().equals(type)) {
            throw new ModuleDescriptorException("Return type must be defined for class level @TypeProperty annotations.");
        } else {
            return type; // Fully qualified name.
        }
    }

    private String getTypeFrom(AnnotationInfo annotationInfo, FieldInfo fieldInfo) {
        String type = getParameterValue("type", UseDefaultType.class.getName(), annotationInfo);
        return UseDefaultType.class.getName().equals(type) ?
                fieldInfo.getTypeDescriptor().toStringWithSimpleNames() : type; // Fully qualified name.
    }
}
