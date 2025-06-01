package com.gobookee.review.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.common.CommonPathTemplate;
import com.gobookee.review.model.dto.Comments;
import com.gobookee.review.service.CommentsService;
import com.gobookee.users.model.dto.User;

@WebServlet("/review/insertcomment")
public class CommentsInsertServlet extends HttpServlet {
	private CommentsService service = CommentsService.commentsService();
	private static final long serialVersionUID = 1L;

	public CommentsInsertServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String content = request.getParameter("commentContent");
		long reviewSeq = Long.parseLong(request.getParameter("reviewSeq"));
		long userSeq = ((User) request.getSession().getAttribute("loginUser")).getUserSeq(); // 예시
		long parentSeq = 0;

		if (request.getParameter("parentCommentSeq") != null) {
			parentSeq = Long.parseLong(request.getParameter("parentCommentSeq"));
		}

		Comments dto = Comments.builder().commentsContents(content).commentsParentSeq(parentSeq).userSeq(userSeq)
				.reviewSeq(reviewSeq).build();

		int result = service.insertComment(dto);
		String msg, loc;
		if (result > 0) {
			msg = "댓글 등록 성공";
			loc = "/review/view?seq=" + reviewSeq;
			request.setAttribute("error", "success");
		} else {
			msg = "댓글 등록 실패";
			loc = "/review/view?seq=" + reviewSeq;
			request.setAttribute("error", "error");
		}
		request.setAttribute("msg", msg);
		request.setAttribute("loc", loc);

		request.getRequestDispatcher(CommonPathTemplate.getViewPath("/common/msg")).forward(request, response);
	}

}
