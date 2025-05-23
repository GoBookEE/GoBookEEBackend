package com.gobookee.review.service;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.commit;
import static com.gobookee.common.JDBCTemplate.getConnection;
import static com.gobookee.common.JDBCTemplate.rollback;
import static com.gobookee.review.model.dao.ReviewDAO.reviewDao;

import java.sql.Connection;
import java.util.List;

import com.gobookee.review.model.dao.ReviewDAO;
import com.gobookee.review.model.dto.Review;

public class ReviewService {

	private static final ReviewService SERVICE=new ReviewService();

	private ReviewDAO dao=reviewDao();
	
	private ReviewService() {}
	public static ReviewService reviewService() {
		return SERVICE;
	}
	
	public List<Review> getAllReviews(int cPage, int numPerpage){
		Connection conn= getConnection();
		List<Review> reviews=dao.getAllReviewsByDate(conn,cPage,numPerpage);
		close(conn);
		return reviews;
	}
	
	
	
}
