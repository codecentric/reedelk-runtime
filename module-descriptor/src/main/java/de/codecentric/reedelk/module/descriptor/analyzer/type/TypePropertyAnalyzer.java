package de.codecentric.reedelk.module.descriptor.analyzer.type;

import de.codecentric.reedelk.module.descriptor.ModuleDescriptorException;
import de.codecentric.reedelk.module.descriptor.model.type.TypePropertyDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.TypeProperties;
import de.codecentric.reedelk.runtime.api.annotation.TypeProperty;
import de.codecentric.reedelk.runtime.api.annotation.UseDefaultType;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;

import java.util.List;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.parameterValueFrom;
import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.repeatableAnnotation;
import static de.codecentric.reedelk.runtime.api.commons.StringUtils.EMPTY;
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
            String name = parameterValueFrom(annotationInfo, "name", TypeProperty.USE_DEFAULT_NAME);
            String description = parameterValueFrom(annotationInfo, "description", EMPTY);
            String example = parameterValueFrom(annotationInfo, "example", EMPTY);

            if (TypeProperty.USE_DEFAULT_NAME.equals(name)) {
                String error = String.format("Name property must be defined for class level @TypeProperty annotations (class: %s).", classInfo.getName());
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
        return classInfo.getDeclaredFieldInfo()
                .filter(fieldInfo -> fieldInfo.hasAnnotation(TypeProperty.class.getName()))
                .stream()
                .map(fieldInfo -> {
                    AnnotationInfo annotationInfo = fieldInfo.getAnnotationInfo(TypeProperty.class.getName());
                    String name = parameterValueFrom(annotationInfo, "name", TypeProperty.USE_DEFAULT_NAME);
                    String description = parameterValueFrom(annotationInfo, "description", EMPTY);
                    String example = parameterValueFrom(annotationInfo, "example", EMPTY);

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
        String type = parameterValueFrom(annotationInfo, "type", UseDefaultType.class.getName());
        if (UseDefaultType.class.getName().equals(type)) {
            throw new ModuleDescriptorException("Return type must be defined for class level @TypeProperty annotations.");
        } else {
            return type; // Fully qualified name.
        }
    }

    private String getTypeFrom(AnnotationInfo annotationInfo, FieldInfo fieldInfo) {
        String type = parameterValueFrom(annotationInfo, "type", UseDefaultType.class.getName());
        return UseDefaultType.class.getName().equals(type) ?
                fieldInfo.getTypeDescriptor().toString() : type; // Fully qualified name.
    }
}
