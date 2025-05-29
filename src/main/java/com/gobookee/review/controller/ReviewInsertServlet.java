package com.gobookee.review.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.common.CommonPathTemplate;
import com.gobookee.review.model.dto.Review;
import com.gobookee.review.service.ReviewService;
import com.gobookee.users.model.dto.User;

@WebServlet("/review/insert")
public class ReviewInsertServlet extends HttpServlet {
	private ReviewService service = ReviewService.reviewService();
	private static final long serialVersionUID = 1L;

	public ReviewInsertServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long bookSeq = Long.parseLong(request.getParameter("bookSeq"));
		String title = request.getParameter("reviewTitle");
		String contents = request.getParameter("reviewContents");
		int rate = Integer.parseInt(request.getParameter("reviewRate"));

		Long userSeq = ((User) request.getSession().getAttribute("loginUser")).getUserSeq(); // 로그인 사용자

		Review review = Review.builder().bookSeq(bookSeq).userSeq(userSeq).reviewTitle(title).reviewContents(contents)
				.reviewRate(rate).build();

		int result = service.insertReview(review);
		String msg, loc;
		if (result > 0) {
			msg = "리뷰 등록 성공";
			loc = "/review/listpage";
		} else {
			msg = "리뷰 등록 실패";
			loc = "review/reviewInsert.jsp?error=fail";
		}
		request.setAttribute("msg", msg);
		request.setAttribute("loc", loc);

		request.getRequestDispatcher(CommonPathTemplate.getViewPath("/common/msg")).forward(request, response);

	}

}
