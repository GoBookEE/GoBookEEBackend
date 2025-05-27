package com.gobookee.users.model.dto;

public enum Gender {
    M("M"),
    F("F");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
