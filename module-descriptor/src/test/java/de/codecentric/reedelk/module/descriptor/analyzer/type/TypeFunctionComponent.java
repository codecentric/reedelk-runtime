package de.codecentric.reedelk.module.descriptor.analyzer.type;

import de.codecentric.reedelk.runtime.api.annotation.TypeFunction;

@TypeFunction(name = "method0", signature = "method0(String value1)", returnType = String.class)
@TypeFunction(name = "method1", signature = "method1(String value1, int value2)", returnType = byte[].class, cursorOffset = 1, description = "My description", example = "method1('test', 2)")
public class TypeFunctionComponent {

    // With empty arguments
    @TypeFunction(description = "Returns the attributes")
    public String method2() {
        return "My attributes";
    }

    // With one argument
    @TypeFunction
    public boolean method3(String value1) {
        return true;
    }

    // With multiple arguments
    @TypeFunction(description = "My description")
    public int method4(String value1, int value2) {
        return 2;
    }

    // With example
    @TypeFunction(example = "TypeFunctionComponent.method5('test', 432)")
    public int method5(String value1, int value2) {
        return value2;
    }

    // With overridden signature and void return type
    @TypeFunction(signature = "info(String value1, int value2)", description = "Logs a message with INFO level")
    public void method6(String value1) {
        // nothing
    }

    // With cursor offset and complex return type
    @TypeFunction(cursorOffset = 2)
    public TypeFunctionComponent method7(String value1) {
        // nothing
        return this;
    }

    // With byte array return type.
    @TypeFunction
    public byte[] method8(long value1) {
        // nothing
        return new byte[] {2, 3};
    }
}
