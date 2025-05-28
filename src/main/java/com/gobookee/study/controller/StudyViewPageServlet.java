package com.gobookee.study.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.common.CommonPathTemplate;
import com.gobookee.study.service.StudyService;

@WebServlet("/study/view")
public class StudyViewPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private StudyService service = StudyService.studyService();   
	
    public StudyViewPageServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long seq = Long.valueOf(request.getParameter("seq"));
		System.out.println(seq);
		System.out.println(service.getStudyView(seq));
		System.out.println(service.getStudyViewUser(seq));
		request.setAttribute("studyView", service.getStudyView(seq));
		request.setAttribute("studyViewUser", service.getStudyViewUser(seq));
		request.getRequestDispatcher(CommonPathTemplate.getViewPath("/study/studyview")).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
