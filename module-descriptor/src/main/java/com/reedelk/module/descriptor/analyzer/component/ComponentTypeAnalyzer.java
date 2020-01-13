package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.model.ComponentType;
import com.reedelk.runtime.api.component.*;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;

class ComponentTypeAnalyzer {

    private final ClassInfo classInfo;

    ComponentTypeAnalyzer(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    ComponentType analyze() {
        if (isInbound(classInfo)) {
            return ComponentType.INBOUND;
        } else if (isProcessor(classInfo)) {
            return ComponentType.PROCESSOR;
        } else if (isComponent(classInfo)) {
            return ComponentType.COMPONENT;
        } else if (isJoin(classInfo)) {
            return ComponentType.JOIN;
        } else {
            return ComponentType.UNKNOWN;
        }
    }

    /**
     * A component is inbound if it implements either the AbstractInbound abstract class
     * or Inbound interface.
     *
     * @param componentClassInfo the class info descriptor.
     * @return true if this class descriptor describes an Inbound component, false otherwise.
     */
    private boolean isInbound(ClassInfo componentClassInfo) {
        ClassInfoList superclasses = componentClassInfo.getSuperclasses();
        boolean implementsAbstractInbound = superclasses.stream().anyMatch(superClassInfo ->
                superClassInfo.getName().equals(AbstractInbound.class.getName()));
        boolean implementsInboundInterface = componentClassInfo.getInterfaces().stream().anyMatch(interfaceClassInfo ->
                interfaceClassInfo.getName().equals(Inbound.class.getName()));
        return implementsAbstractInbound || implementsInboundInterface;
    }

    /**
     * A component is a processor if it implements the ProcessorSync or ProcessorAsync interface.
     *
     * @param componentClassInfo the class info descriptor.
     * @return true if this class descriptor describes a Processor component, false otherwise.
     */
    private boolean isProcessor(ClassInfo componentClassInfo) {
        return implementsInterface(componentClassInfo, ProcessorSync.class) ||
                implementsInterface(componentClassInfo, ProcessorAsync.class);
    }

    /**
     * A component is just a component if it implements the Component interface.
     *
     * @param componentClassInfo the class info descriptor.
     * @return true if this class descriptor describes a Component, false otherwise.
     */
    private boolean isComponent(ClassInfo componentClassInfo) {
        return implementsInterface(componentClassInfo, Component.class);
    }

    /**
     * A component is class join if it implements the Join interface.
     *
     * @param componentClassInfo the class info descriptor.
     * @return true if this class descriptor describes a Join, false otherwise.
     */
    private boolean isJoin(ClassInfo componentClassInfo) {
        return implementsInterface(componentClassInfo, Join.class);
    }

    private static boolean implementsInterface(ClassInfo componentClassInfo, Class target) {
        return componentClassInfo.getInterfaces().stream().anyMatch(classInfo ->
                classInfo.getName().equals(target.getName()));
    }
}
