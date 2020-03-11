package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.model.TypeDescriptor;
import com.reedelk.module.descriptor.model.TypeScriptDescriptor;
import io.github.classgraph.FieldInfo;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.isScript;

public class TypeScriptFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(Class<?> clazz, FieldInfo fieldInfo) {
        return isScript(clazz);
    }

    @Override
    public TypeDescriptor create(Class<?> clazz, FieldInfo fieldInfo) {
        return new TypeScriptDescriptor();
    }
}
