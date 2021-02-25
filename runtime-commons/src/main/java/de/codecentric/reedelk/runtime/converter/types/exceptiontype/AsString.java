package de.codecentric.reedelk.runtime.converter.types.exceptiontype;

import de.codecentric.reedelk.runtime.api.commons.StackTraceUtils;
import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsString implements ValueConverter<Exception,String> {

    @Override
    public String from(Exception value) {
        return StackTraceUtils.asString(value);
    }

}
