package com.herawi.sigma.filter;

import java.util.ArrayList;

public class FilterResponse {
    ArrayList<String> failedFields;
    boolean isOk;

    public FilterResponse(ArrayList<String> failedFields, boolean isOk) {
        this.failedFields = failedFields;
        this.isOk = isOk;
    }

    public ArrayList<String> getFailedFields() {
        return failedFields;
    }

    public void setFailedFields(ArrayList<String> failedFields) {
        this.failedFields = failedFields;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }
}