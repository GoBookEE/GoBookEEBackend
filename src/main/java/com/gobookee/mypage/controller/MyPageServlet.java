package com.gobookee.mypage.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.users.model.dto.User;
import com.gobookee.users.model.dto.UserType;

import javax.servlet.http.*;

import com.gobookee.review.service.ReviewService;
import com.gobookee.review.service.CommentsService;
import com.gobookee.mypage.model.dto.MyStudy;
import com.gobookee.mypage.service.RecStudyService;
import com.gobookee.place.service.PlaceService;
import com.gobookee.users.model.dto.UserType;

/**
 * Servlet implementation class MyPageServlet
 */
@WebServlet("/mypage")
public class MyPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ReviewService reviewService = ReviewService.reviewService();
	private CommentsService commentsService = CommentsService.commentsService();
	private PlaceService placeService = PlaceService.placeService();
	private RecStudyService studyService = RecStudyService.recStudyService();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyPageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		   User loginUser = (User) request.getSession().getAttribute("loginUser");

		    if (loginUser == null) {
		        response.sendRedirect(request.getContextPath() + "/login");
		        return;
		    }

		    Long userSeq = loginUser.getUserSeq();
		    UserType userType = loginUser.getUserType(); 

		    int reviewCount = reviewService.countByUser(userSeq);
		    int commentCount = commentsService.countByUser(userSeq);
		    int placeCount = 0;

		    if (userType == UserType.OWNER) {
		        placeCount = placeService.countByUser(userSeq);
		    }

		    int totalWriteCount = reviewCount + commentCount + placeCount;

		    request.setAttribute("totalWriteCount", totalWriteCount);
		    
		    int review = reviewService.countReviewsRecByUser(userSeq);
			int comment = commentsService.countCommentsRecByUser(userSeq);
			int place = placeService.placeCountRecByUser(userSeq);
			int study = studyService.countStudiesRecByUser(userSeq);
			
			int recommendedCount = review + comment + place + study;
			
			request.setAttribute("recommendedCount", recommendedCount);
			
			
			String tab = request.getParameter("tab"); // applied | created
			String status = request.getParameter("status"); // upcoming | completed
			if (status == null || (!status.equals("upcoming") && !status.equals("completed"))) {
				status = "upcoming";
			}

			MyStudy mystudy = MyStudy.builder()
				.userSeq(userSeq)
				.status(status)
				.build();

			int appliedCount = studyService.countAppliedByStatus(mystudy);
			int createdCount = studyService.countCreatedByStatus(mystudy);
			int myStudyTotalCount = appliedCount + createdCount;

			request.setAttribute("myStudyTotalCount", myStudyTotalCount);
			
		
		request.getRequestDispatcher("/WEB-INF/views/mypage/myPage.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
