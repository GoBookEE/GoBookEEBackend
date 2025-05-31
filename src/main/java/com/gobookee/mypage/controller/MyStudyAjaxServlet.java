package com.gobookee.mypage.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.common.AjaxPageBarTemplate;
import com.gobookee.mypage.model.dto.MyStudy;
import com.gobookee.mypage.service.RecStudyService;
import com.gobookee.study.model.dto.StudyList;
import com.gobookee.users.model.dto.User;
import com.google.gson.Gson;

@WebServlet("/mystudy/ajax")
public class MyStudyAjaxServlet extends HttpServlet {
	private RecStudyService studyService = RecStudyService.recStudyService();
	private static final long serialVersionUID = 1L;

	public MyStudyAjaxServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		Long userSeq = loginUser.getUserSeq();
		String tab = request.getParameter("tab"); // applied | created
		String status = request.getParameter("status"); // upcoming | completed
		int cPage = Integer.parseInt(request.getParameter("cPage"));
		int numPerPage = 5;

		MyStudy mystudy = MyStudy.builder().userSeq(userSeq).status(status).cPage(cPage).build();

		List<StudyList> list = new ArrayList<>();
		int totalCount = 0;

		if ("applied".equals(tab)) {
			list = studyService.getAppliedByStatus(mystudy);
			totalCount = studyService.countAppliedByStatus(mystudy);
		} else if ("created".equals(tab)) {
			list = studyService.getCreatedByStatus(mystudy);
			totalCount = studyService.countCreatedByStatus(mystudy);
		}

		AjaxPageBarTemplate pageBar = new AjaxPageBarTemplate(cPage, numPerPage, totalCount);

		Map<String, Object> result = new HashMap<>();
		result.put("list", list);
		result.put("pageBar", pageBar.makePageBar(request));

		response.setContentType("application/json;charset=UTF-8");
		new Gson().toJson(result, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
