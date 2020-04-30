package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import com.reedelk.module.descriptor.model.property.TypeListDescriptor;
import com.reedelk.runtime.api.annotation.DialogTitle;
import com.reedelk.runtime.api.annotation.ListDisplayProperty;
import com.reedelk.runtime.api.annotation.TabGroup;
import com.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.ClassRefTypeSignature;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.TypeArgument;

import java.util.List;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class TypeListFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName)
                .map(clazz -> PlatformTypes.isSupported(fullyQualifiedClassName) &&
                        isList(clazz)).orElse(false);
    }

    @Override
    public PropertyTypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        String tabGroup = annotationValueOrDefaultFrom(fieldInfo, TabGroup.class, null);
        String dialogTitle = annotationValueOrDefaultFrom(fieldInfo, DialogTitle.class, null);
        String listDisplayProperty = annotationValueOrDefaultFrom(fieldInfo, ListDisplayProperty.class, null);

        // We must find out the value type of the List.
        // The Value type could be a primitive type or a custom object type.

        ClassRefTypeSignature classRefTypeSignature = (ClassRefTypeSignature) fieldInfo.getTypeSignature();
        List<TypeArgument> typeArguments = classRefTypeSignature.getTypeArguments();

        TypeArgument typeArgument = typeArguments.get(0);
        String valueTypeFullyQualifiedName = typeArgument.toString();
        TypeDescriptorFactory factory = TypeDescriptorFactoryProvider.from(valueTypeFullyQualifiedName, fieldInfo, context);
        PropertyTypeDescriptor valueType = factory.create(valueTypeFullyQualifiedName, fieldInfo, context);

        TypeListDescriptor descriptor = new TypeListDescriptor();
        descriptor.setListDisplayProperty(listDisplayProperty);
        descriptor.setDialogTitle(dialogTitle);
        descriptor.setValueType(valueType);
        descriptor.setTabGroup(tabGroup);
        return descriptor;
    }
}
