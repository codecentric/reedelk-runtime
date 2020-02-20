package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.analyzer.component.UnsupportedType;
import com.reedelk.module.descriptor.model.*;
import com.reedelk.module.descriptor.model.Collapsible;
import com.reedelk.module.descriptor.model.Shared;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.PlatformTypes;
import com.reedelk.runtime.api.message.content.MimeType;
import io.github.classgraph.*;

import java.util.*;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;
import static com.reedelk.runtime.api.commons.StringUtils.EMPTY;
import static com.reedelk.runtime.api.commons.StringUtils.isNotBlank;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class PropertyTypeAnalyzer implements FieldInfoAnalyzer {

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
            return processKnownType(clazz, fieldInfo);

        } else {
            // We check that it is a user defined object type (with Implementor).
            // We check that we can resolve class info. If we can, then ..
            ClassInfo classInfo = context.getClassInfo(fullyQualifiedClassName);
            if (classInfo == null) {
                throw new UnsupportedType(typeSignature.getClass());
            }
            Shared shared = isShareable(classInfo);
            Collapsible collapsible = isCollapsible(classInfo);
            ComponentPropertyAnalyzer propertyAnalyzer = new ComponentPropertyAnalyzer(context);

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

    private TypeDescriptor processKnownType(Class<?> clazz, FieldInfo fieldInfo) {
        if (isDynamicValue(clazz)) {
            TypeDynamicValueDescriptor descriptor = new TypeDynamicValueDescriptor();
            descriptor.setType(clazz);
            return descriptor;

        } else if (isDynamicMap(clazz)) {
            String tabGroup = annotationValueOrDefaultFrom(fieldInfo, TabGroup.class, null);
            TabPlacement tabPlacement = tabPlacementOf(fieldInfo);
            TypeDynamicMapDescriptor descriptor = new TypeDynamicMapDescriptor();
            descriptor.setType(clazz);
            descriptor.setTabGroup(tabGroup);
            descriptor.setTabPlacement(tabPlacement);
            return descriptor;

        } else if (isScript(clazz)) {
            return new TypeScriptDescriptor();

        } else if (isPassword(fieldInfo, clazz)) {
            return new TypePasswordDescriptor();

        } else if (isResourceText(clazz)) {
            return new TypeResourceTextDescriptor();

        } else if (isResourceBinary(clazz)) {
            return new TypeResourceBinaryDescriptor();

        } else if (isCombo(fieldInfo, clazz)) {
            boolean editable = annotationParameterValueOrDefaultFrom(fieldInfo, Combo.class, "editable", false);
            Object[] comboValues = annotationParameterValueOrDefaultFrom(fieldInfo, Combo.class, "comboValues", new String[]{});
            String prototype = annotationParameterValueOrDefaultFrom(fieldInfo, Combo.class, "prototype", null);
            String[] items = stream(comboValues).map(value -> (String) value).toArray(String[]::new);
            TypeComboDescriptor descriptor = new TypeComboDescriptor();
            descriptor.setEditable(editable);
            descriptor.setComboValues(items);
            descriptor.setPrototype(prototype);
            return descriptor;

        } else if (isMimeTypeCombo(fieldInfo, clazz)) {
            List<String> predefinedMimeTypes = Arrays.asList(MimeType.ALL_MIME_TYPES);
            String additionalMimeTypes = annotationParameterValueOrDefaultFrom(fieldInfo, MimeTypeCombo.class, "additionalTypes", EMPTY);
            if (isNotBlank(additionalMimeTypes)) {
                String[] additionalTypes = additionalMimeTypes.split(",");
                predefinedMimeTypes = new ArrayList<>(predefinedMimeTypes);
                predefinedMimeTypes.addAll(Arrays.asList(additionalTypes));
            }
            String[] comboMimeTypesArray = predefinedMimeTypes.toArray(new String[]{});
            TypeComboDescriptor descriptor = new TypeComboDescriptor();
            descriptor.setEditable(true);
            descriptor.setComboValues(comboMimeTypesArray);
            descriptor.setPrototype(MimeType.MIME_TYPE_PROTOTYPE);
            return descriptor;

        } else if (isMap(clazz)) {
            String tabGroup = annotationValueOrDefaultFrom(fieldInfo, TabGroup.class, null);
            TabPlacement tabPlacement = tabPlacementOf(fieldInfo);
            TypeMapDescriptor descriptor = new TypeMapDescriptor();
            descriptor.setTabGroup(tabGroup);
            descriptor.setTabPlacement(tabPlacement);
            return descriptor;

        } else {
            TypePrimitiveDescriptor descriptor = new TypePrimitiveDescriptor();
            descriptor.setType(clazz);
            return descriptor;
        }
    }
}
