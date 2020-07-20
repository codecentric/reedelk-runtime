package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.openapi.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.JsonObjectFactory;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import static java.util.Optional.ofNullable;

@Collapsible
@Component(service = LicenseObject.class, scope = ServiceScope.PROTOTYPE)
public class LicenseObject extends AbstractOpenApiSerializable implements Implementor {

    @Property("Name")
    @Hint("Apache 2.0")
    @Example("Apache 2.0")
    @Description("The license name used for the API.")
    private String name;

    @Property("URL")
    @Hint("http://www.apache.org/licenses/LICENSE-2.0.html")
    @Example("http://www.apache.org/licenses/LICENSE-2.0.html")
    @Description("A URL to the license used for the API. MUST be in the format of a URL.")
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public JSONObject serialize(OpenApiSerializableContext context) {
        JSONObject serialized = JsonObjectFactory.newJSONObject();
        set(serialized, "name", ofNullable(name).orElse("API License"));
        set(serialized, "url", url);
        return serialized;
    }
}
