package com.gobookee.study.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.study.model.dto.StudyRequest;
import com.gobookee.study.service.StudyService;
import com.google.gson.Gson;

@WebServlet("/study/request/list")
public class StudyRequestListAjaxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudyService service = new StudyService();
    public StudyRequestListAjaxServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long studySeq = null;
        try {
            studySeq = Long.parseLong(request.getParameter("studySeq"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid input\"}");
            return;
        }

        List<StudyRequest> requestList = service.getStudyRequests(studySeq);
        Gson gson = new Gson();
        String json = gson.toJson(requestList);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
