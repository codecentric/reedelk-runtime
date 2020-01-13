package com.reedelk.module.descriptor.model;

import java.io.Serializable;
import java.util.List;

public class AutoCompleteContributorDescriptor implements Serializable {

    private boolean error;
    private boolean message;
    private boolean context;
    private List<String> contributions;

    public AutoCompleteContributorDescriptor() {
    }

    // Used by Custom Functions definition
    public AutoCompleteContributorDescriptor(List<String> contributions) {
        this.contributions = contributions;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public boolean isContext() {
        return context;
    }

    public void setContext(boolean context) {
        this.context = context;
    }

    public List<String> getContributions() {
        return contributions;
    }

    public void setContributions(List<String> contributions) {
        this.contributions = contributions;
    }

    @Override
    public String toString() {
        return "AutoCompleteContributorDescriptor{" +
                "error=" + error +
                ", message=" + message +
                ", context=" + context +
                ", contributions=" + contributions +
                '}';
    }
}
