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
 * Servlet implementation class NoticeWriteServlet
 */
@WebServlet("/notice/write")
public class NoticeWriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private NoticeService service = NoticeService.noticeService();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeWriteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String noticeTitle = request.getParameter("noticeTitle");
		String noticeContents = request.getParameter("noticeContents");
		Long noticeOrder = Long.parseLong(request.getParameter("noticeOrder"));
		
		Notice write = Notice.builder()
				.noticeTitle(noticeTitle)
				.noticeContents(noticeContents)
				.noticeOrder(noticeOrder)
				.build();
		
		int result = service.writeNotice(write);
		String msg,loc;
		if(result>0) {
			msg = "공지사항 작성 성공";
			loc = "/notice/listpage";
		} else {
			msg = "공지사항 작성 실패";
			loc = "/notice/writepage?mode=update";
		}
		request.setAttribute("msg", msg);
		request.setAttribute("loc", loc);
		
		request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp").forward(request, response);
	}
		
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
