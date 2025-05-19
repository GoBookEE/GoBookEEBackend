package com.gobookee.common;

public class CommonPathTemplate {
    public static final String PREFIX = "/WEB-INF/views";
    public static final String SUFFIX = ".jsp";
    public static final String BASIC_UPLOAD_PATH = "/resources/upload";

    public static String getPath(String viewName) {
        return PREFIX + viewName + SUFFIX;
    }
}
