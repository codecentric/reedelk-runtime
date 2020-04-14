package com.reedelk.platform.component;

import com.reedelk.platform.component.foreach.ForEachWrapper;
import com.reedelk.platform.component.fork.ForkWrapper;
import com.reedelk.platform.component.router.RouterWrapper;
import com.reedelk.platform.component.trycatch.TryCatchWrapper;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.component.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RuntimeComponents {

    private static final Map<String, Class<? extends Component>> COMPONENTS;

    static {
        Map<String, Class<? extends Component>> tmp = new HashMap<>();
        tmp.put(Stop.class.getName(), Stop.class);
        tmp.put(Fork.class.getName(), ForkWrapper.class);
        tmp.put(Router.class.getName(), RouterWrapper.class);
        tmp.put(ForEach.class.getName(), ForEachWrapper.class);
        tmp.put(TryCatch.class.getName(), TryCatchWrapper.class);
        tmp.put(FlowReference.class.getName(), FlowReference.class);
        COMPONENTS = Collections.unmodifiableMap(tmp);
    }

    private RuntimeComponents() {
    }

    public static boolean is(String componentName) {
        return COMPONENTS.containsKey(componentName);
    }

    public static boolean is(Component componentObject) {
        return COMPONENTS.keySet().stream().anyMatch(componentClassName -> {
            try {
                Class<?> aClass = Class.forName(componentClassName);
                return aClass.isAssignableFrom(componentObject.getClass());
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
    }

    public static Class<? extends Component> getDefiningClass(String componentName) {
        return COMPONENTS.get(componentName);
    }

    public static Collection<String> allNames() {
        return COMPONENTS.keySet();
    }
}
