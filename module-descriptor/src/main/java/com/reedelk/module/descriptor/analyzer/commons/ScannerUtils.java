package com.reedelk.module.descriptor.analyzer.commons;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.analyzer.component.UnsupportedType;
import com.reedelk.module.descriptor.model.TabPlacement;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.resource.ResourceBinary;
import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.api.script.Script;
import com.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;
import io.github.classgraph.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;

public class ScannerUtils {

    private static final String ANNOTATION_DEFAULT_PARAM_NAME = "value";

    private ScannerUtils() {
    }

    public static boolean hasAnnotation(FieldInfo fieldInfo, Class<?> clazz) {
        return fieldInfo.hasAnnotation(clazz.getName());
    }

    public static boolean isVisibleProperty(FieldInfo fieldInfo) {
        return hasAnnotation(fieldInfo, Property.class) &&
                !hasAnnotation(fieldInfo, Hidden.class);
    }

    public static String stringParameterValueFrom(AnnotationInfo info, String parameterName) {
        AnnotationParameterValueList parameterValues = info.getParameterValues();
        AnnotationParameterValue parameterValue = parameterValues.get(parameterName);
        return parameterValue != null ? (String) parameterValue.getValue() : null;
    }

    public static String stringParameterValueFrom(AnnotationInfo info, String parameterName, String defaultValue) {
        AnnotationParameterValueList parameterValues = info.getParameterValues();
        AnnotationParameterValue parameterValue = parameterValues.get(parameterName);
        return parameterValue != null ? (String) parameterValue.getValue() : defaultValue;
    }

    public static List<String> stringListParameterValueFrom(AnnotationInfo info, String parameterName) {
        AnnotationParameterValueList parameterValues = info.getParameterValues();
        AnnotationParameterValue parameterValue = parameterValues.get(parameterName);
        if (parameterValue == null) return unmodifiableList(new ArrayList<>());
        Object[] array = (Object[]) parameterValue.getValue();
        return array == null ?
                unmodifiableList(new ArrayList<>()) :
                stream(array).map(o -> (String) o).collect(Collectors.toList());
    }

    public static boolean booleanParameterValueFrom(AnnotationInfo info, String parameterName, boolean defaultValue) {
        AnnotationParameterValueList parameterValues = info.getParameterValues();
        if (parameterValues == null) return defaultValue;
        AnnotationParameterValue parameterValue = parameterValues.get(parameterName);
        return parameterValue == null ? defaultValue : (boolean) parameterValue.getValue();
    }

    public static <T> T annotationValueOrDefaultFrom(FieldInfo fieldInfo, Class<?> annotationClazz, T defaultValue) {
        if (!fieldInfo.hasAnnotation(annotationClazz.getName())) {
            return defaultValue;
        }
        AnnotationInfo annotationInfo = fieldInfo.getAnnotationInfo(annotationClazz.getName());
        return annotationValueOrDefaultFrom(defaultValue, annotationInfo);
    }

    public static <T> T annotationValueOrDefaultFrom(ClassInfo classInfo, Class<?> annotationClazz, T defaultValue) {
        if (!classInfo.hasAnnotation(annotationClazz.getName())) {
            return defaultValue;
        }
        AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(annotationClazz.getName());
        return annotationValueOrDefaultFrom(defaultValue, annotationInfo);
    }

    public static <T> T annotationParameterValueOrDefaultFrom(FieldInfo fieldInfo, Class<?> annotationClazz, String annotationParamName, T defaultValue) {
        if (!fieldInfo.hasAnnotation(annotationClazz.getName())) {
            return defaultValue;
        }
        AnnotationInfo annotationInfo = fieldInfo.getAnnotationInfo(annotationClazz.getName());
        return getParameterValue(annotationParamName, defaultValue, annotationInfo);
    }

    /**
     * Returns a new Predicate which filters FieldInfo's having type the target
     * class name specified in the argument of this function.
     */
    public static Predicate<FieldInfo> filterByFullyQualifiedClassNameType(String targetFullyQualifiedClassName) {
        return fieldInfo -> {
            TypeSignature typeDescriptor = fieldInfo.getTypeDescriptor();
            if (typeDescriptor instanceof ClassRefTypeSignature) {
                ClassRefTypeSignature matchingClass = (ClassRefTypeSignature) typeDescriptor;
                return matchingClass.getFullyQualifiedClassName()
                        .equals(targetFullyQualifiedClassName);
            }
            return false;
        };
    }

