package com.reedelk.runtime.component;

import com.reedelk.runtime.api.annotation.Description;
import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.component.Component;

@ModuleComponent("For Each")
@Description("The For Each component applies to each element of the input collection the components following the for each processor. " +
        "A Join component can be added right outside the scope of the For Each to merge together all the results. " +
        "If no Join component is present all the results after the execution are collected in a list.")
public class ForEach implements Component {
}
