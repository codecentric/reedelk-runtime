package de.codecentric.reedelk.runtime.rest.api.health.v1;

public class HealthGETRes {

    private String version;
    private String status;
    private String qualifier;

    public HealthGETRes() { }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }
}
