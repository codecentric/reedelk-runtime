package com.reedelk.runtime.rest.api.health.v1;

public class HealthGETRes {

    private String version;
    private String status;

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
}
