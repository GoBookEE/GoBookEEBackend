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

import com.gobookee.common.JDBCTemplate;
import com.gobookee.review.model.dto.Review;

public class ReviewDAO {
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Properties sqlProp = new Properties();

	private static ReviewDAO dao;

	private ReviewDAO() {
		String path = ReviewDAO.class.getResource("/config/review_sql.properties").getPath();
		try (FileReader fr = new FileReader(path)) {
			sqlProp.load(fr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ReviewDAO reviewDao() {
		if (dao == null)
			dao = new ReviewDAO();
		return dao;
	}

	public List<Review> getAllReviewsByDate(Connection conn, int cPage, int numPerpage) {
		List<Review> reviews = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getAllReviews"));
			pstmt.setInt(1, (cPage-1)*numPerpage+1);
			pstmt.setInt(2, cPage*numPerpage);
			rs = pstmt.executeQuery();
			while (rs.next())
				reviews.add(getReviewDTO(rs));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return reviews;
	}
	
	public List<Review> getAllReviewsByRec(Connection conn, int cPage, int numPerpage) {
		List<Review> reviews = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getAllReviewsByRec"));
			pstmt.setInt(1, (cPage-1)*numPerpage+1);
			pstmt.setInt(2, cPage*numPerpage);
			rs = pstmt.executeQuery();
			while (rs.next())
				reviews.add(getReviewDTO(rs));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return reviews;
	}
	
	public int reviewCount(Connection conn) {
		Statement stmt=null;
		ResultSet rs=null;
		int result=0;
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery(sqlProp.getProperty("reviewCount"));
			if(rs.next()) result=rs.getInt(1); 
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(stmt);
			
		}
		return result;
	}
	
	public List<Review> getBestReviews(Connection conn) {
		List<Review> reviews = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getBestReviews"));
			rs = pstmt.executeQuery();
			while (rs.next())
				reviews.add(getReviewDTO(rs));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return reviews;
	}

	private Review getReviewDTO(ResultSet rs) throws SQLException {
		Review dto = Review.builder().reviewSeq(rs.getLong("REVIEW_SEQ"))
				.reviewTitle(rs.getString("REVIEW_TITLE")).reviewContents(rs.getString("REVIEW_CONTENTS"))
				.reviewCreateTime(rs.getTimestamp("REVIEW_CREATE_TIME")).reviewRate(rs.getInt("REVIEW_RATE"))
				.reviewEditTime(rs.getTimestamp("REVIEW_EDIT_TIME")).userSeq(rs.getLong("USER_SEQ"))
				.bookSeq(rs.getLong("BOOK_SEQ"))
				.reviewIsPublic(rs.getString("REVIEW_IS_PUBLIC").charAt(0))
				.comments(new ArrayList<>())
				.recommendCount(rs.getInt("RECOMMEND_COUNT"))
				.bookAuthor(rs.getString("BOOK_AUTHOR")).bookAvgRate(rs.getDouble("BOOK_AVG_RATE"))
				.bookCover(rs.getString("BOOK_COVER")).bookPublisher(rs.getString("BOOK_PUBLISHER"))
				.bookReviewCount(rs.getInt("BOOK_REVIEW_COUNT")).bookTitle(rs.getString("BOOK_TITLE"))
				.build();
		return dto;
	}
}
