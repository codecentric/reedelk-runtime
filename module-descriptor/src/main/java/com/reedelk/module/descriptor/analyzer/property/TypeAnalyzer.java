package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.analyzer.component.UnsupportedType;
import com.reedelk.module.descriptor.analyzer.property.type.TypeDescriptorFactory;
import com.reedelk.module.descriptor.analyzer.property.type.TypeDescriptorFactoryProvider;
import com.reedelk.module.descriptor.model.property.PrimitiveDescriptor;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import com.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import io.github.classgraph.BaseTypeSignature;
import io.github.classgraph.ClassRefTypeSignature;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.TypeSignature;

public class TypeAnalyzer implements FieldInfoAnalyzer {

    @Override
    public void handle(FieldInfo fieldInfo, PropertyDescriptor.Builder builder, ComponentAnalyzerContext context) {
        TypeSignature typeSignature = fieldInfo.getTypeDescriptor();

        // Primitive
        if (typeSignature instanceof BaseTypeSignature) {
            Class<?> clazz = ((BaseTypeSignature) typeSignature).getType();
            PrimitiveDescriptor typeDescriptor = new PrimitiveDescriptor();
            typeDescriptor.setType(clazz);
            builder.type(typeDescriptor);

            // Non primitive: String, BigDecimal, DynamicString or custom types...
        } else if (typeSignature instanceof ClassRefTypeSignature) {
            ClassRefTypeSignature classRef = (ClassRefTypeSignature) typeSignature;
            String fullyQualifiedClassName = classRef.getFullyQualifiedClassName();
            TypeDescriptorFactory typeDescriptorFactory =
                    TypeDescriptorFactoryProvider.from(fullyQualifiedClassName, fieldInfo, context);
            PropertyTypeDescriptor typeDescriptor = typeDescriptorFactory.create(fullyQualifiedClassName, fieldInfo, context);
            builder.type(typeDescriptor);

        } else {
            throw new UnsupportedType(typeSignature.getClass());
        }
    }
}
