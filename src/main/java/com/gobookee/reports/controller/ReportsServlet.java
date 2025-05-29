package com.gobookee.reports.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.reports.service.ReportsService;
import com.gobookee.users.model.dto.User;
import com.google.gson.Gson;

@WebServlet("/reports/insert")
public class ReportsServlet extends HttpServlet {
	private ReportsService service = ReportsService.reportsService();
	private static final long serialVersionUID = 1L;

	public ReportsServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long userSeq = ((User) request.getSession().getAttribute("loginUser")).getUserSeq();
		Long boardSeq = Long.valueOf(request.getParameter("boardSeq"));
		String reason = request.getParameter("reason");
		String boardType = request.getParameter("boardType");

		Gson gson = new Gson();
		response.setContentType("application/json;charset=UTF-8");
		Map<String, Object> result = new HashMap<>();

		boolean alreadyReported = service.hasReported(userSeq, boardSeq);
		if (alreadyReported) {
			result.put("success", false);
			result.put("message", "이미 신고하셨습니다.");
		} else {
			int insert = service.insertReport(userSeq, boardSeq, reason, boardType);
			if (insert > 0) {
				result.put("success", true);
			} else {
				result.put("success", false);
				result.put("message", "신고 처리 실패");
			}
		}

		gson.toJson(result, response.getWriter());
	}

}
