package com.herawi.sigma.constants;

public enum APIEndpoints {
    PROFILE_PICTURE("api/v1/files/profile-picture/");

    final String value;
    APIEndpoints(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
