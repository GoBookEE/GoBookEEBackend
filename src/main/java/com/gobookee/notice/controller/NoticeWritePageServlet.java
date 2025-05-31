package com.gobookee.notice.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.notice.model.dto.Notice;
import com.gobookee.notice.model.service.NoticeService;

/**
 * Servlet implementation class NoticeWritePageServlet
 */
@WebServlet("/notice/writepage")
public class NoticeWritePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeWritePageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String mode = request.getParameter("mode");
		request.setAttribute("mode", mode);
		if("update".equals(mode)) {
			Long noticeSeq = Long.parseLong(request.getParameter("noticeSeq"));
			Notice notice = NoticeService.noticeService().noticeBySeq(noticeSeq);
			request.setAttribute("notice", notice);

		}
		
		request.getRequestDispatcher("/WEB-INF/views/notice/noticewriter.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
