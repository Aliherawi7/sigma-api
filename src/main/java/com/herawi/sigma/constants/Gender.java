package com.herawi.sigma.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum  Gender {
    MALE("MALE"),
    FEMALE("FEMALE");

    final String value;
    Gender(String value){
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
    @JsonCreator
    public static Gender decode(final String value){
        return Stream.of(Gender.values()).filter(targetEnum -> targetEnum.value.equals(value.toUpperCase())).findFirst().orElse(null);
    }
}