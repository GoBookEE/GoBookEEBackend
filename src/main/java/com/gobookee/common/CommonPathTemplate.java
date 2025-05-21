package com.gobookee.common;

/**
 * 경로 설정 템플릿
 * 사용법 : getPath() 인자로 views 이후 경로 + 파일명 (확장자 .jsp 제외)을 넘겨주면 전체 경로를 가져올 수 있다.
 */
public class CommonPathTemplate {
    public static final String PREFIX = "/WEB-INF/views";
    public static final String SUFFIX = ".jsp";
    public static final String BASIC_UPLOAD_PATH = "/resources/upload";

    public static String getPath(String viewName) {
        return PREFIX + viewName + SUFFIX;
    }
}
