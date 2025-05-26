package com.gobookee.users.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserInsertServlet
 */
@WebServlet(name = "userInsertServlet", urlPatterns = { "/signup" })
public class UserInsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserInsertServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userGender = request.getParameter("userGender");
		String userNickname = request.getParameter("userNickname");
		String address = request.getParameter("userAddress");
		String userAddressDetail = request.getParameter("userAddressDetail");
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		String userPwdCheck = request.getParameter("userPwdCheck");
		String userEmail =request.getParameter("userEmail");
		int userEmailCheckNum = Integer.parseInt(request.getParameter("userEmailCheckNum"));
		String userPhone = request.getParameter("userPhone");
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
