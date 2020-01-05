package com.reedelk.esb.flow.deserializer;


import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.esb.module.DeSerializedModule;
import com.reedelk.esb.module.ModulesManager;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.converter.DeserializerConverter;
import org.osgi.framework.Bundle;

public class FlowDeserializerContext {

    private final Bundle bundle;
    private final DeserializerConverter deserializerConverter;
    private final ModulesManager modulesManager;
    private final DeSerializedModule deSerializedModule;

    public FlowDeserializerContext(Bundle bundle,
                                   ModulesManager modulesManager,
                                   DeSerializedModule deSerializedModule,
                                   DeserializerConverter deserializerConverter) {
        this.bundle = bundle;
        this.deserializerConverter = deserializerConverter;
        this.modulesManager = modulesManager;
        this.deSerializedModule = deSerializedModule;
    }

    public ExecutionNode instantiateComponent(Class clazz) {
        return instantiateComponent(clazz.getName());
    }

    public ExecutionNode instantiateComponent(String componentName) {
        return modulesManager.instantiateComponent(bundle.getBundleContext(), componentName);
    }

    public Implementor instantiateImplementor(ExecutionNode executionNode, String implementorName) {
        return modulesManager.instantiateImplementor(bundle.getBundleContext(), executionNode, implementorName);
    }

    public DeSerializedModule deserializedModule() {
        return deSerializedModule;
    }

    public DeserializerConverter converter() {
        return deserializerConverter;
    }
}
