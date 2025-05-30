package com.gobookee.users.controller;

import com.gobookee.common.MessageRedirectTemplate;
import com.gobookee.users.model.dto.User;
import com.gobookee.users.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet implementation class UserLoginServlet
 */
@WebServlet(name = "userLoginServlet",
        urlPatterns = {"/login"})
public class UserLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String userPwd = request.getParameter("userPwd");
        String saveId = request.getParameter("saveId");

        if (saveId != null) {
            Cookie c = new Cookie("saveId", userId);
            c.setMaxAge(60 * 60 * 24 * 7);
            c.setPath("/");
            response.addCookie(c);
        } else {
            Cookie c = new Cookie("saveId", userId);
            c.setMaxAge(0);
            c.setPath("/");
            response.addCookie(c);
        }

        User u = UserService.userService().searchUserById(userId);
        if (u != null && u.getUserPwd().equals(userPwd)) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", u);
            MessageRedirectTemplate.builder()
                    .msg("로그인 성공 ")
                    .response(response)
                    .request(request)
                    .build().forward();
        } else {
            MessageRedirectTemplate.builder()
                    .msg("아이디나 패스워드가 일치하지 않습니다.")
                    .loc("/loginpage")
                    .error("error")
                    .response(response)
                    .request(request)
                    .build().forward();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
