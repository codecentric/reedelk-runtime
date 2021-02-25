package de.codecentric.reedelk.module.descriptor.model.property;

public class PrimitiveDescriptor implements PropertyTypeDescriptor {

    private transient Class<?> type;

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public void setType(Class<?> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PrimitiveDescriptor{" +
                "type=" + type +
                '}';
    }
}
