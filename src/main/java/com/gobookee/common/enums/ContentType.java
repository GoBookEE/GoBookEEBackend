package com.gobookee.common.enums;

/**
 * 자주 사용하는 ContentType Enum으로 설정
 */
public enum ContentType {
    JSON("application/json;charset=utf-8"),
    HTML("text/html;charset=utf-8"),
    UPLOAD("multipart/form-data"),
    DOWNLOAD("application/octet-stream");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
