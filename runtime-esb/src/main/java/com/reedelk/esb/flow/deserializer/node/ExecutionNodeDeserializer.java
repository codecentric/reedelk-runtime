package com.reedelk.esb.flow.deserializer.node;

import com.reedelk.esb.flow.deserializer.FlowDeserializerContext;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.commons.JsonParser;
import com.reedelk.runtime.component.FlowReference;
import com.reedelk.runtime.component.Fork;
import com.reedelk.runtime.component.Router;
import com.reedelk.runtime.component.TryCatch;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExecutionNodeDeserializer {

    private ExecutionGraph graph;
    private ExecutionNode parent;
    private FlowDeserializerContext context;
    private JSONObject componentDefinition;

    private static final Class<? extends Deserializer> GENERIC_DESERIALIZER = GenericComponentDeserializer.class;

    private static final Map<String, Class<? extends Deserializer>> COMPONENT_NAME_DESERIALIZER_MAP;
    static {
        Map<String, Class<? extends Deserializer>> tmp = new HashMap<>();
        tmp.put(Fork.class.getName(), ForkDeserializer.class);
        tmp.put(Router.class.getName(), RouterDeserializer.class);
        tmp.put(TryCatch.class.getName(), TryCatchDeserializer.class);
        tmp.put(FlowReference.class.getName(), FlowReferenceDeserializer.class);
        COMPONENT_NAME_DESERIALIZER_MAP = Collections.unmodifiableMap(tmp);
    }

    private ExecutionNodeDeserializer() {
    }

    public static ExecutionNodeDeserializer get() {
        return new ExecutionNodeDeserializer();
    }

    public ExecutionNodeDeserializer graph(ExecutionGraph graph) {
        this.graph = graph;
        return this;
    }

    public ExecutionNodeDeserializer parent(ExecutionNode parent) {
        this.parent = parent;
        return this;
    }

    public ExecutionNodeDeserializer context(FlowDeserializerContext context) {
        this.context = context;
        return this;
    }

    public ExecutionNodeDeserializer componentDefinition(JSONObject componentDefinition) {
        this.componentDefinition = componentDefinition;
        return this;
    }

    public ExecutionNode deserialize() {
        String componentName = JsonParser.Implementor.name(componentDefinition);
        Class<? extends Deserializer> deserializerClazz = COMPONENT_NAME_DESERIALIZER_MAP.getOrDefault(componentName, GENERIC_DESERIALIZER);
        return instanceOf(graph, context, deserializerClazz).deserialize(parent, componentDefinition);
    }

    private static Deserializer instanceOf(ExecutionGraph graph, FlowDeserializerContext context, Class<? extends Deserializer> builderClazz) {
        try {
            return builderClazz
                    .getDeclaredConstructor(ExecutionGraph.class, FlowDeserializerContext.class)
                    .newInstance(graph, context);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ESBException(e);
        }
    }
}
