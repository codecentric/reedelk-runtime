package de.codecentric.reedelk.runtime.component;

import de.codecentric.reedelk.runtime.api.annotation.Description;
import de.codecentric.reedelk.runtime.api.annotation.ModuleComponent;
import de.codecentric.reedelk.runtime.api.component.Component;

@ModuleComponent("For Each")
@Description("The For Each component applies to each element of the input collection the components following the for each processor. " +
        "A Join component can be added right outside the scope of the For Each to merge together all the results. " +
        "If no Join component is present all the results after the execution are collected in a list.")
public class ForEach implements Component {
}
