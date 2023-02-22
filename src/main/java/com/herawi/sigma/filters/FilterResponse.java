package com.herawi.sigma.filters;

import java.util.Map;

public class FilterResponse {
    Map<String, String> failedFields;
    boolean isOk;

    public FilterResponse(Map<String, String> failedFields, boolean isOk) {
        this.failedFields = failedFields;
        this.isOk = isOk;
    }

    public Map<String, String> getFailedFields() {
        return failedFields;
    }

    public void setFailedFields(Map<String, String> failedFields) {
        this.failedFields = failedFields;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }
}