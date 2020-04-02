package com.reedelk.runtime.converter.types.defaulttype;

import com.reedelk.runtime.converter.types.ValueConverter;

class AsByteArray implements ValueConverter<Object,byte[]> {

    /**
     * IMPORTANT: An object is first converted to a string before getting its bytes.
     * This serializer could serialize the Java object, however,
     * in the majority of cases we can't do that much with a serialized Java object.
     * For the user it is much better to get a string representation of the object
     * rather than its serialized bytes. If in the future it will be required
     * sending a serialized Java object a component can be provided to perform
     * such conversion.
     */
    @Override
    public byte[] from(Object value) {
        return value == null ? new byte[0] :
                value.toString().getBytes();
    }
}
