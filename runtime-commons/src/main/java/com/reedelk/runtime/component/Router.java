package com.reedelk.runtime.component;

import com.reedelk.runtime.api.annotation.Description;
import com.reedelk.runtime.api.annotation.Example;
import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.script.dynamicmap.DynamicBooleanMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;

@ModuleComponent("Router")
@Description("A router component allows to execute only one of the branches following " +
                "the component based on a given condition which must be defined for each branch. " +
                "A default condition is mandatory, which represents the default branch to be executed " +
                "when no other condition is met. A condition might be a function checking the content of " +
                "the payload or the presence (or absence) of a specific attribute in the message and so on.")
public class Router implements Component {

    public static final DynamicString DEFAULT_CONDITION = DynamicString.from("otherwise");

    @Property("Condition and route definitions")
    @Example("<code>message.payload().contains('orders')</code>")
    @Description("For each route a dynamic value returning a boolean value must be specified. Each dynamic value is evaluated for each branch of the router to determine which branch has to be executed.")
    private DynamicBooleanMap conditionAndRouteDefinitions = DynamicBooleanMap.empty();

}
