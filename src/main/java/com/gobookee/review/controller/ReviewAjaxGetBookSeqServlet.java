package com.gobookee.review.controller;

import com.gobookee.book.service.BookService;
import com.gobookee.common.AjaxPageBarTemplate;
import com.gobookee.review.model.dto.ReviewBookSeqResponse;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/review/ajax/reviewbookseq")
public class ReviewAjaxGetBookSeqServlet extends HttpServlet {
	private BookService service = BookService.bookService();
	private static final long serialVersionUID = 1L;

	public ReviewAjaxGetBookSeqServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long bookSeq = Long.valueOf(request.getParameter("bookSeq"));
		String orderBy = request.getParameter("orderBy");

		int cPage,userSeq;

		try {
			userSeq = Integer.parseInt(request.getParameter("userSeq"));
		} catch (NumberFormatException e) {
			userSeq = 0;
		}

		String o = "";
		switch (orderBy) {
			case "CREATD": o = "  R.REVIEW_CREATE_TIME DESC"; break;
			case "CREATA": o = "  R.REVIEW_CREATE_TIME ASC"; break;
			case "RECD": o = " RECOMMEND_COUNT DESC"; break;
			case "DISD": o = " NON_RECOMMEND_COUNT DESC"; break;
			default: o = " R.REVIEW_CREATE_TIME DESC"; break;
		}

		try {
			cPage = Integer.parseInt(request.getParameter("cPage"));
		} catch (NumberFormatException e) {
			cPage = 1;
		}
		int numPerPage = 5;

		Gson gson = new Gson();
		response.setContentType("application/json;charset=UTF-8");
		List<ReviewBookSeqResponse> list = service.getReviewByBookSeq(userSeq, bookSeq, o, cPage, numPerPage);

		int ListCount = service.getReviewByBookSeqCount(Integer.parseInt(bookSeq.toString()));
		AjaxPageBarTemplate pb = new AjaxPageBarTemplate(cPage, numPerPage, ListCount);
		Map<String, Object> result = new HashMap<>();
		result.put("reviews", list);
		result.put("pageBar", pb.makePageBar(request));

		gson.toJson(result, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
