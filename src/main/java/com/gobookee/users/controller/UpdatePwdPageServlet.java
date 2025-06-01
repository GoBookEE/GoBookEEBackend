package com.gobookee.users.controller;

import com.gobookee.common.CommonPathTemplate;
import com.gobookee.common.MessageRedirectTemplate;
import com.gobookee.users.service.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "updatePwdPageServlet", urlPatterns = "/updatepwdpage")
public class UpdatePwdPageServlet extends HttpServlet {
    private UserService userService = UserService.userService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String email = request.getParameter("email");
        boolean result = userService.searchUserByIdAndEmail(userId, email);

        if (result) {
            request.getRequestDispatcher(CommonPathTemplate.getViewPath("/user/updatePwd")).forward(request, response);
        } else {
            MessageRedirectTemplate.builder()
                    .error("error")
                    .msg("아이디와 이메일이 일치하지 않습니다.")
                    .loc("/findpwdpage")
                    .request(request)
                    .response(response)
                    .build().forward();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
