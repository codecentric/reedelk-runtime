package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.TypeComboDescriptor;
import com.reedelk.module.descriptor.model.TypeDescriptor;
import com.reedelk.runtime.api.annotation.Combo;
import com.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.FieldInfo;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;
import static java.util.Arrays.stream;

public class TypeComboFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName)
                .map(clazz -> PlatformTypes.isSupported(fullyQualifiedClassName) &&
                        isCombo(fieldInfo, clazz)).orElse(false);
    }

    @Override
    public TypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        boolean editable = annotationParameterValueOrDefaultFrom(fieldInfo, Combo.class, "editable", false);
        Object[] comboValues = annotationParameterValueOrDefaultFrom(fieldInfo, Combo.class, "comboValues", new String[]{});
        String prototype = annotationParameterValueOrDefaultFrom(fieldInfo, Combo.class, "prototype", null);
        String[] items = stream(comboValues).map(value -> (String) value).toArray(String[]::new);

        TypeComboDescriptor descriptor = new TypeComboDescriptor();
        descriptor.setEditable(editable);
        descriptor.setComboValues(items);
        descriptor.setPrototype(prototype);
        return descriptor;
    }
}
