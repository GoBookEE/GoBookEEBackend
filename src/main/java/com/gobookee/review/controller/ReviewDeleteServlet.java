package com.gobookee.review.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.review.model.dto.ReviewViewResponse;
import com.gobookee.review.service.ReviewService;
import com.gobookee.users.model.dto.User;

@WebServlet("/review/delete")
public class ReviewDeleteServlet extends HttpServlet {
	private ReviewService service = ReviewService.reviewService();
	private static final long serialVersionUID = 1L;

	public ReviewDeleteServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long reviewSeq = Long.parseLong(request.getParameter("reviewSeq"));
		Long loginUserSeq = ((User) request.getSession().getAttribute("loginUser")).getUserSeq();

		ReviewViewResponse review = service.getReviewBySeq(reviewSeq);

		if (review == null || !review.getUserSeq().equals(loginUserSeq)) {
			request.setAttribute("msg", "삭제 권한이 없습니다.");
			request.setAttribute("loc", "/review/listpage");
			request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp").forward(request, response);
			return;
		}

		int result = service.deleteReview(reviewSeq, loginUserSeq);
		request.setAttribute("msg", result > 0 ? "삭제 완료" : "삭제 실패");
		request.setAttribute("loc", "/review/listpage");
		request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp").forward(request, response);
	}

}
