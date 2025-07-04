package com.gobookee.users.controller;

import com.gobookee.users.model.dto.User;
import com.gobookee.users.service.UserService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Servlet implementation class AjaxuserIdduplicate
 */
@WebServlet("/ajax/useridduplicate")
public class AjaxUserIdDuplicate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxUserIdDuplicate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userId = request.getParameter("userId");
		
		User u = UserService.userService().searchUserById(userId);
		
		response.setContentType("application/json;charset=utf-8");
		new Gson().toJson(Map.of("result",u==null),response.getWriter());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
