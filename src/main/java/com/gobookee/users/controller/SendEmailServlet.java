package com.gobookee.users.controller;

import com.gobookee.common.JsonConvertTemplate;
import com.gobookee.common.MailSenderUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "sendEmailServlet", urlPatterns = "/sendemail")
public class SendEmailServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String toEmail = request.getParameter("email");

        // 인증번호 및 생성시간 저장
        String authCode = String.valueOf((int) (Math.random() * 900000) + 100000);
        long createTime = System.currentTimeMillis(); // 현재 시간 (ms)

        HttpSession session = request.getSession();
        session.setAttribute("authCode", authCode);
        session.setAttribute("authCodeTime", createTime);

        String subject = "[GoBookE] 회원가입 인증번호";
        String content = "인증번호는 [" + authCode + "] 입니다. 5분 이내에 입력해주세요.";

        boolean result = MailSenderUtil.sendEmail(toEmail, subject, content);

        JsonConvertTemplate.toJson(result, response);
    }
}
