package com.gobookee.review.service;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.commit;
import static com.gobookee.common.JDBCTemplate.getConnection;
import static com.gobookee.common.JDBCTemplate.rollback;

import static com.gobookee.review.model.dao.CommentsDAO.commentsDao;
import static com.gobookee.review.model.dao.ReviewDAO.reviewDao;

import java.sql.Connection;
import java.util.List;

import com.gobookee.book.model.dto.BookReviewResponse;
import com.gobookee.common.JDBCTemplate;
import com.gobookee.review.model.dao.CommentsDAO;
import com.gobookee.review.model.dao.ReviewDAO;
import com.gobookee.review.model.dto.CommentsViewResponse;
import com.gobookee.review.model.dto.Review;
import com.gobookee.review.model.dto.ReviewListResponse;
import com.gobookee.review.model.dto.ReviewViewResponse;

public class ReviewService {

	private static final ReviewService SERVICE = new ReviewService();

	private ReviewDAO dao = reviewDao();
	private CommentsDAO cdao = commentsDao();

	private ReviewService() {
	}

	public static ReviewService reviewService() {
		return SERVICE;
	}

	public List<ReviewListResponse> getAllReviews(int cPage, int numPerPage) {
		Connection conn = getConnection();
		List<ReviewListResponse> reviews = dao.getAllReviewsByDate(conn, cPage, numPerPage);
		close(conn);
		return reviews;
	}

	public List<ReviewListResponse> getAllReviewsByRec(int cPage, int numPerPage) {
		Connection conn = getConnection();
		List<ReviewListResponse> reviews = dao.getAllReviewsByRec(conn, cPage, numPerPage);
		close(conn);
		return reviews;
	}

	public int reviewCount() {
		Connection conn = getConnection();
		int totalData = dao.reviewCount(conn);
		close(conn);
		return totalData;
	}

	public List<ReviewListResponse> getBestReviews() {
		Connection conn = getConnection();
		List<ReviewListResponse> reviews = dao.getBestReviews(conn);
		close(conn);
		return reviews;
	}

	public ReviewViewResponse getReviewBySeq(Long reviewSeq) {
		Connection conn = getConnection();
		ReviewViewResponse review = dao.getReviewBySeq(conn, reviewSeq);

		List<CommentsViewResponse> comments = cdao.getReviewComments(conn, reviewSeq);
		if (comments != null) {
			review.setComments(comments);
		}
		close(conn);
		return review;
	}

	public int insertReview(Review review) {
		Connection conn = getConnection();
		int result = dao.insertReview(conn, review);
		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		return result;
	}

	public int updateReview(Review dto) {
		Connection conn = getConnection();
		int result = dao.updateReview(conn, dto);
		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		return result;
	}

	public int deleteReview(Long reviewSeq, Long userSeq) {
		Connection conn = getConnection();
		int result = dao.deleteReview(conn, reviewSeq, userSeq);
		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		return result;
	}

	public List<BookReviewResponse> searchBooks(String keyword) {
		Connection conn = getConnection();
		List<BookReviewResponse> result = dao.searchBooks(conn, keyword);
		JDBCTemplate.close(conn);
		return result;
	}

}
