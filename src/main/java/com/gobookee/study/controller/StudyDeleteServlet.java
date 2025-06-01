package com.gobookee.study.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.study.service.StudyService;

@WebServlet("/study/delete")
public class StudyDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final StudyService studyService = new StudyService();
    public StudyDeleteServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    Long studySeq = null;
	    try {
	        studySeq = Long.parseLong(request.getParameter("studySeq"));
	    } catch (NumberFormatException e) {
	        request.setAttribute("msg", "잘못된 요청입니다.");
	        request.setAttribute("loc", request.getContextPath() + "/study/listpage");
	        request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp").forward(request, response);
	        return;
	    }

	    int result = studyService.deleteStudy(studySeq);

	    if (result > 0) {
	        request.setAttribute("msg", "스터디 게시물이 성공적으로 삭제되었습니다.");
	        request.setAttribute("loc", "/study/listpage");
	    } else {
	        request.setAttribute("msg", "삭제에 실패했습니다.");
	        request.setAttribute("error", "error");
	        request.setAttribute("loc", "/study/view?seq=" + studySeq);
	        
	    }

	    request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp").forward(request, response);
	}

}
