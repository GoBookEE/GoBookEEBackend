package com.gobookee.common.enums;

public enum FileType {
    NOTICE,
    USER,
    PLACE,
    STUDY,
    BOOK;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
