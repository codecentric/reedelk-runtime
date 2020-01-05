package com.reedelk.runtime.component;

import com.reedelk.runtime.api.annotation.ESBComponent;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;

@ESBComponent("Router")
public class Router implements Component {

    public static final DynamicString DEFAULT_CONDITION = DynamicString.from("otherwise");

}
