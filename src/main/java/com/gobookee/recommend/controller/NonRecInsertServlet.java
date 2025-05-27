package com.gobookee.recommend.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.recommend.service.RecommendService;
import com.gobookee.users.model.dto.User;
import com.google.gson.Gson;

@WebServlet("/nonrecommend/insert")
public class NonRecInsertServlet extends HttpServlet {
	private RecommendService service = RecommendService.recommendService();
	private static final long serialVersionUID = 1L;

	public NonRecInsertServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long boardSeq = Long.parseLong(request.getParameter("boardSeq"));
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		Long userSeq = loginUser.getUserSeq();

		Gson gson = new Gson();
		response.setContentType("application/json;charset=UTF-8");
		Map<String, Object> result = new HashMap<>();
		try {
			result = service.toggleNonRecommend(userSeq, boardSeq);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", "처리 중 오류 발생");
			e.printStackTrace();
		}

		gson.toJson(result, response.getWriter());
	}

}
