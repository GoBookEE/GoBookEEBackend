package com.gobookee.common;

public class ViewPath {
    public static final String PREFIX = "/WEB-INF/views";
    public static final String SUFFIX = ".jsp";

    public static String getPath(String viewName) {
        return PREFIX + viewName + SUFFIX;
    }
}
