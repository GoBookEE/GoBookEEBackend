package com.gobookee.mypage.controller;

import com.gobookee.users.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ranking/speed")
public class RankingListServlet extends HttpServlet {
	private UserService userService = UserService.userService();
	private static final long serialVersionUID = 1L;

	public RankingListServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//List<User> rankingList = userService.getSpeedRanking();
		//request.setAttribute("rankingList", rankingList);
		request.getRequestDispatcher("/WEB-INF/views/mypage/ranking.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
