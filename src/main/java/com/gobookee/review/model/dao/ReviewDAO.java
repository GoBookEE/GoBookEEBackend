package com.gobookee.review.model.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.gobookee.common.JDBCTemplate;
import com.gobookee.review.model.dto.ReviewDTO;

public class ReviewDAO {
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Properties sqlProp = new Properties();

	private static ReviewDAO dao;

	private ReviewDAO() {
		String path=ReviewDAO.class.getResource("/sql/review_sql.properties").getPath();
		try(FileReader fr=new FileReader(path)){
			sqlProp.load(fr);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static ReviewDAO reviewDao() {
		if (dao == null)
			dao = new ReviewDAO();
		return dao;
	}

	public List<ReviewDTO> getAllReviews(Connection conn) {
		List<ReviewDTO> reviews = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getAllReviews"));
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

	public List<ReviewDTO> getBestReviews(Connection conn) {
		List<ReviewDTO> reviews = new ArrayList<>();
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

	private ReviewDTO getReviewDTO(ResultSet rs) throws SQLException {
		ReviewDTO dto = ReviewDTO.builder().reviewSeq(rs.getLong("REVIEW_SEQ")).reviewTitle(rs.getString("REVIEW_TITLE"))
				.reviewContents(rs.getString("REVIEW_CONTENTS")).reviewCreateTime(rs.getTimestamp("REVIEW_CREATE_TIME"))
				.reviewRate(rs.getInt("REVIEW_RATE")).reviewEditTime(rs.getTimestamp("REVIEW_EDIT_TIME"))
				.userSeq(rs.getLong("USER_SEQ")).bookSeq(rs.getLong("BOOK_SEQ")).build();
		return dto;
	}
}
