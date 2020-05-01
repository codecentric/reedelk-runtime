package com.reedelk.module.descriptor.fixture;

import com.reedelk.runtime.api.annotation.ComponentInput;
import com.reedelk.runtime.api.annotation.ComponentOutput;

@ComponentInput(payload = {String.class, byte[].class}, description = "My input description")
@ComponentOutput(attributes = MyAttributes.class, payload = {long.class, Byte[].class}, description = "My output description")
public class ClassWithComponentInputAndOutputAnnotation {
}
