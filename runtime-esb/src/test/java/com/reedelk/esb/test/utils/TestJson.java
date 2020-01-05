package com.reedelk.esb.test.utils;

import com.reedelk.runtime.api.commons.FileUtils;
import com.reedelk.runtime.commons.JsonParser;
import org.json.JSONObject;

import java.net.URL;

public enum TestJson {

    FLOW_WITH_COMPONENTS {
        @Override
        String path() {
            return "/com/reedelk/esb/lifecycle/flow_with_some_components.flow";
        }
    },

    FLOW_WITH_ROUTER {
        @Override
        String path() {
            return "/com/reedelk/esb/lifecycle/flow_with_router.flow";
        }
    },

    FLOW_WITHOUT_ID {
        @Override
        String path() {
            return "/com/reedelk/esb/lifecycle/flow_without_id.flow";
        }
    },

    FLOW_WITH_NOT_WELL_FORMED_ROUTER {
        @Override
        String path() {
            return "/com/reedelk/esb/lifecycle/flow_with_not_well_formed_router.flow";
        }
    },

    FLOW_WITH_NOT_WELL_FORMED_FORK {
        @Override
        String path() {
            return "/com/reedelk/esb/lifecycle/flow_with_not_well_formed_fork.flow";
        }
    },

    SUBFLOW_WITH_COMPONENTS {
        @Override
        String path() {
            return "/com/reedelk/esb/lifecycle/subflow_with_some_components.flow";
        }
    },

    CONFIG {
        @Override
        String path() {
            return "/com/reedelk/esb/lifecycle/config.fconfig";
        }
    };

    public URL url() {
        return TestJson.class.getResource(path());
    }

    abstract String path();

    public JSONObject parse() {
        URL url = url();
        String flowAsJson = FileUtils.ReadFromURL.asString(url);
        return JsonParser.from(flowAsJson);
    }
}
