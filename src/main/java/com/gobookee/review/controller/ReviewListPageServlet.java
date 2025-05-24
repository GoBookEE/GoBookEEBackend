package com.gobookee.review.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.common.PageBarTemplate;
import com.gobookee.review.service.ReviewService;

@WebServlet("/review/listpage")
public class ReviewListPageServlet extends HttpServlet {
	private ReviewService service = ReviewService.reviewService();
	private static final long serialVersionUID = 1L;

	public ReviewListPageServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("list", service.getBestReviews());
		request.getRequestDispatcher("/WEB-INF/views/review/reviewList.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
