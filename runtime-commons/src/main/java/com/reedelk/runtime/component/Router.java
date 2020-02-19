package com.reedelk.runtime.component;

import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;

@ModuleComponent(
        name="Router",
        description = "A router component allows to execute only one of the branches following " +
                "the component based on a given condition which must be defined for each branch. " +
                "A default condition is mandatory, which represents the default branch to be executed " +
                "when no other condition is met. A condition might be a function checking the content of " +
                "the payload or the presence (or absence) of a specific attribute in the message and so on.")
public class Router implements Component {

    public static final DynamicString DEFAULT_CONDITION = DynamicString.from("otherwise");

}
