package de.codecentric.reedelk.module.descriptor.analyzer.property.type;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.model.property.EnumDescriptor;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.DisplayName;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.FieldInfoList;

import java.util.Map;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;
import static java.util.stream.Collectors.toMap;

public class EnumFactory implements DescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return isEnumeration(fullyQualifiedClassName, context);
    }

    @Override
    public PropertyTypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        ClassInfo enumClassInfo = context.getClassInfo(fullyQualifiedClassName);
        FieldInfoList declaredFieldInfo = enumClassInfo.getDeclaredFieldInfo();

        Map<String, String> nameAndDisplayName = declaredFieldInfo
                .stream()
                .filter(filterByFullyQualifiedClassNameType(fullyQualifiedClassName))
                .collect(toMap(FieldInfo::getName, field ->
                        annotationValueFrom(field, DisplayName.class, field.getName())));

        EnumDescriptor descriptor = new EnumDescriptor();
        descriptor.setNameAndDisplayNameMap(nameAndDisplayName);
        return descriptor;
    }
}
