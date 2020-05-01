package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.property.ComboDescriptor;
import com.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import com.reedelk.runtime.api.annotation.MimeTypeCombo;
import com.reedelk.runtime.api.commons.PlatformTypes;
import com.reedelk.runtime.api.message.content.MimeType;
import io.github.classgraph.FieldInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;
import static com.reedelk.runtime.api.commons.StringUtils.EMPTY;
import static com.reedelk.runtime.api.commons.StringUtils.isNotBlank;

public class TypeMimeTypeComboFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName)
                .map(clazz -> PlatformTypes.isSupported(fullyQualifiedClassName) && isMimeTypeCombo(fieldInfo, clazz))
                .orElse(false);
    }

    @Override
    public PropertyTypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        List<String> predefinedMimeTypes = MimeType.ALL.stream().map(MimeType::toString).collect(Collectors.toList());
        String additionalMimeTypes = annotationParameterValueOrDefaultFrom(fieldInfo, MimeTypeCombo.class, "additionalTypes", EMPTY);
        if (isNotBlank(additionalMimeTypes)) {
            String[] additionalTypes = additionalMimeTypes.split(",");
            predefinedMimeTypes = new ArrayList<>(predefinedMimeTypes);
            predefinedMimeTypes.addAll(Arrays.asList(additionalTypes));
        }
        String[] comboMimeTypesArray = predefinedMimeTypes.toArray(new String[]{});

        ComboDescriptor descriptor = new ComboDescriptor();
        descriptor.setEditable(true);
        descriptor.setComboValues(comboMimeTypesArray);
        descriptor.setPrototype(MimeType.MIME_TYPE_PROTOTYPE);
        return descriptor;
    }
}
