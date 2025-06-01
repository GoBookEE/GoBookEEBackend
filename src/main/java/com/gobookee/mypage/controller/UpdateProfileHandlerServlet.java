package com.gobookee.mypage.controller;

import com.gobookee.common.AjaxFileUploadTemplate;
import com.gobookee.common.MessageRedirectTemplate;
import com.gobookee.common.PasswordEncoder;
import com.gobookee.common.enums.FileType;
import com.gobookee.users.model.dto.User;
import com.gobookee.users.model.dto.UserUpdateProfile;
import com.gobookee.users.service.UserService;
import com.oreilly.servlet.MultipartRequest;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "updateProfileHandlerServlet", value = "/mypage/updateprofile")
public class UpdateProfileHandlerServlet extends HttpServlet {
    private UserService userService = UserService.userService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        MultipartRequest mr = AjaxFileUploadTemplate.uploadFiles(request, FileType.USER, 1024 * 1024 * 100);
        String profileImage = null;
        if (mr.getFilesystemName("profileImage") != null) {
            profileImage = mr.getFilesystemName("profileImage");
        }

        String userNickname = mr.getParameter("userNickname");
        String newPassword = mr.getParameter("newPassword");
        String userPhone = mr.getParameter("userPhone");
        Long userSeq = loginUser.getUserSeq();
        PasswordEncoder pe = new PasswordEncoder(request);
        String password = pe.getSHA512(newPassword);


        UserUpdateProfile updateUser = UserUpdateProfile.builder()
                .userSeq(userSeq)
                .userNickname(userNickname)
                .userPwd(password)
                .userPhone(userPhone)
                .profileImage(profileImage)
                .userId(loginUser.getUserId())
                .userEmail(loginUser.getUserEmail())
                .build();

        boolean result = userService.updateProfile(updateUser);
        if (result) {
            session.setAttribute("loginUser",
                    User.builder()
                            .userSeq(userSeq)
                            .userNickName(userNickname)
                            .userPwd(updateUser.getUserPwd())
                            .userPhone(userPhone)
                            .userProfile(profileImage)
                            .userId(updateUser.getUserId())
                            .userEmail(updateUser.getUserEmail())
                            .userType(loginUser.getUserType())
                            .build());
            MessageRedirectTemplate.builder()
                    .request(request)
                    .msg("프로필 수정에 성공했습니다.")
                    .loc("/mypage/updateprofilepage")
                    .response(response)
                    .build().forward();
        } else {
            MessageRedirectTemplate.builder()
                    .request(request)
                    .msg("프로필 수정에 실패했습니다.")
                    .loc("/mypage/updateprofilepage")
                    .error("error")
                    .response(response)
                    .build().forward();
        }
    }
}
