package com.reedelk.platform.services.scriptengine.evaluator;

import com.reedelk.platform.exception.ScriptCompilationException;
import com.reedelk.platform.pubsub.Event;
import com.reedelk.platform.pubsub.OnMessage;
import com.reedelk.platform.services.scriptengine.GroovyEngineProvider;
import com.reedelk.runtime.api.exception.PlatformException;
import com.reedelk.runtime.api.script.ScriptGlobalFunctions;
import com.reedelk.runtime.api.script.ScriptSource;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.reedelk.platform.pubsub.Action.Module.ActionModuleUninstalled;
import static com.reedelk.platform.pubsub.Action.Module.UN_INSTALLED;

public class ScriptSourceEvaluator extends ScriptEngineServiceAdapter {

    final Map<Long, Collection<String>> moduleIdAndScriptModuleNamesMap = new HashMap<>();

    public ScriptSourceEvaluator() {
        Event.operation.subscribe(UN_INSTALLED, this);
    }

    @Override
    public void register(ScriptGlobalFunctions globalFunction) {
        scriptEngine().bind(globalFunction.bindings());
        moduleIdAndScriptModuleNamesMap
                .put(globalFunction.moduleId(), globalFunction.bindings().keySet());
    }

    @Override
    public void register(ScriptSource scriptSource) {
        try (Reader reader = scriptSource.get()) {
            scriptEngine().compile(scriptSource.scriptModuleNames(), reader, scriptSource.bindings());
            moduleIdAndScriptModuleNamesMap.put(scriptSource.moduleId(), scriptSource.scriptModuleNames());
        } catch (IOException exception) {
            throw new PlatformException(exception);
        } catch (ScriptException scriptCompilationException) {
            throw new ScriptCompilationException(scriptSource, scriptCompilationException);
        }
    }

    @OnMessage
    public void onModuleUninstalled(ActionModuleUninstalled action) {
        long moduleId = action.getMessage();
        if (moduleIdAndScriptModuleNamesMap.containsKey(moduleId)) {
            moduleIdAndScriptModuleNamesMap.remove(moduleId)
                    .forEach(scriptModuleName -> scriptEngine().removeBinding(scriptModuleName));
        }
    }

    ScriptEngineProvider scriptEngine() {
        return GroovyEngineProvider.getInstance();
    }
}
