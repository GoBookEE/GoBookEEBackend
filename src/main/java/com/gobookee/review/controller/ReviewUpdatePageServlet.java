package com.gobookee.review.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.common.CommonPathTemplate;
import com.gobookee.review.model.dto.ReviewViewResponse;
import com.gobookee.review.service.ReviewService;

@WebServlet("/review/updatepage")
public class ReviewUpdatePageServlet extends HttpServlet {
	private ReviewService service = ReviewService.reviewService();
	private static final long serialVersionUID = 1L;

	public ReviewUpdatePageServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long reviewSeq = Long.parseLong(request.getParameter("reviewSeq"));
		ReviewViewResponse review = service.getReviewBySeq(reviewSeq);

		request.setAttribute("review", review);
		request.setAttribute("mode", "update");

		request.getRequestDispatcher(CommonPathTemplate.getViewPath("/review/reviewInsert")).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
