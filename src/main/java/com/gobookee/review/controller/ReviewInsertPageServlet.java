package com.gobookee.review.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.common.CommonPathTemplate;

@WebServlet("/review/insertpage")
public class ReviewInsertPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ReviewInsertPageServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(CommonPathTemplate.getViewPath("/review/reviewInsert")).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
