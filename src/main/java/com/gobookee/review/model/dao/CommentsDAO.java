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
import com.gobookee.review.model.dto.Comments;
import com.gobookee.review.model.dto.CommentsViewResponse;

public class CommentsDAO {

	private PreparedStatement pstmt;
	private ResultSet rs;
	private Properties sqlProp = new Properties();

	private static CommentsDAO dao;

	private CommentsDAO() {
		String path = CommentsDAO.class.getResource("/config/review-sql.properties").getPath();
		try (FileReader fr = new FileReader(path)) {
			sqlProp.load(fr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static CommentsDAO commentsDao() {
		if (dao == null) {
			dao = new CommentsDAO();
		}
		return dao;
	}

	public List<CommentsViewResponse> getReviewComments(Connection conn, Long seq) {
		pstmt = null;
		rs = null;
		List<CommentsViewResponse> comments = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getReviewComments"));
			pstmt.setLong(1, seq);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				comments.add(getCommentsViewResponse(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return comments;
	}

	public List<CommentsViewResponse> getAllCommentsByUser(Connection conn, Long userSeq, int cPage, int numPerPage) {
		pstmt = null;
		rs = null;
		List<CommentsViewResponse> comments = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getAllCommentsByUser"));
			pstmt.setLong(1, userSeq);
			pstmt.setInt(2, (cPage - 1) * numPerPage + 1);
			pstmt.setInt(3, cPage * numPerPage);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				comments.add(getCommentsViewResponseMyPage(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return comments;
	}

	public int countByUser(Connection conn, Long userSeq) {
		pstmt = null;
		rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("commentsCountByUser"));
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

	public List<CommentsViewResponse> getAllCommentsRecByUser(Connection conn, Long userSeq, int cPage,
			int numPerPage) {
		pstmt = null;
		rs = null;
		List<CommentsViewResponse> comments = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getAllCommentsRecByUser"));
			pstmt.setLong(1, userSeq);
			pstmt.setInt(2, (cPage - 1) * numPerPage + 1);
			pstmt.setInt(3, cPage * numPerPage);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				comments.add(getCommentsViewResponseMyPage(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return comments;
	}

	public int countCommentsRecByUser(Connection conn, Long userSeq) {
		pstmt = null;
		rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("countCommentsRecByUser"));
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

	public int insertComment(Connection conn, Comments dto) {
		pstmt = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("insertComment"));
			pstmt.setString(1, dto.getCommentsContents());
			pstmt.setLong(2, dto.getCommentsParentSeq());
			pstmt.setLong(3, dto.getUserSeq());
			pstmt.setLong(4, dto.getReviewSeq());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public int updateComment(Connection conn, long commentSeq, long userSeq, String newContent) {
		pstmt = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("updateComment"));
			pstmt.setString(1, newContent);
			pstmt.setLong(2, commentSeq);
			pstmt.setLong(3, userSeq);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public int deleteComment(Connection conn, long commentSeq, long userSeq) {
		pstmt = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("deleteComment"));
			pstmt.setLong(1, commentSeq);
			pstmt.setLong(2, userSeq);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	private Comments getCommentDTO(ResultSet rs) throws SQLException {
		Comments dto = Comments.builder().commentsSeq(rs.getLong("COMMENTS_SEQ"))
				.commentsContents(rs.getString("COMMENTS_CONTENTS"))
				.commentsCreateTime(rs.getTimestamp("COMMENTS_CREATE_TIME"))
				.commentsDeleteTime(rs.getTimestamp("COMMENTS_DELETE_TIME"))
				.commentsEditTime(rs.getTimestamp("COMMENTS_EDIT_TIME"))
				.commentsParentSeq(rs.getLong("COMMENTS_PARENT_SEQ"))
				.commentsIsPublic(rs.getString("COMMENTS_IS_PUBLIC").charAt(0)).userSeq(rs.getLong("USER_SEQ"))
				.reviewSeq(rs.getLong("REVIEW_SEQ")).build();
		return dto;
	}

	private CommentsViewResponse getCommentsViewResponse(ResultSet rs) throws SQLException {
		CommentsViewResponse dto = CommentsViewResponse.builder().commentsContents(rs.getString("COMMENTS_CONTENTS"))
				.commentLevel(rs.getInt("COMMENT_LEVEL")).commentsParentSeq(rs.getLong("COMMENTS_PARENT_SEQ"))
				.commentsSeq(rs.getLong("COMMENTS_SEQ")).commentsCreateTime(rs.getTimestamp("COMMENTS_CREATE_TIME"))
				.commentsEditTime(rs.getTimestamp("COMMENTS_EDIT_TIME")).userNickName(rs.getString("USER_NICKNAME"))
				.recommendCount(rs.getInt("RECOMMEND_COUNT")).nonRecommendCount(rs.getInt("NON_RECOMMEND_COUNT"))
				.userSeq(rs.getLong("USER_SEQ")).build();
		return dto;
	}

	private CommentsViewResponse getCommentsViewResponseMyPage(ResultSet rs) throws SQLException {
		CommentsViewResponse dto = CommentsViewResponse.builder().commentsContents(rs.getString("COMMENTS_CONTENTS"))
				.commentsSeq(rs.getLong("COMMENTS_SEQ")).commentsCreateTime(rs.getTimestamp("COMMENTS_CREATE_TIME"))
				.commentsEditTime(rs.getTimestamp("COMMENTS_EDIT_TIME")).userNickName(rs.getString("USER_NICKNAME"))
				.recommendCount(rs.getInt("RECOMMEND_COUNT")).nonRecommendCount(rs.getInt("NON_RECOMMEND_COUNT"))
				.userSeq(rs.getLong("USER_SEQ")).reviewSeq(rs.getLong("REVIEW_SEQ"))
				.reviewTitle(rs.getString("REVIEW_TITLE")).build();
		return dto;
	}
}