package com.gobookee.recommend.model.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.gobookee.common.JDBCTemplate;
import com.gobookee.recommend.model.dto.Recommend;

public class RecommendDAO {

	private PreparedStatement pstmt;
	private ResultSet rs;
	private Properties sqlProp = new Properties();

	private static RecommendDAO dao;

	private RecommendDAO() {
		String path = RecommendDAO.class.getResource("/config/recommend-sql.properties").getPath();
		try (FileReader fr = new FileReader(path)) {
			sqlProp.load(fr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RecommendDAO recommendDao() {
		if (dao == null)
			dao = new RecommendDAO();
		return dao;
	}

	public void delete(Connection conn, Long recSeq) {

		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("delete"));
			pstmt.setLong(1, recSeq);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
	}

	public Recommend getRecommendBySeq(Connection conn, Long userSeq, Long boardSeq) {
		Recommend rec = null;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getRecommend"));
			pstmt.setLong(1, userSeq);
			pstmt.setLong(2, boardSeq);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				rec = getRecommend(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return rec;
	}

	public int countByType(Connection conn, Long boardSeq, String recType) {
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("countByType"));
			pstmt.setLong(1, boardSeq);
			pstmt.setString(2, recType);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void merge(Connection conn, Long userSeq, Long boardSeq, String recType) {
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("merge"));
			pstmt.setLong(1, userSeq); // ON 조건
			pstmt.setLong(2, boardSeq); // ON 조건
			pstmt.setString(3, recType); // UPDATE
			pstmt.setLong(4, userSeq); // INSERT
			pstmt.setLong(5, boardSeq); // INSERT
			pstmt.setString(6, recType); // INSERT
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
	}

	public void updateUserSpeed(Connection conn, Long userSeq, int change) {
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("updateUserSpeed"));
			pstmt.setInt(1, change);
			pstmt.setLong(2, userSeq);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
	}

	private Recommend getRecommend(ResultSet rs) throws SQLException {
		Recommend rec = Recommend.builder().recBoardSeq(rs.getLong("REC_BOARD_SEQ")).recSeq(rs.getLong("REC_SEQ"))
				.recType(rs.getString("REC_TYPE")).userSeq(rs.getLong("USER_SEQ")).build();
		return rec;
	}
}
