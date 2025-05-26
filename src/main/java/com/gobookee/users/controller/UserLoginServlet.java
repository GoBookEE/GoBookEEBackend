package com.gobookee.users.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gobookee.users.model.dto.User;
import com.gobookee.users.service.UserService;

/**
 * Servlet implementation class UserLoginServlet
 */
@WebServlet(name="userLoginServlet",
		urlPatterns = {"/login"})
public class UserLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		
		String saveId = request.getParameter("saveId");
		
		if(saveId!=null) {
			Cookie c = new Cookie("saveId",userId);
			c.setMaxAge(60*60*24*7);
			c.setPath("/");
			response.addCookie(c);
		}else {
			Cookie c = new Cookie("saveId",userId);
			c.setMaxAge(0);
			c.setPath("/");
			response.addCookie(c);
		}
		
		User u = UserService.userService().searchUserById(userId);
		
		if(u!=null && u.getUserPwd().equals(userPwd)) {
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", u);
			System.out.println("로그인성공");
			System.out.println(request.getContextPath());
			response.sendRedirect(request.getContextPath());
		}else {
			request.setAttribute("msg", "아이디나 패스워드가 일치하지 않습니다.");
			request.setAttribute("loc", "/loginpage");
			request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
