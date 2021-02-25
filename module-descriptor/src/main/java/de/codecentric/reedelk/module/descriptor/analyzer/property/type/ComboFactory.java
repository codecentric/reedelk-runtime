package de.codecentric.reedelk.module.descriptor.analyzer.property.type;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.model.property.ComboDescriptor;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.Combo;
import de.codecentric.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.FieldInfo;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;
import static java.util.Arrays.stream;

public class ComboFactory implements DescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName)
                .map(clazz -> PlatformTypes.isSupported(fullyQualifiedClassName) &&
                        isCombo(fieldInfo, clazz)).orElse(false);
    }

    @Override
    public PropertyTypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        boolean editable = annotationParameterValueFrom(fieldInfo, Combo.class, "editable", false);
        Object[] comboValues = annotationParameterValueFrom(fieldInfo, Combo.class, "comboValues", new String[]{});
        String prototype = annotationParameterValueFrom(fieldInfo, Combo.class, "prototype", null);
        String[] items = stream(comboValues).map(value -> (String) value).toArray(String[]::new);

        ComboDescriptor descriptor = new ComboDescriptor();
        descriptor.setEditable(editable);
        descriptor.setComboValues(items);
        descriptor.setPrototype(prototype);
        return descriptor;
    }
}
