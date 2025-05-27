package com.gobookee.common;

import com.gobookee.common.enums.ContentType;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Gson 라이브러리로 JSON <-> 객체 변환 템플릿
 */
public class JsonConvertTemplate {
    private static final Gson gson = new Gson();
    /**
     * JSON 문자열을 자바 객체로 변환해주는 메소드
     * 문자열, 클래스 타입 인자로 전달해주면 해당 타입 객체 반환
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * 자바 객체를 JSON 문자열로 변환해주는 메소드
     * 자바 객체를 인자로 전달해주면 JSON 문자열 반환
     * @param obj
     * @return
     */
    public static void toJson(Object obj, HttpServletResponse response) throws IOException {
        response.setContentType(ContentType.JSON.toString());
        gson.toJson(obj, response.getWriter());
    }
}
