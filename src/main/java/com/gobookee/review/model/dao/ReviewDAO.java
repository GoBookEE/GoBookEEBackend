package com.gobookee.review.model.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.gobookee.book.model.dto.BookReviewResponse;
import com.gobookee.common.JDBCTemplate;
import com.gobookee.review.model.dto.Review;
import com.gobookee.review.model.dto.ReviewListResponse;
import com.gobookee.review.model.dto.ReviewViewResponse;

public class ReviewDAO {
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Properties sqlProp = new Properties();

	private static ReviewDAO dao;

	private ReviewDAO() {
		String path = ReviewDAO.class.getResource("/config/review-sql.properties").getPath();
		try (FileReader fr = new FileReader(path)) {
			sqlProp.load(fr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ReviewDAO reviewDao() {
		if (dao == null) {
			dao = new ReviewDAO();
		}
		return dao;
	}

	public List<ReviewListResponse> getAllReviewsByDate(Connection conn, int cPage, int numPerPage) {
		pstmt = null;
		rs = null;
		List<ReviewListResponse> reviews = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getAllReviews"));
			pstmt.setInt(1, (cPage - 1) * numPerPage + 1);
			pstmt.setInt(2, cPage * numPerPage);
			rs = pstmt.executeQuery();
			while (rs.next())
				reviews.add(getReviewListResponse(rs));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return reviews;
	}

	public List<ReviewListResponse> getAllReviewsByRec(Connection conn, int cPage, int numPerPage) {
		pstmt = null;
		rs = null;
		List<ReviewListResponse> reviews = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getAllReviewsByRec"));
			pstmt.setInt(1, (cPage - 1) * numPerPage + 1);
			pstmt.setInt(2, cPage * numPerPage);
			rs = pstmt.executeQuery();
			while (rs.next())
				reviews.add(getReviewListResponse(rs));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return reviews;
	}

	public List<ReviewListResponse> getAllReviewsByUser(Connection conn, Long userSeq, int cPage, int numPerPage) {
		pstmt = null;
		rs = null;
		List<ReviewListResponse> reviews = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getAllReviewsByUser"));
			pstmt.setLong(1, userSeq);
			pstmt.setInt(2, (cPage - 1) * numPerPage + 1);
			pstmt.setInt(3, cPage * numPerPage);
			rs = pstmt.executeQuery();
			while (rs.next())
				reviews.add(getReviewListResponse(rs));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return reviews;
	}

	public int countByUser(Connection conn, Long userSeq) {
		pstmt = null;
		rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("countByUser"));
			pstmt.setLong(1, userSeq);
			rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public List<ReviewListResponse> getAllReviewsRecByUser(Connection conn, Long userSeq, int cPage, int numPerPage) {
		pstmt = null;
		rs = null;
		List<ReviewListResponse> reviews = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getAllReviewsRecByUser"));
			pstmt.setLong(1, userSeq);
			pstmt.setInt(2, (cPage - 1) * numPerPage + 1);
			pstmt.setInt(3, cPage * numPerPage);
			rs = pstmt.executeQuery();
			while (rs.next())
				reviews.add(getReviewListResponse(rs));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return reviews;
	}

	public int countReviewsRecByUser(Connection conn, Long userSeq) {
		pstmt = null;
		rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("countReviewsRecByUser"));
			pstmt.setLong(1, userSeq);
			rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public int reviewCount(Connection conn) {
		Statement stmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlProp.getProperty("reviewCount"));
			if (rs.next())
				result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(stmt);

		}
		return result;
	}

	public List<ReviewListResponse> getBestReviews(Connection conn) {
		pstmt = null;
		rs = null;
		List<ReviewListResponse> reviews = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getBestReviews"));
			rs = pstmt.executeQuery();
			while (rs.next())
				reviews.add(getReviewListResponse(rs));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return reviews;
	}

	public ReviewViewResponse getReviewBySeq(Connection conn, Long reviewSeq) {
		pstmt = null;
		rs = null;
		ReviewViewResponse dto = new ReviewViewResponse();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getReviewBySeq"));
			pstmt.setLong(1, reviewSeq);
			rs = pstmt.executeQuery();
			if (rs.next())
				dto = getReviewViewResponse(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return dto;
	}

	public int insertReview(Connection conn, Review dto) {
		pstmt = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("insertReview"));
			pstmt.setString(1, dto.getReviewTitle());
			pstmt.setString(2, dto.getReviewContents());
			pstmt.setInt(3, dto.getReviewRate());
			pstmt.setLong(4, dto.getUserSeq());
			pstmt.setLong(5, dto.getBookSeq());
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public int updateReview(Connection conn, Review dto) {
		pstmt = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("updateReview"));
			pstmt.setString(1, dto.getReviewTitle());
			pstmt.setString(2, dto.getReviewContents());
			pstmt.setInt(3, dto.getReviewRate());
			pstmt.setLong(4, dto.getBookSeq());
			pstmt.setLong(5, dto.getReviewSeq());
			pstmt.setLong(6, dto.getUserSeq());
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public int deleteReview(Connection conn, Long reviewSeq, Long userSeq) {
		pstmt = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("deleteReview"));
			pstmt.setLong(1, reviewSeq);
			pstmt.setLong(2, userSeq);
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public List<BookReviewResponse> searchBooks(Connection conn, String keyword) {
		pstmt = null;
		rs = null;
		List<BookReviewResponse> list = new ArrayList<>();

		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("searchBooks"));
			String search = "%" + keyword + "%";
			pstmt.setString(1, search);
			pstmt.setString(2, search);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				BookReviewResponse book = BookReviewResponse.builder().bookSeq(rs.getLong("BOOK_SEQ"))
						.bookTitle(rs.getString("BOOK_TITLE")).bookAuthor(rs.getString("BOOK_AUTHOR"))
						.bookPublisher(rs.getString("BOOK_PUBLISHER")).bookPubDate(rs.getString("BOOK_PUBDATE"))
						.bookCover(rs.getString("BOOK_COVER")).build();
				list.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}

	private Review getReviewDTO(ResultSet rs) throws SQLException {
		Review dto = Review.builder().reviewSeq(rs.getLong("REVIEW_SEQ")).reviewTitle(rs.getString("REVIEW_TITLE"))
				.reviewContents(rs.getString("REVIEW_CONTENTS")).reviewCreateTime(rs.getTimestamp("REVIEW_CREATE_TIME"))
				.reviewRate(rs.getInt("REVIEW_RATE")).reviewDeleteTime(rs.getTimestamp("REVIEW_DELETE_TIME"))
				.reviewEditTime(rs.getTimestamp("REVIEW_EDIT_TIME")).userSeq(rs.getLong("USER_SEQ"))
				.reviewIsPublic(rs.getString("REVIEW_IS_PUBLIC").charAt(0)).build();
		return dto;
	}

	public ReviewListResponse getReviewListResponse(ResultSet rs) throws SQLException {
		return ReviewListResponse.builder().reviewSeq(rs.getLong("REVIEW_SEQ"))
				.reviewTitle(rs.getString("REVIEW_TITLE")).reviewContents(rs.getString("REVIEW_CONTENTS"))
				.reviewCreateTime(rs.getTimestamp("REVIEW_CREATE_TIME")).reviewRate(rs.getInt("REVIEW_RATE"))
				.reviewEditTime(rs.getTimestamp("REVIEW_EDIT_TIME")).bookTitle(rs.getString("BOOK_TITLE"))
				.bookCover(rs.getString("BOOK_COVER")).recommendCount(rs.getInt("RECOMMEND_COUNT"))
				.commentsCount(rs.getInt("COMMENTS_COUNT")).build();
	}

	public ReviewViewResponse getReviewViewResponse(ResultSet rs) throws SQLException {
		return ReviewViewResponse.builder().reviewSeq(rs.getLong("REVIEW_SEQ"))
				.reviewTitle(rs.getString("REVIEW_TITLE")).reviewContents(rs.getString("REVIEW_CONTENTS"))
				.reviewCreateTime(rs.getTimestamp("REVIEW_CREATE_TIME")).reviewRate(rs.getInt("REVIEW_RATE"))
				.reviewEditTime(rs.getTimestamp("REVIEW_EDIT_TIME")).comments(new ArrayList<>())
				.bookTitle(rs.getString("BOOK_TITLE")).bookAuthor(rs.getString("BOOK_AUTHOR"))
				.bookPublisher(rs.getString("BOOK_PUBLISHER")).bookCover(rs.getString("BOOK_COVER"))
				.bookReviewCount(rs.getInt("BOOK_REVIEW_COUNT")).bookAvgRate(rs.getDouble("BOOK_AVG_RATE"))
				.recommendCount(rs.getInt("RECOMMEND_COUNT")).nonRecommendCount(rs.getInt("NON_RECOMMEND_COUNT"))
				.userNickName(rs.getString("USER_NICKNAME")).userProfile(rs.getString("USER_PROFILE"))
				.bookDescription(rs.getString("BOOK_DESCRIPTION")).userSeq(rs.getLong("USER_SEQ"))
				.bookSeq(rs.getLong("BOOK_SEQ")).userSpeed(rs.getLong("USER_SPEED")).build();
	}

}
