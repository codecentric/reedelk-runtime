package de.codecentric.reedelk.admin.console;

import de.codecentric.reedelk.runtime.api.annotation.ModuleComponent;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.rest.api.hotswap.v1.HotSwapPOSTReq;
import de.codecentric.reedelk.runtime.rest.api.hotswap.v1.HotSwapPOSTRes;
import de.codecentric.reedelk.runtime.rest.api.hotswap.v1.HotSwapPOSTResNotFound;
import de.codecentric.reedelk.runtime.system.api.HotSwapService;
import de.codecentric.reedelk.runtime.system.api.ModuleNotFoundException;
import de.codecentric.reedelk.runtime.rest.api.InternalAPI;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;
import static de.codecentric.reedelk.runtime.rest.api.InternalAPI.HotSwap;

@ModuleComponent("Module hot swap")
@Component(service = ModuleHotSwap.class, scope = PROTOTYPE)
public class ModuleHotSwap implements ProcessorSync {

    private static final int STATUS_NOT_FOUND = 404;
    private static final String REQUEST_RESPONSE_CODE = "responseCode";

    @Reference
    private HotSwapService hotSwapService;

    @Override
    public Message apply(FlowContext flowContext, Message message) {

        String payload = message.payload();

        String resultJson = hotSwap(payload, flowContext);

        return MessageBuilder.get(ModuleHotSwap.class)
                .withJson(resultJson)
                .build();
    }

    private String hotSwap(String json, FlowContext flowContext) {
        HotSwapPOSTReq hotSwapReq = HotSwap.V1.POST.Req.deserialize(json);

        long hotSwappedModuleId;
        try {
            hotSwappedModuleId = hotSwapService.hotSwap(hotSwapReq.getModuleFilePath(), hotSwapReq.getResourcesRootDirectory());
        } catch (ModuleNotFoundException e) {
            // If we tried to Hot swap a module which was not installed in the runtime,
            // we return status code 'Not Found' - 404.
            flowContext.put(REQUEST_RESPONSE_CODE, STATUS_NOT_FOUND);
            HotSwapPOSTResNotFound dto = new HotSwapPOSTResNotFound();
            dto.setModuleFilePath(hotSwapReq.getModuleFilePath());
            dto.setResourcesRootDirectory(hotSwapReq.getResourcesRootDirectory());
            return InternalAPI.HotSwap.V1.POST.ResNotFound.serialize(dto);
        }

        HotSwapPOSTRes dto = new HotSwapPOSTRes();
        dto.setModuleId(hotSwappedModuleId);
        return InternalAPI.HotSwap.V1.POST.Res.serialize(dto);
    }
}
