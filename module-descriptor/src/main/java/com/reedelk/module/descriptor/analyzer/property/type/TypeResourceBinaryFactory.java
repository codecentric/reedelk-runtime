package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.TypeDescriptor;
import com.reedelk.module.descriptor.model.TypeResourceBinaryDescriptor;
import com.reedelk.runtime.api.annotation.HintBrowseFile;
import com.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.FieldInfo;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class TypeResourceBinaryFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName)
                .map(clazz -> PlatformTypes.isSupported(fullyQualifiedClassName) && isResourceBinary(clazz))
                .orElse(false);
    }

    @Override
    public TypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        String hintBrowseFile = annotationValueOrDefaultFrom(fieldInfo, HintBrowseFile.class, null);

        TypeResourceBinaryDescriptor descriptor = new TypeResourceBinaryDescriptor();
        descriptor.setHintBrowseFile(hintBrowseFile);
        return descriptor;
    }
}
