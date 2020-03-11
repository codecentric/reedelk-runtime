package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.analyzer.component.UnsupportedType;
import com.reedelk.module.descriptor.analyzer.property.type.KnownTypeDescriptor;
import com.reedelk.module.descriptor.model.*;
import com.reedelk.runtime.api.annotation.DisplayName;
import com.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class TypeAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        TypeSignature typeSignature = fieldInfo.getTypeDescriptor();

        // Primitive
        if (typeSignature instanceof BaseTypeSignature) {
            Class<?> clazz = ((BaseTypeSignature) typeSignature).getType();
            TypePrimitiveDescriptor typeDescriptor = new TypePrimitiveDescriptor();
            typeDescriptor.setType(clazz);
            builder.type(typeDescriptor);

            // Non primitive: String, BigDecimal, DynamicString, ...
        } else if (typeSignature instanceof ClassRefTypeSignature) {
            ClassRefTypeSignature classRef = (ClassRefTypeSignature) typeSignature;
            TypeDescriptor typeDescriptor = processClassRefType(classRef, fieldInfo, context);
            builder.type(typeDescriptor);

        } else {
            throw new UnsupportedType(typeSignature.getClass());
        }
    }

    private TypeDescriptor processClassRefType(ClassRefTypeSignature typeSignature, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        String fullyQualifiedClassName = typeSignature.getFullyQualifiedClassName();

        if (isEnumeration(fullyQualifiedClassName, context)) {
            return processEnumType(typeSignature, context);

            // For example: String, Integer, Float, DynamicString ...
        } else if (PlatformTypes.isSupported(fullyQualifiedClassName)) {
            Class<?> clazz = clazzByFullyQualifiedName(fullyQualifiedClassName);
            return KnownTypeDescriptor.from(clazz, fieldInfo);

        } else {
            // We check that it is a user defined object type (with Implementor).
            // We check that we can resolve class info. If we can, then ..
            ClassInfo classInfo = context.getClassInfo(fullyQualifiedClassName);
            if (classInfo == null) {
                throw new UnsupportedType(fullyQualifiedClassName);
            }
            Shared shared = isShareable(classInfo);
            Collapsible collapsible = isCollapsible(classInfo);
            PropertyAnalyzer propertyAnalyzer = new PropertyAnalyzer(context);

            List<PropertyDescriptor> allProperties = classInfo
                    .getFieldInfo()
                    .stream()
                    .map(propertyAnalyzer::analyze)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(toList());

            TypeObjectDescriptor descriptor = new TypeObjectDescriptor();
            descriptor.setTypeFullyQualifiedName(fullyQualifiedClassName);
            descriptor.setObjectProperties(allProperties);
            descriptor.setShared(shared);
            descriptor.setCollapsible(collapsible);
            return descriptor;
        }
    }

    private TypeEnumDescriptor processEnumType(ClassRefTypeSignature enumRefType, ComponentAnalyzerContext context) {
        String enumFullyQualifiedClassName = enumRefType.getFullyQualifiedClassName();
        ClassInfo enumClassInfo = context.getClassInfo(enumFullyQualifiedClassName);
        FieldInfoList declaredFieldInfo = enumClassInfo.getDeclaredFieldInfo();
        Map<String, String> nameAndDisplayName = declaredFieldInfo
                .stream()
                .filter(filterByFullyQualifiedClassNameType(enumFullyQualifiedClassName))
                .collect(toMap(FieldInfo::getName, fieldInfo ->
                        annotationValueOrDefaultFrom(fieldInfo, DisplayName.class, fieldInfo.getName())));

        TypeEnumDescriptor descriptor = new TypeEnumDescriptor();
        descriptor.setNameAndDisplayNameMap(nameAndDisplayName);
        return descriptor;
    }
}
