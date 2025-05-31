package com.gobookee.mypage.model.dao;

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
import com.gobookee.mypage.model.dto.RecStudy;

public class RecStudyDAO {
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Properties sqlProp = new Properties();

	private static RecStudyDAO dao;

	private RecStudyDAO() {
		String path = RecStudyDAO.class.getResource("/config/review-sql.properties").getPath();
		try (FileReader fr = new FileReader(path)) {
			sqlProp.load(fr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RecStudyDAO recStudyDao() {
		if (dao == null) {
			dao = new RecStudyDAO();
		}
		return dao;
	}

	public List<RecStudy> getStudiesRecByUser(Connection conn, Long userSeq, int cPage, int numPerPage) {
		List<RecStudy> studies = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getStudiesRecByUser"));
			pstmt.setLong(1, userSeq);
			pstmt.setInt(2, (cPage - 1) * numPerPage + 1);
			pstmt.setInt(3, cPage * numPerPage);
			rs = pstmt.executeQuery();
			while (rs.next())
				studies.add(getRecStudy(rs));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return studies;
	}

	public int countStudiesRecByUser(Connection conn, Long userSeq) {
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("countStudiesRecByUser"));
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

	private RecStudy getRecStudy(ResultSet rs) throws SQLException {
		return RecStudy.builder().studySeq(rs.getLong("STUDY_SEQ")).studyTitle(rs.getString("STUDY_TITLE"))
				.studyDate(rs.getDate("STUDY_DATE")).studyMemberLimit(rs.getInt("STUDY_MEMBER_LIMIT"))
				.studyAddress(rs.getString("STUDY_ADDRESS")).confirmCount(rs.getInt("CONFIRMED_COUNT"))
				.photoRenamedName(rs.getString("PHOTO_NAME")).likeCount(rs.getInt("LIKE_COUNT"))
				.dislikeCount(rs.getInt("DISLIKE_COUNT")).nickname(rs.getString("USER_NICKNAME")).build();
	}

}
