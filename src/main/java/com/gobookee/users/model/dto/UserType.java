package com.gobookee.users.model.dto;

public enum UserType {
    USER("0"),
    OWNER("1");
    private final String value;

    UserType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
