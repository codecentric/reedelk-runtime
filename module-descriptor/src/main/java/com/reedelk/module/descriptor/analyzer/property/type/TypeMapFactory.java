package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.TypeDescriptor;
import com.reedelk.module.descriptor.model.TypeMapDescriptor;
import com.reedelk.runtime.api.annotation.KeyName;
import com.reedelk.runtime.api.annotation.TabGroup;
import com.reedelk.runtime.api.annotation.ValueName;
import com.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.ClassRefTypeSignature;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.TypeArgument;

import java.util.List;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class TypeMapFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName)
                .map(clazz -> PlatformTypes.isSupported(fullyQualifiedClassName) &&
                isMap(clazz)).orElse(false);
    }

    @Override
    public TypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        String keyName = annotationValueOrDefaultFrom(fieldInfo, KeyName.class, null);
        String tabGroup = annotationValueOrDefaultFrom(fieldInfo, TabGroup.class, null);
        String valueName = annotationValueOrDefaultFrom(fieldInfo, ValueName.class, null);

        // The second type is the map value types. We must find out the value type of the Map.
        // The Value type could be a primitive type or a custom object type.

        ClassRefTypeSignature classRefTypeSignature = (ClassRefTypeSignature) fieldInfo.getTypeSignature();
        List<TypeArgument> typeArguments = classRefTypeSignature.getTypeArguments();

        TypeArgument typeArgument = typeArguments.get(1);
        String valueTypeFullyQualifiedName = typeArgument.toString();
        TypeDescriptorFactory factory = TypeDescriptorFactoryProvider.from(valueTypeFullyQualifiedName, fieldInfo, context);
        TypeDescriptor valueType = factory.create(valueTypeFullyQualifiedName, fieldInfo, context);

        TypeMapDescriptor descriptor = new TypeMapDescriptor();
        descriptor.setValueType(valueType);
        descriptor.setValueName(valueName);
        descriptor.setTabGroup(tabGroup);
        descriptor.setKeyName(keyName);
        return descriptor;
    }
}
