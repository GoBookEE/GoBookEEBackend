package com.gobookee.study.controller;

import com.gobookee.common.JsonConvertTemplate;
import com.gobookee.study.model.dto.SearchStudyResponse;
import com.gobookee.study.service.StudyService;
import com.gobookee.users.model.dto.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "searchStudyServlet", urlPatterns = "/study/searchstudy")
public class SearchStudyAjaxHandlerServlet extends HttpServlet {
    private StudyService studyService = StudyService.studyService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loginMember = (User) request.getSession().getAttribute("loginUser");
        List<SearchStudyResponse> studyList = studyService.getStudyListByHostUserSeq(loginMember.getUserSeq());
        JsonConvertTemplate.toJson(studyList, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
