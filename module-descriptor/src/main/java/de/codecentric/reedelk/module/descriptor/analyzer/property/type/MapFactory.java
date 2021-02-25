package de.codecentric.reedelk.module.descriptor.analyzer.property.type;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.model.property.MapDescriptor;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.DialogTitle;
import de.codecentric.reedelk.runtime.api.annotation.KeyName;
import de.codecentric.reedelk.runtime.api.annotation.TabGroup;
import de.codecentric.reedelk.runtime.api.annotation.ValueName;
import de.codecentric.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.ClassRefTypeSignature;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.TypeArgument;

import java.util.List;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class MapFactory implements DescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName)
                .map(clazz -> PlatformTypes.isSupported(fullyQualifiedClassName) &&
                isMap(clazz)).orElse(false);
    }

    @Override
    public PropertyTypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        String keyName = annotationValueFrom(fieldInfo, KeyName.class, null);
        String tabGroup = annotationValueFrom(fieldInfo, TabGroup.class, null);
        String valueName = annotationValueFrom(fieldInfo, ValueName.class, null);
        String dialogTitle = annotationValueFrom(fieldInfo, DialogTitle.class, null);

        // The second type is the map value types. We must find out the value type of the Map.
        // The Value type could be a primitive type or a custom object type.

        ClassRefTypeSignature classRefTypeSignature = (ClassRefTypeSignature) fieldInfo.getTypeSignature();
        List<TypeArgument> typeArguments = classRefTypeSignature.getTypeArguments();

        TypeArgument typeArgument = typeArguments.get(1);
        String valueTypeFullyQualifiedName = typeArgument.toString();
        DescriptorFactory factory = DescriptorFactoryProvider.from(valueTypeFullyQualifiedName, fieldInfo, context);
        PropertyTypeDescriptor valueType = factory.create(valueTypeFullyQualifiedName, fieldInfo, context);

        MapDescriptor descriptor = new MapDescriptor();
        descriptor.setDialogTitle(dialogTitle);
        descriptor.setValueType(valueType);
        descriptor.setValueName(valueName);
        descriptor.setTabGroup(tabGroup);
        descriptor.setKeyName(keyName);
        return descriptor;
    }
}
