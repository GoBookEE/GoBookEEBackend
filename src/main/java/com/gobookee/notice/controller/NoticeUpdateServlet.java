package com.gobookee.notice.controller;

import com.gobookee.notice.model.dto.Notice;
import com.gobookee.notice.model.service.NoticeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class NoticeUpdateServlet
 */
@WebServlet("/notice/update")
public class NoticeUpdateServlet extends HttpServlet {
	private NoticeService service = NoticeService.noticeService();
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String noticeTitle = request.getParameter("noticeTitle");
		String noticeContents = request.getParameter("noticeContents");
		Long noticeSeq = Long.parseLong(request.getParameter("noticeSeq"));
		Long noticeOrder = Long.parseLong(request.getParameter("noticeOrder"));
		
		Notice update = Notice.builder()
				.noticeTitle(noticeTitle)
				.noticeContents(noticeContents)
				.noticeSeq(noticeSeq)
				.noticeOrder(noticeOrder).build();
		
		int result = service.updateNotice(update);
		String msg, loc;
		if(result > 0) {
			msg = "공지사항 수정 성공";
			loc = "/notice/listpage";
		} else {
			msg = "공지사항 수정 실패";
			loc = "/notice/writepage?mode=update&noticeSeq=" +noticeSeq;
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
