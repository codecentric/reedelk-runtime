package com.reedelk.runtime.component;

import com.reedelk.runtime.api.annotation.Description;
import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.component.Component;

@ModuleComponent("Fork")
@Description("This components copies the current flow message and sends each copy to every " +
                "branch following the component. The execution of each branch is performed in parallel " +
                "and the resulting messages from each execution can be merged by adding a Join component " +
                "right after the fork scope. If the fork scope is not followed by any branch an empty flow " +
                "message is set instead.")
public class Fork implements Component {
}
