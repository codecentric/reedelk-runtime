package de.codecentric.reedelk.module.descriptor.analyzer.type;

import de.codecentric.reedelk.module.descriptor.ModuleDescriptorException;
import de.codecentric.reedelk.module.descriptor.model.type.TypeFunctionDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.TypeFunction;
import de.codecentric.reedelk.runtime.api.annotation.TypeFunctions;
import de.codecentric.reedelk.runtime.api.annotation.UseDefaultType;
import io.github.classgraph.*;

import java.util.ArrayList;
import java.util.List;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.parameterValueFrom;
import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.repeatableAnnotation;
import static de.codecentric.reedelk.runtime.api.commons.StringUtils.EMPTY;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.stream.Collectors.toList;

// Only classes with @Type annotation are scanned for @TypeFunction annotations.
// This is needed in order to link a function definition to a type.
// The function definitions can be placed at class definition or on top of class methods.
// Function defined in the class definition are for example functions we want to expose
// from a base inherited class. See FlowContext as an example of this case.
public class TypeFunctionAnalyzer {

    private static final String AUTO_GENERATED_ARGUMENTS_PREFIX = "arg";

    private final ClassInfo classInfo;

    public TypeFunctionAnalyzer(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    public List<TypeFunctionDescriptor> analyze() {
        // We must return definitions for @TypeFunction annotations on top of the class name
        // and on top of each method in the class with @TypeFunction annotation on it.
        List<TypeFunctionDescriptor> classLevelTypeFunctions = classLevelTypeFunctions();
        List<TypeFunctionDescriptor> methodLevelTypeFunctions = methodLevelTypeFunctions();
        classLevelTypeFunctions.addAll(methodLevelTypeFunctions);
        return classLevelTypeFunctions;
    }

    private List<TypeFunctionDescriptor> classLevelTypeFunctions() {
        return repeatableAnnotation(classInfo, TypeFunction.class, TypeFunctions.class).stream().map(annotationInfo -> {
            String signature = parameterValueFrom(annotationInfo, "signature", TypeFunction.USE_DEFAULT_SIGNATURE);
            String name = parameterValueFrom(annotationInfo, "name", TypeFunction.USE_DEFAULT_NAME);
            String description = parameterValueFrom(annotationInfo, "description", EMPTY);
            String example = parameterValueFrom(annotationInfo, "example", EMPTY);
            int cursorOffset = parameterValueFrom(annotationInfo, "cursorOffset", 0);

            if (TypeFunction.USE_DEFAULT_NAME.equals(name)) {
                String error = format("Name property must be defined for class level @TypeFunction annotation (class: %s).", classInfo.getName());
                throw new ModuleDescriptorException(error);
            }

            if (TypeFunction.USE_DEFAULT_SIGNATURE.equals(signature)) {
                String error = format("Signature property must be defined for class level @TypeFunction annotation (class: %s).", classInfo.getName());
                throw new ModuleDescriptorException(error);
            }

            // Return type is mandatory for class level @TypeFunction definitions. From class methods we can
            // infer the return type from the method definition but not in this case; therefore an exception
            // is thrown if the user did not provide a return type class.
            String returnType = getReturnTypeFromOrThrowWhenDefault(annotationInfo);

            TypeFunctionDescriptor descriptor = new TypeFunctionDescriptor();
            descriptor.setCursorOffset(cursorOffset);
            descriptor.setDescription(description);
            descriptor.setReturnType(returnType);
            descriptor.setSignature(signature);
            descriptor.setExample(example);
            descriptor.setName(name);
            return descriptor;

        }).collect(toList());
    }

    private List<TypeFunctionDescriptor> methodLevelTypeFunctions() {
        return classInfo.getDeclaredMethodInfo()
                .filter(methodInfo -> methodInfo.hasAnnotation(TypeFunction.class.getName()))
                .stream()
                .map(methodInfo -> {
                    AnnotationInfo annotationInfo = methodInfo.getAnnotationInfo(TypeFunction.class.getName());
                    String name = parameterValueFrom(annotationInfo, "name", TypeFunction.USE_DEFAULT_NAME);
                    String description = parameterValueFrom(annotationInfo, "description", EMPTY);
                    String example = parameterValueFrom(annotationInfo, "example", EMPTY);
                    int cursorOffset = parameterValueFrom(annotationInfo, "cursorOffset", 0);

                    // The real name is the method name if not specified in the annotation args.
                    String realName = TypeFunction.USE_DEFAULT_NAME.equals(name) ? methodInfo.getName() : name;
                    // The method signature is inferred from the method definition if signature not specified in the annotation args.
                    String realSignature = getSignatureFrom(annotationInfo, methodInfo);
                    // The return type is inferred from the method definition if type not specified in the annotation args.
                    String realReturnType = getReturnTypeFrom(annotationInfo, methodInfo);

                    TypeFunctionDescriptor descriptor = new TypeFunctionDescriptor();
                    descriptor.setReturnType(realReturnType);
                    descriptor.setCursorOffset(cursorOffset);
                    descriptor.setDescription(description);
                    descriptor.setSignature(realSignature);
                    descriptor.setExample(example);
                    descriptor.setName(realName);
                    return descriptor;

                }).collect(toList());
    }

    private String getSignatureFrom(AnnotationInfo annotationInfo, MethodInfo methodInfo) {
        String signature = parameterValueFrom(annotationInfo, "signature", TypeFunction.USE_DEFAULT_SIGNATURE);
        if (TypeFunction.USE_DEFAULT_SIGNATURE.equals(signature)) {
            return createSignatureFrom(methodInfo);
        } else {
            return signature;
        }
    }

    private String getReturnTypeFromOrThrowWhenDefault(AnnotationInfo annotationInfo) {
        String returnType = parameterValueFrom(annotationInfo, "returnType", UseDefaultType.class.getName());
        if (UseDefaultType.class.getName().equals(returnType)) {
            throw new ModuleDescriptorException("Return type must be defined for class level @TypeFunction annotations.");
        } else {
            return returnType; // Fully qualified name.
        }
    }

    private String getReturnTypeFrom(AnnotationInfo annotationInfo, MethodInfo methodInfo) {
        String returnType = parameterValueFrom(annotationInfo, "returnType", UseDefaultType.class.getName());
        if (UseDefaultType.class.getName().equals(returnType)) {
            TypeSignature resultType = methodInfo.getTypeDescriptor().getResultType();
            return resultType.toString(); // Fully qualified name
        } else {
            return returnType; // Fully qualified name.
        }
    }

    // Generates signature of a method from the method information.
    // Output Example: myMethod(String arg0, int arg1)
    private String createSignatureFrom(MethodInfo methodInfo) {
        StringBuilder signature = new StringBuilder(methodInfo.getName()).append("(");
        MethodParameterInfo[] parameterInfo = methodInfo.getParameterInfo();
        int argCount = 0;
        List<String> args = new ArrayList<>();
        for (MethodParameterInfo info : parameterInfo) {
            String type = info.getTypeDescriptor().toStringWithSimpleNames();
            args.add(type + " " + AUTO_GENERATED_ARGUMENTS_PREFIX + argCount);
            argCount++;
        }
        signature.append(join(", ", args));
        signature.append(")");
        return signature.toString();
    }
}
