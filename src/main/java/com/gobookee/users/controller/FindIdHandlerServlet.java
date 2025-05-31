package com.gobookee.users.controller;

import com.gobookee.common.JsonConvertTemplate;
import com.gobookee.users.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "findIdHandlerServlet", value = "/findid")
public class FindIdHandlerServlet extends HttpServlet {
    private UserService userService = UserService.userService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> idList = userService.searchUserIdByEmail(request.getParameter("email"));
        JsonConvertTemplate.toJson(idList, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
