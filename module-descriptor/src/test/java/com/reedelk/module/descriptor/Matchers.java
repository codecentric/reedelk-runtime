package com.reedelk.module.descriptor;

import com.reedelk.module.descriptor.analyzer.Matcher;
import com.reedelk.module.descriptor.model.type.TypeFunctionDescriptor;

import java.util.Objects;

public class Matchers {

    public static Matcher<TypeFunctionDescriptor> ofTypeFunction(TypeFunctionDescriptor expected) {
        return new Matcher<TypeFunctionDescriptor>() {
            @Override
            public boolean matches(TypeFunctionDescriptor actual) {
                // TODO: Use reflection equals. with assertJ.
                // TODO: Continue here.
                String expectedDescription = expected.getDescription();
                String actualDescription = actual.getDescription();
                return Objects.equals(expectedDescription, actualDescription);
            }
        };
    }
}
