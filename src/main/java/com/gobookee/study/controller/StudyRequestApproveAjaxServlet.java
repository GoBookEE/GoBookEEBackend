package com.gobookee.study.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.study.service.StudyService;

@WebServlet("/study/approve")
public class StudyRequestApproveAjaxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public StudyRequestApproveAjaxServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    try {
	        Long studySeq = Long.parseLong(request.getParameter("studySeq"));
	        Long userSeq = Long.parseLong(request.getParameter("userSeq"));
	        String status = request.getParameter("status"); // "Y" or "R"

	        int result = new StudyService().updateRequestConfirm(userSeq, studySeq, status);
	        System.out.println(result);
	        response.setContentType("application/json;charset=UTF-8");
	        response.getWriter().write("{\"success\":" + (result > 0) + "}");
	    } catch (Exception e) {
	        response.setContentType("application/json;charset=UTF-8");
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("{\"success\":false, \"error\":\"" + e.getMessage() + "\"}");
	        e.printStackTrace();
	    }
	}

}
