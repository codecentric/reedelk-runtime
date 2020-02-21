package com.reedelk.runtime.component;

import com.reedelk.runtime.api.annotation.Description;
import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.component.Component;

@ModuleComponent("Try-Catch")
@Description("Allows to execute a sequence of components in the try branch " +
                "and perform another sequence of components in the catch branch whenever an " +
                "error is thrown by the execution of the components in the try.")
public class TryCatch implements Component {
}
