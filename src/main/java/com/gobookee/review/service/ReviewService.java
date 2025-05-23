package com.gobookee.review.service;

import com.gobookee.review.model.dao.ReviewDAO;
import com.gobookee.review.model.dto.Review;

import java.sql.Connection;
import java.util.List;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.getConnection;
import static com.gobookee.review.model.dao.ReviewDAO.reviewDao;

public class ReviewService {

    private static final ReviewService SERVICE = new ReviewService();

    private ReviewDAO dao = reviewDao();

    private ReviewService() {
    }

    public static ReviewService reviewService() {
        return SERVICE;
    }

    public List<Review> getAllReviews(int cPage, int numPerpage) {
        Connection conn = getConnection();
        List<Review> reviews = dao.getAllReviewsByDate(conn, cPage, numPerpage);
        close(conn);
        return reviews;
    }

    public List<Review> getAllReviewsByRec(int cPage, int numPerpage) {
        Connection conn = getConnection();
        List<Review> reviews = dao.getAllReviewsByRec(conn, cPage, numPerpage);
        close(conn);
        return reviews;
    }

    public int reviewCount() {
        Connection conn = getConnection();
        int totalData = dao.reviewCount(conn);
        close(conn);
        return totalData;
    }

    public List<Review> getBestReviews() {
        Connection conn = getConnection();
        List<Review> reviews = dao.getBestReviews(conn);
        close(conn);
        return reviews;
    }

    public Review getReviewBySeq(Long reviewSeq) {
        Connection conn = getConnection();
        Review review = dao.getReviewBySeq(conn, reviewSeq);
        close(conn);
        return review;
    }

    public int getRecommendCount(Integer reviewSeq) {
        Connection conn = getConnection();
        int count = dao.getRecommendCount(conn, reviewSeq);
        close(conn);
        return count;
    }

}
