package com.gobookee.common.enums;

public enum FileType {
    USER,
    PLACE,
    STUDY,
    BOOK;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
