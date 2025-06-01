package com.gobookee.review.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.book.model.dto.BookReviewResponse;
import com.gobookee.book.service.BookService;
import com.gobookee.common.CommonPathTemplate;

@WebServlet("/review/insertpage")
public class ReviewInsertPageServlet extends HttpServlet {
	private BookService service = BookService.bookService();
	private static final long serialVersionUID = 1L;

	public ReviewInsertPageServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bookSeqStr = request.getParameter("bookSeq");
		if (bookSeqStr != null) {
			try {
				long bookSeq = Long.parseLong(bookSeqStr);
				BookReviewResponse book = service.getBookForReview(bookSeq); // Book 객체를 가져온다고 가정
				request.setAttribute("selectedBook", book);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		request.getRequestDispatcher(CommonPathTemplate.getViewPath("/review/reviewInsert")).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
