package de.codecentric.reedelk.module.descriptor.analyzer.commons;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.analyzer.component.UnsupportedType;
import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.resource.ResourceBinary;
import de.codecentric.reedelk.runtime.api.resource.ResourceText;
import de.codecentric.reedelk.runtime.api.script.Script;
import de.codecentric.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicValue;
import io.github.classgraph.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public static boolean hasAnnotation(ClassInfo classInfo, Class<?> clazz) {
        return classInfo.hasAnnotation(clazz.getName());
    }

    public static boolean isVisibleProperty(FieldInfo fieldInfo) {
        return hasAnnotation(fieldInfo, Property.class) &&
                !hasAnnotation(fieldInfo, Hidden.class);
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

    public static <T> T annotationValueFrom(FieldInfo fieldInfo, Class<?> annotationClazz, T defaultValue) {
        if (!fieldInfo.hasAnnotation(annotationClazz.getName())) {
            return defaultValue;
        }
        AnnotationInfo annotationInfo = fieldInfo.getAnnotationInfo(annotationClazz.getName());
        return annotationValueFrom(annotationInfo, defaultValue);
    }

    public static <T> T annotationValueFrom(ClassInfo classInfo, Class<?> annotationClazz, T defaultValue) {
        if (!classInfo.hasAnnotation(annotationClazz.getName())) {
            return defaultValue;
        }
        AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(annotationClazz.getName());
        return annotationValueFrom(annotationInfo, defaultValue);
    }

    public static <T> T annotationParameterValueFrom(FieldInfo fieldInfo, Class<?> annotationClazz, String annotationParamName, T defaultValue) {
        if (!fieldInfo.hasAnnotation(annotationClazz.getName())) {
            return defaultValue;
        }
        AnnotationInfo annotationInfo = fieldInfo.getAnnotationInfo(annotationClazz.getName());
        return parameterValueFrom(annotationInfo, annotationParamName, defaultValue);
    }

    public static <T> T annotationParameterValueFrom(ClassInfo classInfo, Class<?> annotationClazz, String annotationParamName, T defaultValue) {
        if (!classInfo.hasAnnotation(annotationClazz.getName())) {
            return defaultValue;
        }
        AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(annotationClazz.getName());
        return parameterValueFrom(annotationInfo, annotationParamName, defaultValue);
    }

    public static Class<?> clazzByFullyQualifiedNameOrThrow(String fullyQualifiedClassName) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName)
                .orElseThrow(() -> new UnsupportedType(fullyQualifiedClassName));
    }

    @SuppressWarnings("unchecked")
    public static <T> T parameterValueFrom(AnnotationInfo annotationInfo, String paramName, T defaultValue) {
        Object parameterValue = parameterValueFrom(annotationInfo, paramName);
        if (parameterValue instanceof AnnotationEnumValue) {
            return (T) ((AnnotationEnumValue) parameterValue).loadClassAndReturnEnumValue();
        } else if (parameterValue instanceof AnnotationClassRef) {
            AnnotationClassRef classRef = ((AnnotationClassRef) parameterValue);
            return (T) classRef.getName();
        } else {
            return parameterValue == null ? defaultValue : (T) parameterValue;
        }
    }

    public static List<AnnotationInfo> repeatableAnnotation(ClassInfo classInfo, Class<?> singleClazz, Class<?> repeatableClass) {
        List<AnnotationInfo> annotationInfos = new ArrayList<>();
        for (AnnotationInfo info : classInfo.getAnnotationInfo()) {
            if (info.getName().equals(singleClazz.getName())) {
                annotationInfos.add(info);
            }
        }
        if (ScannerUtils.hasAnnotation(classInfo, repeatableClass)) {
            AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(repeatableClass.getName());
            if (annotationInfo != null) {
                AnnotationParameterValueList annotationsList = annotationInfo.getParameterValues();
                Object[] annotationInfosObjects = (Object[]) annotationsList.get(0).getValue();
                for (Object info : annotationInfosObjects) {
                    AnnotationInfo casted = (AnnotationInfo) info;
                    annotationInfos.add(casted);
                }
            }
        }
        return annotationInfos;
    }

    public static List<AnnotationInfo> repeatableAnnotation(FieldInfo fieldInfo, Class<?> singleClazz, Class<?> repeatableClass) {
        List<AnnotationInfo> annotationInfos = new ArrayList<>();
        for (AnnotationInfo info : fieldInfo.getAnnotationInfo()) {
            if (info.getName().equals(singleClazz.getName())) {
                annotationInfos.add(info);
            }
        }
        if (ScannerUtils.hasAnnotation(fieldInfo, repeatableClass)) {
            AnnotationInfo annotationInfo = fieldInfo.getAnnotationInfo(repeatableClass.getName());
            if (annotationInfo != null) {
                AnnotationParameterValueList annotationsList = annotationInfo.getParameterValues();
                Object[] annotationInfosObjects = (Object[]) annotationsList.get(0).getValue();
                for (Object info : annotationInfosObjects) {
                    AnnotationInfo casted = (AnnotationInfo) info;
                    annotationInfos.add(casted);
                }
            }
        }
        return annotationInfos;
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

    public static boolean isList(Class<?> clazz) {
        return List.class.equals(clazz);
    }

    public static boolean isScript(Class<?> clazz) {
        return Script.class.equals(clazz);
    }

    public static de.codecentric.reedelk.module.descriptor.model.property.Shared isShareable(ClassInfo classInfo) {
        return classInfo.hasAnnotation(Shared.class.getName()) ?
                de.codecentric.reedelk.module.descriptor.model.property.Shared.YES : de.codecentric.reedelk.module.descriptor.model.property.Shared.NO;
    }

    public static de.codecentric.reedelk.module.descriptor.model.property.Collapsible isCollapsible(ClassInfo classInfo) {
        return classInfo.hasAnnotation(Collapsible.class.getName()) ?
                de.codecentric.reedelk.module.descriptor.model.property.Collapsible.YES : de.codecentric.reedelk.module.descriptor.model.property.Collapsible.NO;
    }

    public static boolean isHidden(ClassInfo classInfo) {
        return classInfo.hasAnnotation(Hidden.class.getName());
    }

    public static Optional<Class<?>> clazzByFullyQualifiedName(String fullyQualifiedClassName) {
        try {
            return Optional.of(Class.forName(fullyQualifiedClassName));
        } catch (ClassNotFoundException e) {
            // The class could not be found, this happens when the fully qualified name
            // refer to a class loaded by the OSGi environment. These classes are typically
            // implementing the @Implementor interface.
            return Optional.empty();
        }
    }

    private static Object parameterValueFrom(AnnotationInfo info, String parameterName) {
        AnnotationParameterValueList parameterValues = info.getParameterValues();
        AnnotationParameterValue parameterValue = parameterValues.get(parameterName);
        return parameterValue == null ? parameterValue : parameterValue.getValue();
    }

    @SuppressWarnings("unchecked")
    private static <T> T annotationValueFrom(AnnotationInfo annotationInfo, T defaultValue) {
        AnnotationParameterValueList parameterValues = annotationInfo.getParameterValues();
        if (parameterValues == null) return defaultValue;
        return parameterValues.get(ANNOTATION_DEFAULT_PARAM_NAME) == null ?
                defaultValue :
                (T) parameterValues.getValue(ANNOTATION_DEFAULT_PARAM_NAME);
    }
}
