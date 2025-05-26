package com.gobookee.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * SHA512 방식과 필터를 적용해서 패스워드를 암호화해주는 템플릿
 * 사용법 : 단방향 암호화이기 때문에 비교할 문자열도 암호화 진행 후 equals()로 비교할 것!!
 */
public class PasswordEncoder extends HttpServletRequestWrapper {
    public PasswordEncoder(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String ori) {
        if (ori.equals("password") || ori.equals("password_new") || ori.equals("userPwd")) {
            return getSHA512(super.getParameter(ori));
        }
        return super.getParameter(ori);
    }

    public String getSHA512(String oriVal) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] oriValArr = oriVal.getBytes();
        md.update(oriValArr);
        byte[] encOriValArr = md.digest();

        return Base64.getEncoder().encodeToString(encOriValArr);
    }
}
