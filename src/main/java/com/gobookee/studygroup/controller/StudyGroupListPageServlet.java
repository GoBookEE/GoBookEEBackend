package com.gobookee.studygroup.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.common.CommonPathTemplate;

/**
 * Servlet implementation class StudyGroupListPageServlet
 */
@WebServlet("/studygroup/listpage/active")
public class StudyGroupListPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public StudyGroupListPageServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(CommonPathTemplate.getViewPath("/studygroup/studygrouplist")).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
