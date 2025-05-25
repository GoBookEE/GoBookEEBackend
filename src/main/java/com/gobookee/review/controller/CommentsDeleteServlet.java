package com.gobookee.review.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.common.CommonPathTemplate;
import com.gobookee.review.service.CommentsService;

@WebServlet("/review/deletecomment")
public class CommentsDeleteServlet extends HttpServlet {
	private CommentsService service = CommentsService.commentsService();
	private static final long serialVersionUID = 1L;

	public CommentsDeleteServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long commentSeq = Long.parseLong(request.getParameter("commentSeq"));
		// long userSeq = ((User)
		// request.getSession().getAttribute("loginUser")).getUserSeq();
		long reviewSeq = Long.parseLong(request.getParameter("reviewSeq")); // redirect용
		// int result = service.deleteComment(commentSeq, userSeq);
//      String msg, loc;
//		if(result>0) {
//			msg="댓글 삭제 성공";
//			loc="/review/reviewseq?seq=" + reviewSeq;
//		}else {
//			msg="댓글 삭제 실패";
//			loc="/review/reviewseq?seq=" + reviewSeq; 
//		}
		// request.setAttribute("msg", msg);
		// request.setAttribute("loc", loc);

		request.getRequestDispatcher(CommonPathTemplate.getViewPath("/common/msg")).forward(request, response);

	}

}
