package com.reedelk.runtime.converter.types.exceptiontype;

import com.reedelk.runtime.api.commons.StackTraceUtils;
import com.reedelk.runtime.converter.types.ValueConverter;

class AsString implements ValueConverter<Exception,String> {

    @Override
    public String from(Exception value) {
        return StackTraceUtils.asString(value);
    }

}
