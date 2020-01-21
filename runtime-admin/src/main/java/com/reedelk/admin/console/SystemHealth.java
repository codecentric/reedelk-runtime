package com.reedelk.admin.console;

import com.reedelk.runtime.api.annotation.ESBComponent;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.rest.api.InternalAPI;
import com.reedelk.runtime.rest.api.health.v1.HealthGETRes;
import com.reedelk.runtime.system.api.SystemProperty;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ESBComponent("System health")
@Component(service = SystemHealth.class, scope = PROTOTYPE)
public class SystemHealth implements ProcessorSync {

    @Reference
    private SystemProperty systemProperty;

    private static final String UP_STATUS = "UP";

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        String resultJson = healthStatus();

        return MessageBuilder.get().withJson(resultJson).build();
    }

    private String healthStatus() {

        HealthGETRes health = new HealthGETRes();

        health.setStatus(UP_STATUS);

        health.setVersion(systemProperty.version());

        return InternalAPI.Health.V1.GET.Res.serialize(health);
    }
}
