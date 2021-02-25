package de.codecentric.reedelk.module.descriptor.analyzer.property.type;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.model.property.ListDescriptor;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.DialogTitle;
import de.codecentric.reedelk.runtime.api.annotation.ListDisplayProperty;
import de.codecentric.reedelk.runtime.api.annotation.TabGroup;
import de.codecentric.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.ClassRefTypeSignature;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.TypeArgument;

import java.util.List;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class ListFactory implements DescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName)
                .map(clazz -> PlatformTypes.isSupported(fullyQualifiedClassName) &&
                        isList(clazz)).orElse(false);
    }

    @Override
    public PropertyTypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        String tabGroup = annotationValueFrom(fieldInfo, TabGroup.class, null);
        String dialogTitle = annotationValueFrom(fieldInfo, DialogTitle.class, null);
        String listDisplayProperty = annotationValueFrom(fieldInfo, ListDisplayProperty.class, null);

        // We must find out the value type of the List.
        // The Value type could be a primitive type or a custom object type.

        ClassRefTypeSignature classRefTypeSignature = (ClassRefTypeSignature) fieldInfo.getTypeSignature();
        List<TypeArgument> typeArguments = classRefTypeSignature.getTypeArguments();

        TypeArgument typeArgument = typeArguments.get(0);
        String valueTypeFullyQualifiedName = typeArgument.toString();
        DescriptorFactory factory = DescriptorFactoryProvider.from(valueTypeFullyQualifiedName, fieldInfo, context);
        PropertyTypeDescriptor valueType = factory.create(valueTypeFullyQualifiedName, fieldInfo, context);

        ListDescriptor descriptor = new ListDescriptor();
        descriptor.setListDisplayProperty(listDisplayProperty);
        descriptor.setDialogTitle(dialogTitle);
        descriptor.setValueType(valueType);
        descriptor.setTabGroup(tabGroup);
        return descriptor;
    }
}
