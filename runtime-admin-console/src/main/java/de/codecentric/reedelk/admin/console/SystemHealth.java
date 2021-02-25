package de.codecentric.reedelk.admin.console;

import de.codecentric.reedelk.runtime.api.annotation.ModuleComponent;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.rest.api.InternalAPI;
import de.codecentric.reedelk.runtime.rest.api.health.v1.HealthGETRes;
import de.codecentric.reedelk.runtime.system.api.SystemProperty;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("System health")
@Component(service = SystemHealth.class, scope = PROTOTYPE)
public class SystemHealth implements ProcessorSync {

    @Reference
    private SystemProperty systemProperty;

    private static final String UP_STATUS = "UP";

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        String resultJson = healthStatus();

        return MessageBuilder.get(SystemHealth.class)
                .withJson(resultJson)
                .build();
    }

    private String healthStatus() {
        HealthGETRes health = new HealthGETRes();
        health.setStatus(UP_STATUS);
        health.setVersion(systemProperty.version());
        health.setQualifier(systemProperty.qualifier());
        return InternalAPI.Health.V1.GET.Res.serialize(health);
    }
}
