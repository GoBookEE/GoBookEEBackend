package com.gobookee.review.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.book.model.dto.BookReviewResponse;
import com.gobookee.review.service.ReviewService;
import com.google.gson.Gson;

@WebServlet("/book/search")
public class BookSearchServlet extends HttpServlet {
	private ReviewService service = ReviewService.reviewService();
	private static final long serialVersionUID = 1L;

	public BookSearchServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String keyword = request.getParameter("keyword");

		List<BookReviewResponse> books = service.searchBooks(keyword);
		response.setContentType("application/json;charset=UTF-8");

		Gson gson = new Gson();
		response.getWriter().write(gson.toJson(books));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
