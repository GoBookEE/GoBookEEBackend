package com.gobookee.users.controller;

import com.gobookee.common.MessageRedirectTemplate;
import com.gobookee.users.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "updatePwdHandlerServlet", urlPatterns = "/updatepwd")
public class UpdatePwdHandlerServlet extends HttpServlet {
    private UserService userService = UserService.userService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String newPassword = request.getParameter("newPassword");
        boolean result = userService.changePwd(userId, newPassword);
        if (result) {
            MessageRedirectTemplate.builder()
                    .msg("비밀번호 변경에 성공했습니다.")
                    .response(response)
                    .request(request)
                    .build().forward();
        } else {
            MessageRedirectTemplate.builder()
                    .msg("비밀번호 변경에 실패했습니다.")
                    .loc("/findpwdpage")
                    .error("error")
                    .response(response)
                    .request(request)
                    .build().forward();
        }
    }
}
