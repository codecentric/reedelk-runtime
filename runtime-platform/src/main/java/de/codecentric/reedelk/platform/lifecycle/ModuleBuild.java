package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.commons.Messages;
import de.codecentric.reedelk.platform.flow.deserializer.converter.*;
import de.codecentric.reedelk.platform.module.DeSerializedModule;
import de.codecentric.reedelk.platform.module.ModulesManager;
import de.codecentric.reedelk.platform.module.state.ModuleState;
import de.codecentric.reedelk.platform.exception.FlowBuildException;
import de.codecentric.reedelk.platform.execution.FlowExecutorEngine;
import de.codecentric.reedelk.platform.flow.ErrorStateFlow;
import de.codecentric.reedelk.platform.flow.Flow;
import de.codecentric.reedelk.platform.flow.deserializer.FlowDeserializer;
import de.codecentric.reedelk.platform.flow.deserializer.FlowDeserializerContext;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.module.Module;
import de.codecentric.reedelk.runtime.api.commons.StackTraceUtils;
import de.codecentric.reedelk.runtime.converter.DeserializerConverter;
import de.codecentric.reedelk.runtime.commons.JsonParser;
import org.json.JSONObject;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class ModuleBuild extends AbstractStep<Module, Module> {

    private static final Logger logger = LoggerFactory.getLogger(ModuleBuild.class);

    @Override
    public Module run(Module module) {

        if (module.state() != ModuleState.RESOLVED) return module;

        deserialize(module).ifPresent(deSerializedModule -> {

            Bundle bundle = bundle();

            Set<Flow> flows = deSerializedModule.getFlows().stream()
                    .map(flowDefinition -> buildFlow(bundle, flowDefinition, deSerializedModule))
                    .collect(toSet());

            // If exists at least one flow in error state,
            // then we release component references for each flow built
            // belonging to this Module.
            Set<ErrorStateFlow> flowsWithErrors = flows.stream()
                    .filter(flow -> flow instanceof ErrorStateFlow)
                    .map(flow -> (ErrorStateFlow) flow)
                    .collect(toSet());

            if (flowsWithErrors.isEmpty()) {
                module.stop(flows);
            } else {
                // If there are errors, we MUST release references
                // for the flows we have already built.
                flows.forEach(flow -> flow.releaseReferences(bundle));

                module.error(flowsWithErrors.stream()
                        .map(ErrorStateFlow::getException)
                        .collect(toList()));
            }
        });

        return module;
    }

    private Flow buildFlow(Bundle bundle, JSONObject flowDefinition, DeSerializedModule deSerializedModule) {
        ExecutionGraph flowGraph = ExecutionGraph.build();
        FlowExecutorEngine executionEngine = new FlowExecutorEngine(flowGraph);

        ModulesManager modulesManager = modulesManager();
        Module module = modulesManager.getModuleById(bundle.getBundleId());
        long moduleId = module.id();

        String flowId = JsonParser.Flow.id(flowDefinition);
        String flowTitle = JsonParser.Flow.hasTitle(flowDefinition) ? JsonParser.Flow.title(flowDefinition) : null;

        DeserializerConverter converter = createDeserializerConverter(deSerializedModule, module, moduleId);
        try {

            FlowDeserializerContext context = new FlowDeserializerContext(bundle, modulesManager, deSerializedModule, converter);
            FlowDeserializer flowDeserializer = new FlowDeserializer(context);
            flowDeserializer.deserialize(flowGraph, flowDefinition);
            return new Flow(module.id(), module.name(), flowId, flowTitle, flowGraph, executionEngine);

        } catch (Throwable exception) {
            // We are catching throwable because when an Implementor's 'initialize()' method is called it might
            // throw an exception if the bundle configuration is not correct. In order to provide the user a
            // meaningful and clear error message, we wrap the exception with the root cause details.
            String rootCauseMessage = StackTraceUtils.rootCauseMessageOf(exception);
            String errorMessage = Messages.FlowErrorMessage.DEFAULT.format(moduleId, module.name(), flowId, flowTitle,
                    null, exception.getClass().getName(), rootCauseMessage);

            FlowBuildException buildException = new FlowBuildException(errorMessage, exception);

            if (logger.isErrorEnabled()) {
                logger.error(errorMessage, exception);
            }

            return new ErrorStateFlow(module.id(), module.name(),
                    flowId, flowTitle, flowGraph,
                    executionEngine, buildException);
        }
    }

    DeserializerConverter createDeserializerConverter(DeSerializedModule deSerializedModule,
                                                              Module module,
                                                              long moduleId) {
        DeserializerConverter converter = DeserializerConverter.getInstance();
        converter = new ResourceResolverDecorator(converter, deSerializedModule, module); // Evaluate fifth
        converter = new ScriptResolverDecorator(converter, deSerializedModule); // Evaluate fourth
        converter = new ConfigPropertyDecorator(configurationService(), converter); // Evaluate third
        converter = new SystemConfigPropertyReplacerDecorator(systemPropertyService(), converter); // Evaluate second
        converter = new DeserializerConverterContextDecorator(converter, moduleId); // Evaluate first
        return converter;
    }
}
