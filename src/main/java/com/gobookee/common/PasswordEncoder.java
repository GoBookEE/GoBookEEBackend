package com.gobookee.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordEncoder extends HttpServletRequestWrapper {
    public PasswordEncoder(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String ori) {
        if (ori.equals("password") || ori.equals("password_new")) {
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
