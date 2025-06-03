package com.gobookee.mypage.controller;

import com.gobookee.common.AjaxPageBarTemplate;
import com.gobookee.users.model.dto.User;
import com.gobookee.users.service.UserService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/ranking/ajax")
public class RankingAjaxServlet extends HttpServlet {
    private UserService userService = UserService.userService();
    private static final long serialVersionUID = 1L;

    public RankingAjaxServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int cPage;
        try {
            cPage = Integer.parseInt(request.getParameter("cPage"));
        } catch (NumberFormatException e) {
            cPage = 1;
        }
        int numPerPage = 5;
        AjaxPageBarTemplate pb = new AjaxPageBarTemplate(cPage, numPerPage, userService.countSpeedRanking());

        List<User> list = userService.getSpeedRanking(cPage, numPerPage);

        User loginUser = (User) request.getSession().getAttribute("loginUser");
        User user = userService.searchUserById(loginUser.getUserId());
        request.getSession().setAttribute("loginUser", user);

        int myRank = userService.getUserRank(loginUser.getUserSeq());

        Gson gson = new Gson();
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("pageBar", pb.makePageBar(request));
        result.put("myRank", myRank);
        result.put("mySpeed", loginUser.getUserSpeed());

        gson.toJson(result, response.getWriter());

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
