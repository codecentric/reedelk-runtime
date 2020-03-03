package com.reedelk.runtime.converter;

import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.converter.types.ValueConverter;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;

@SuppressWarnings("unchecked")
public class RuntimeConverters {

    // TODO: Test all for to object.
    private static final RuntimeConverters INSTANCE = new RuntimeConverters();

    private static class ConvertersHelper {
        private static final Map<Class<?>, ValueConverter<?,?>> DEFAULT =
                Collections.unmodifiableMap(com.reedelk.runtime.converter.types.defaulttype.FromDefaultConverters.ALL);

        private static final Map<Class<?>, Map<Class<?>, ValueConverter<?, ?>>> CONVERTERS;
        static {
            Map<Class<?>, Map<Class<?>, ValueConverter<?, ?>>> tmp = new HashMap<>();
            tmp.put(Float.class, com.reedelk.runtime.converter.types.floattype.FromFloatConverters.ALL);
            tmp.put(Double.class, com.reedelk.runtime.converter.types.doubletype.FromDoubleConverters.ALL);
            tmp.put(String.class, com.reedelk.runtime.converter.types.stringtype.FromStringConverters.ALL);
            tmp.put(Integer.class, com.reedelk.runtime.converter.types.integertype.FromIntegerConverters.ALL);
            tmp.put(Boolean.class, com.reedelk.runtime.converter.types.booleantype.FromBooleanConverters.ALL);
            tmp.put(byte[].class, com.reedelk.runtime.converter.types.bytearraytype.FromByteArrayConverters.ALL);
            tmp.put(Byte[].class, com.reedelk.runtime.converter.types.bytearraytype.FromByteArrayConverters.ALL);
            tmp.put(Exception.class, com.reedelk.runtime.converter.types.exceptiontype.FromExceptionConverters.ALL);
            tmp.put(ScriptObjectMirror.class, com.reedelk.runtime.converter.types.scriptobjectmirrortype.FromScriptObjectMirrorConverters.ALL);
            CONVERTERS = Collections.unmodifiableMap(tmp);
        }
    }

    private RuntimeConverters() {
    }

    public static RuntimeConverters getInstance() {
        return INSTANCE;
    }

    static Map<Class<?>, Map<Class<?>, ValueConverter<?, ?>>> converters() {
        return ConvertersHelper.CONVERTERS;
    }

    static Map<Class<?>, ValueConverter<?,?>> defaults() {
        return ConvertersHelper.DEFAULT;
    }

    public <O> O convert(Object input, Class<O> outputClass) {
        if (input == null) {
            return null;
        } else {
            return convert(input, input.getClass(), outputClass);
        }
    }

    private <I, O> O convert(Object input, Class<I> inputClass, Class<O> outputClass) {
        if (input == null) {
            return null;
        } else if (inputClass.equals(outputClass)) {
            return (O) input;
        } else if (input instanceof Exception) {
            return convertType(input, Exception.class, outputClass);
        } else {
            return convertType(input, inputClass, outputClass);
        }
    }

    private <I, O> O convertType(I input, Class<?> inputClass, Class<O> outputClass) {
        Map<Class<?>, ValueConverter<?, ?>> fromInputConverters = converters().getOrDefault(inputClass, defaults());
        return Optional.of(fromInputConverters)
                .flatMap(fromConverter -> Optional.ofNullable((ValueConverter<I, O>) fromConverter.get(outputClass)))
                .map(toConverter -> toConverter.from(input))
                .orElseThrow(converterNotFound(inputClass, outputClass));
    }

     static Supplier<? extends ESBException> converterNotFound(Class<?> inputClazz, Class<?> outputClazz) {
        return () -> new ESBException(format("Converter for input=[%s] to output=[%s] not available", inputClazz.getName(), outputClazz.getName()));
    }
}
