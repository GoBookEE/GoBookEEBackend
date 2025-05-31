package com.gobookee.users.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/verifycode")
public class VerifyEmailServlet extends HttpServlet {
    private static final long CODE_EXPIRE_TIME = 5 * 60 * 1000; // 5분 (ms)

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String inputCode = request.getParameter("code");
        HttpSession session = request.getSession();

        String authCode = (String) session.getAttribute("authCode");
        Long codeTime = (Long) session.getAttribute("authCodeTime");

        boolean isValid = false;
        if (authCode != null && codeTime != null) {
            long now = System.currentTimeMillis();
            boolean timeValid = now - codeTime <= CODE_EXPIRE_TIME;

            if (timeValid && authCode.equals(inputCode)) {
                isValid = true;
                session.removeAttribute("authCode");
                session.removeAttribute("authCodeTime");
            }
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"result\": " + isValid + "}");
    }
}
