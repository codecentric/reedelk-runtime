package de.codecentric.reedelk.runtime.component;

import de.codecentric.reedelk.runtime.api.annotation.Description;
import de.codecentric.reedelk.runtime.api.annotation.ModuleComponent;
import de.codecentric.reedelk.runtime.api.component.Component;

@ModuleComponent("Fork")
@Description("The Fork component copies the current flow message and sends each copy to every " +
                "branch following the component. The execution of each branch is performed in parallel " +
                "and the resulting messages from each execution can be merged by adding a Join component " +
                "right after the fork scope. If the fork scope is not followed by any branch an empty flow " +
                "message is set instead.")
public class Fork implements Component {
}
