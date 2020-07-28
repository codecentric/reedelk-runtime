package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableAbstract1;
import com.reedelk.openapi.OpenApiSerializableContext1;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class InfoObject extends OpenApiSerializableAbstract1 {

    private String title;
    private String description;
    private String termsOfService;
    private String version;
    private ContactObject contact;
    private LicenseObject license;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }

    public ContactObject getContact() {
        return contact;
    }

    public void setContact(ContactObject contact) {
        this.contact = contact;
    }

    public LicenseObject getLicense() {
        return license;
    }

    public void setLicense(LicenseObject license) {
        this.license = license;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public Map<String, Object> serialize(OpenApiSerializableContext1 context) {
        Map<String, Object> map = new LinkedHashMap<>();
        set(map, "title", ofNullable(title).orElse("API")); // REQUIRED
        set(map, "description", description);
        set(map, "termsOfService", termsOfService);
        set(map, "contact", contact, context);
        set(map, "license", license, context);
        set(map, "version", ofNullable(version).orElse("v1")); // REQUIRED
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {
        title = getString(serialized, "title");
        description = getString(serialized, "description");
        termsOfService = getString(serialized, "termsOfService");
        version = getString(serialized, "version");
        if (serialized.containsKey("contact")) {
            contact = new ContactObject();
            contact.deserialize(getMap(serialized, "contact"));
        }
        if (serialized.containsKey("license")) {
            license = new LicenseObject();
            license.deserialize(getMap(serialized, "license"));
        }
    }
}
