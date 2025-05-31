package com.gobookee.users.controller;

import com.gobookee.common.MessageRedirectTemplate;
import com.gobookee.users.model.dto.Gender;
import com.gobookee.users.model.dto.User;
import com.gobookee.users.model.dto.UserType;
import com.gobookee.users.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class UserInsertServlet
 */
@WebServlet(name = "userInsertServlet", urlPatterns = {"/signup"})
public class UserInsertServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UserInsertServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String emailVerified = request.getParameter("emailVerified");
        if (emailVerified == null || !emailVerified.equals("true")) {
            MessageRedirectTemplate.builder()
                    .request(request)
                    .msg("이메일 인증이 완료되지 않았습니다.")
                    .error("error")
                    .loc("/signuppage?userType=" + request.getParameter("userType"))
                    .response(response)
                    .build().forward();
            return;
        }

        String gender = request.getParameter("userGender");
        Gender userGender = null;
        try {
            userGender = Gender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
        }

        String type = request.getParameter("userType");
        UserType userType = null;
        try {
            userType = UserType.valueOf(type);
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
        }

        String userNickname = request.getParameter("userNickname");
        String address = request.getParameter("userAddress");
        String userAddressDetail = request.getParameter("userAddressDetail");
        String userId = request.getParameter("userId");
        String userPwd = request.getParameter("userPwd");
        String userEmail = request.getParameter("userEmail");
        String userPhone = request.getParameter("userPhone");

        User newUser = User.builder()
                .UserAddress(address + " || " + userAddressDetail)
                .UserGender(userGender)
                .UserNickName(userNickname)
                .UserId(userId)
                .UserPwd(userPwd)
                .UserType(userType)
                .UserEmail(userEmail)
                .UserPhone(userPhone)
                .build();

        int result = UserService.userService().insertUser(newUser);
        String url;

        if (result > 0) {
            url = "/loginpage";
            MessageRedirectTemplate.builder()
                    .request(request)
                    .msg("회원가입에 성공했습니다.")
                    .loc(url)
                    .response(response)
                    .build().forward();
        } else {
            url = "/signuppage?userType=" + type;
            MessageRedirectTemplate.builder()
                    .request(request)
                    .msg("회원가입에 실패했습니다.")
                    .error("error")
                    .loc(url)
                    .response(response)
                    .build().forward();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