    /**
     * Returns true  if the given class info is of type enumeration, false otherwise.
     */
    public static boolean isEnumeration(String fullyQualifiedClassName, ComponentAnalyzerContext context) {
        ClassInfo classInfo = context.getClassInfo(fullyQualifiedClassName);
        if (classInfo == null) return false;

        ClassInfoList superclasses = classInfo.getSuperclasses();
        if (superclasses == null) return false;

        return superclasses
                .stream()
                .anyMatch(info -> info.getName().equals(Enum.class.getName()));
    }

    public static boolean isDynamicValue(Class<?> clazz) {
        try {
            return DynamicValue.class.equals(clazz.getSuperclass());
        } catch (UnsupportedType exception) {
            return false;
        }
    }

    public static boolean isDynamicMap(Class<?> clazz) {
        try {
            return DynamicMap.class.equals(clazz.getSuperclass());
        } catch (UnsupportedType exception) {
            return false;
        }
    }

    public static boolean isCombo(FieldInfo fieldInfo, Class<?> clazz) {
        return hasAnnotation(fieldInfo, Combo.class) && String.class.equals(clazz);
    }

    public static boolean isMimeTypeCombo(FieldInfo fieldInfo, Class<?> clazz) {
        return hasAnnotation(fieldInfo, MimeTypeCombo.class) && String.class.equals(clazz);
    }

    public static boolean isPassword(FieldInfo fieldInfo, Class<?> clazz) {
        return hasAnnotation(fieldInfo, Password.class) && String.class.equals(clazz);
    }

    public static boolean isResourceText(Class<?> clazz) {
        return ResourceText.class.equals(clazz);
    }

    public static boolean isResourceBinary(Class<?> clazz) {
        return ResourceBinary.class.equals(clazz);
    }

    public static boolean isMap(Class<?> clazz) {
        return Map.class.equals(clazz);
    }

    public static boolean isScript(Class<?> clazz) {
        return Script.class.equals(clazz);
    }

    public static com.reedelk.module.descriptor.model.Shared isShareable(ClassInfo classInfo) {
        return classInfo.hasAnnotation(com.reedelk.runtime.api.annotation.Shared.class.getName()) ?
                com.reedelk.module.descriptor.model.Shared.YES : com.reedelk.module.descriptor.model.Shared.NO;
    }

    public static com.reedelk.module.descriptor.model.Collapsible isCollapsible(ClassInfo classInfo) {
        return classInfo.hasAnnotation(com.reedelk.runtime.api.annotation.Collapsible.class.getName()) ?
                com.reedelk.module.descriptor.model.Collapsible.YES : com.reedelk.module.descriptor.model.Collapsible.NO;
    }

    public static TabPlacement tabPlacementOf(FieldInfo fieldInfo) {
        return hasAnnotation(fieldInfo, TabPlacementTop.class) ?
                TabPlacement.TOP : TabPlacement.LEFT; // Left placement is the default value
    }

    public static boolean isHidden(ClassInfo classInfo) {
        return classInfo.hasAnnotation(Hidden.class.getName());
    }

    public static Class<?> clazzByFullyQualifiedName(String fullyQualifiedClassName) {
        try {
            return Class.forName(fullyQualifiedClassName);
        } catch (ClassNotFoundException e) {
            // if it is a known type, then the class must be resolvable.
            // Otherwise the @PropertyValueConverterFactory class would not even compile.
            throw new UnsupportedType(fullyQualifiedClassName);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getParameterValue(String annotationParamName, T defaultValue, AnnotationInfo annotationInfo) {
        Object parameterValue = getParameterValue(annotationInfo, annotationParamName);
        if (parameterValue instanceof AnnotationEnumValue) {
            return (T) ((AnnotationEnumValue) parameterValue).loadClassAndReturnEnumValue();
        }
        return parameterValue == null ? defaultValue : (T) parameterValue;
    }

    private static Object getParameterValue(AnnotationInfo info, String parameterName) {
        AnnotationParameterValueList parameterValues = info.getParameterValues();
        AnnotationParameterValue parameterValue = parameterValues.get(parameterName);
        return parameterValue == null ? parameterValue : parameterValue.getValue();
    }

    @SuppressWarnings("unchecked")
    private static <T> T annotationValueOrDefaultFrom(T defaultValue, AnnotationInfo annotationInfo) {
        AnnotationParameterValueList parameterValues = annotationInfo.getParameterValues();
        if (parameterValues == null) return defaultValue;
        return parameterValues.get(ANNOTATION_DEFAULT_PARAM_NAME) == null ?
                defaultValue :
                (T) parameterValues.getValue(ANNOTATION_DEFAULT_PARAM_NAME);
    }
}
