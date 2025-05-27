package com.gobookee.recommend.model.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.gobookee.common.JDBCTemplate;

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

	public void insert(Connection conn, Long userSeq, Long boardSeq, String recType) {
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("insert"));
			pstmt.setLong(1, userSeq);
			pstmt.setLong(2, boardSeq);
			pstmt.setString(3, recType);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
	}

	public void delete(Connection conn, Long userSeq, Long boardSeq) {

		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("delete"));
			pstmt.setLong(1, userSeq);
			pstmt.setLong(2, boardSeq);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
	}

	public boolean exists(Connection conn, Long userSeq, Long boardSeq, String recType) {

		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("exists"));
			pstmt.setLong(1, userSeq);
			pstmt.setLong(2, boardSeq);
			pstmt.setString(3, recType);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return false;
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
}
