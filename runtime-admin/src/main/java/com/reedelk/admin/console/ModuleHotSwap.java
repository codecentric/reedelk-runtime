package com.reedelk.admin.console;

import com.reedelk.runtime.api.annotation.ESBComponent;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.rest.api.hotswap.v1.HotSwapPOSTReq;
import com.reedelk.runtime.rest.api.hotswap.v1.HotSwapPOSTRes;
import com.reedelk.runtime.rest.api.hotswap.v1.HotSwapPOSTResNotFound;
import com.reedelk.runtime.system.api.HotSwapService;
import com.reedelk.runtime.system.api.ModuleNotFoundException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import static com.reedelk.runtime.rest.api.InternalAPI.HotSwap;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ESBComponent("Module hot swap")
@Component(service = ModuleHotSwap.class, scope = PROTOTYPE)
public class ModuleHotSwap implements ProcessorSync {

    private static final int STATUS_NOT_FOUND = 404;
    private static final String REQUEST_RESPONSE_CODE = "responseCode";

    @Reference
    private HotSwapService hotSwapService;

    @Override
    public Message apply(Message message, FlowContext flowContext) {

        String payload = message.payload();

        String resultJson = hotSwap(payload, flowContext);

        return MessageBuilder.get().withJson(resultJson).build();
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
            return HotSwap.V1.POST.ResNotFound.serialize(dto);
        }

        HotSwapPOSTRes dto = new HotSwapPOSTRes();
        dto.setModuleId(hotSwappedModuleId);
        return HotSwap.V1.POST.Res.serialize(dto);
    }
}
