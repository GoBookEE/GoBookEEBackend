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
import com.gobookee.place.model.dao.PlaceDao;
import com.gobookee.place.service.PlaceService;
import com.gobookee.review.service.CommentsService;
import com.gobookee.review.service.ReviewService;
import com.gobookee.users.model.dto.User;
import com.google.gson.Gson;

@WebServlet("/myboard/ajax")
public class MyboardAjaxServlet extends HttpServlet {
	private ReviewService reviewService = ReviewService.reviewService();
	private CommentsService commentsService = CommentsService.commentsService();
	private PlaceService placeService = PlaceService.placeService();
	private static final long serialVersionUID = 1L;
       
    public MyboardAjaxServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		Long userSeq = loginUser.getUserSeq();
        String tab = request.getParameter("tab");
        int cPage = Integer.parseInt(request.getParameter("cPage"));
        int numPerPage = 5;
        
        List<?> list = new ArrayList<>();
        int totalCount = 0;

        switch (tab) {
            case "review":
                list = reviewService.getAllReviewsByUser(userSeq, cPage, numPerPage);
                totalCount = reviewService.countByUser(userSeq);
                break;
            case "comment":
                list = commentsService.getAllCommentsByUser(userSeq, cPage, numPerPage);
                totalCount = commentsService.countByUser(userSeq);
                break;
            case "place":
                list = placeService.getAllPlaceByUser(userSeq, cPage, numPerPage);
                totalCount = placeService.countByUser(userSeq);
                break;
        }
		
        AjaxPageBarTemplate pb = new AjaxPageBarTemplate(cPage, numPerPage, totalCount);
        
        Gson gson = new Gson();
		response.setContentType("application/json;charset=UTF-8");

		Map<String, Object> result = new HashMap<>();
		result.put("list", list);
		result.put("pageBar", pb.makePageBar(request));

		gson.toJson(result, response.getWriter());
        
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
