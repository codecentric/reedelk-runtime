package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import com.reedelk.module.descriptor.model.property.ResourceTextDescriptor;
import com.reedelk.runtime.api.annotation.HintBrowseFile;
import com.reedelk.runtime.api.annotation.WidthAuto;
import com.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.FieldInfo;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class ResourceTextFactory implements DescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName)
                .map(clazz -> PlatformTypes.isSupported(fullyQualifiedClassName) && isResourceText(clazz))
                .orElse(false);
    }

    @Override
    public PropertyTypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        boolean widthAuto = hasAnnotation(fieldInfo, WidthAuto.class);
        String hintBrowseFile = annotationValueFrom(fieldInfo, HintBrowseFile.class, null);

        ResourceTextDescriptor descriptor = new ResourceTextDescriptor();
        descriptor.setHintBrowseFile(hintBrowseFile);
        if (widthAuto) descriptor.setWidthAuto(Boolean.TRUE);
        return descriptor;
    }
}
