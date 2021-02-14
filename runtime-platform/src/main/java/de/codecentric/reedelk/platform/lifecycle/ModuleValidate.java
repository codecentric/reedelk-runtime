package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.commons.Messages;
import de.codecentric.reedelk.platform.module.Module;
import de.codecentric.reedelk.platform.exception.FlowValidationException;
import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import de.codecentric.reedelk.runtime.commons.JsonParser;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ModuleValidate extends AbstractStep<Module, Module> {

    private static final Logger logger = LoggerFactory.getLogger(ModuleValidate.class);

    @Override
    public Module run(Module module) {

        deserialize(module).ifPresent(deSerializedModule -> {

            // Validate Flows
            Set<JSONObject> flows = deSerializedModule.getFlows();
            Collection<Exception> flowErrors = validateFlows(flows, module);

            // Validate Subflows
            Set<JSONObject> subflows = deSerializedModule.getSubflows();
            Collection<Exception> subflowErrors = validateSubFlows(subflows, module);

            // Validate Configurations
            Collection<JSONObject> configurations = deSerializedModule.getConfigurations();
            Collection<Exception> configurationErrors = validateConfigurations(configurations, module);

            Collection<Exception> errors = new ArrayList<>();
            errors.addAll(flowErrors);
            errors.addAll(subflowErrors);
            errors.addAll(configurationErrors);

            // If there is any error, the module transition to ERROR state.
            if (!errors.isEmpty()) {
                errors.forEach(exception -> logger.error(StringUtils.EMPTY, exception));
                module.error(errors);
            }

        });

        return module;
    }

    private Collection<Exception> validateFlows(Collection<JSONObject> flows, Module module) {
        Collection<Exception> exceptions = new ArrayList<>(UniquePropertyValidator.from(JsonParser.Flow.id())
                        .validate(flows, Messages.Flow.VALIDATION_ID_NOT_UNIQUE.format(module.name())));
        flows.forEach(definition -> {
            if (!JsonParser.Flow.hasId(definition) || StringUtils.isBlank(JsonParser.Flow.id(definition))) {
                String message = Messages.Flow.VALIDATION_ID_NOT_VALID.format(module.name());
                FlowValidationException exception = new FlowValidationException(message);
                exceptions.add(exception);
            }
        });
        return exceptions;
    }

    private Collection<Exception> validateSubFlows(Collection<JSONObject> subflows, Module module) {
        Collection<Exception> exceptions = new ArrayList<>(UniquePropertyValidator.from(JsonParser.Subflow.id())
                        .validate(subflows, Messages.Subflow.VALIDATION_ID_NOT_UNIQUE.format(module.name())));
        subflows.forEach(definition -> {
            if (!JsonParser.Subflow.hasId(definition) || StringUtils.isBlank(JsonParser.Subflow.id(definition))) {
                String message = Messages.Subflow.VALIDATION_ID_NOT_VALID.format(module.name());
                FlowValidationException exception = new FlowValidationException(message);
                exceptions.add(exception);
            }
        });
        return exceptions;
    }

    private Collection<Exception> validateConfigurations(Collection<JSONObject> configurations, Module module) {
        Collection<Exception> exceptions = new ArrayList<>(UniquePropertyValidator.from(JsonParser.Config.id())
                        .validate(configurations, Messages.Config.VALIDATION_ID_NOT_UNIQUE.format(module.name())));
        configurations.forEach(definition -> {
            if (!JsonParser.Config.hasId(definition) || StringUtils.isBlank(JsonParser.Config.id(definition))) {
                String message = Messages.Config.VALIDATION_ID_NOT_VALID.format(module.name());
                FlowValidationException exception = new FlowValidationException(message);
                exceptions.add(exception);
            }
        });
        return exceptions;
    }

    /**
     * Validates that all the items in the collection contain a property
     * value which is unique across all the elements in it.
     */
    static class UniquePropertyValidator {

        static UniquePropertyValidator from(String propertyName) {
            return new UniquePropertyValidator(propertyName);
        }

        private final String propertyName;

        UniquePropertyValidator(String propertyName) {
            this.propertyName = propertyName;
        }

        Collection<Exception> validate(Collection<JSONObject> flowsDefinition, String message) {
            Collection<Exception> errors = new ArrayList<>();
            boolean test = flowsDefinition.stream()
                    .filter(definition -> definition.has(propertyName))
                    .map(definition -> definition.get(propertyName))
                    .allMatch(new HashSet<>()::add);
            if (!test) errors.add(new FlowValidationException(message));
            return errors;
        }
    }
}
