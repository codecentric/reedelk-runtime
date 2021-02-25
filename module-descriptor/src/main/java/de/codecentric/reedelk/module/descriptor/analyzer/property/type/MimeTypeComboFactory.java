package de.codecentric.reedelk.module.descriptor.analyzer.property.type;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.model.property.ComboDescriptor;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.MimeTypeCombo;
import de.codecentric.reedelk.runtime.api.commons.PlatformTypes;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import io.github.classgraph.FieldInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class MimeTypeComboFactory implements DescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName)
                .map(clazz -> PlatformTypes.isSupported(fullyQualifiedClassName) && isMimeTypeCombo(fieldInfo, clazz))
                .orElse(false);
    }

    @Override
    public PropertyTypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        List<String> predefinedMimeTypes = MimeType.ALL.stream().map(MimeType::toString).collect(Collectors.toList());
        String additionalMimeTypes = annotationParameterValueFrom(fieldInfo, MimeTypeCombo.class, "additionalTypes", StringUtils.EMPTY);
        if (StringUtils.isNotBlank(additionalMimeTypes)) {
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
