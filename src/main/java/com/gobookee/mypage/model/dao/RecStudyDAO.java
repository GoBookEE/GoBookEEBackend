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
import com.gobookee.mypage.model.dto.MyStudy;
import com.gobookee.mypage.model.dto.RecStudy;
import com.gobookee.study.model.dto.StudyList;

public class RecStudyDAO {
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Properties sqlProp = new Properties();

	private static RecStudyDAO dao;

	private RecStudyDAO() {
		String path = RecStudyDAO.class.getResource("/config/mypage-sql.properties").getPath();
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
		pstmt = null;
		rs = null;
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

	public List<StudyList> getAppliedByStatus(Connection conn, MyStudy mystudy) {
		pstmt = null;
		rs = null;
		int numPerPage = 5;
		List<StudyList> studies = new ArrayList<>();
		String sqlKey = mystudy.getStatus().equals("upcoming") ? "getAppliedUpcoming" : "getAppliedCompleted";
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty(sqlKey));
			pstmt.setLong(1, mystudy.getUserSeq()); // 신청자
			pstmt.setLong(2, mystudy.getUserSeq()); // 내가 만든 건 제외
			pstmt.setInt(3, (mystudy.getCPage() - 1) * numPerPage + 1);
			pstmt.setInt(4, mystudy.getCPage() * numPerPage);
			rs = pstmt.executeQuery();
			while (rs.next())
				studies.add(getStudyList(rs));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return studies;
	}

	public int countAppliedByStatus(Connection conn, MyStudy mystudy) {
		pstmt = null;
		rs = null;
		int result = 0;
		String sqlKey = mystudy.getStatus().equals("upcoming") ? "countAppliedUpcoming" : "countAppliedCompleted";
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty(sqlKey));
			pstmt.setLong(1, mystudy.getUserSeq());
			pstmt.setLong(2, mystudy.getUserSeq());
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

	public List<StudyList> getCreatedByStatus(Connection conn, MyStudy mystudy) {
		pstmt = null;
		rs = null;
		int numPerPage = 5;
		List<StudyList> studies = new ArrayList<>();
		String sqlKey = mystudy.getStatus().equals("upcoming") ? "getCreatedUpcoming" : "getCreatedCompleted";
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty(sqlKey));
			pstmt.setLong(1, mystudy.getUserSeq());
			pstmt.setInt(2, (mystudy.getCPage() - 1) * numPerPage + 1);
			pstmt.setInt(3, mystudy.getCPage() * numPerPage);
			rs = pstmt.executeQuery();
			while (rs.next())
				studies.add(getStudyList(rs));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return studies;
	}

	public int countCreatedByStatus(Connection conn, MyStudy mystudy) {
		pstmt = null;
		rs = null;
		int result = 0;
		String sqlKey = mystudy.getStatus().equals("upcoming") ? "countCreatedUpcoming" : "countCreatedCompleted";
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty(sqlKey));
			pstmt.setLong(1, mystudy.getUserSeq());
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

	private StudyList getStudyList(ResultSet rs) throws SQLException {
		return StudyList.builder().studySeq(rs.getLong("STUDY_SEQ")).studyTitle(rs.getString("STUDY_TITLE"))
				.studyDate(rs.getDate("STUDY_DATE")).studyMemberLimit(rs.getInt("STUDY_MEMBER_LIMIT"))
				.studyAddress(rs.getString("STUDY_ADDRESS")).confirmedCount(rs.getInt("CONFIRMED_COUNT"))
				.photoRenamedName(rs.getString("PHOTO_NAME")).likeCount(rs.getInt("LIKE_COUNT"))
				.dislikeCount(rs.getInt("DISLIKE_COUNT")).build();
	}

	private RecStudy getRecStudy(ResultSet rs) throws SQLException {
		return RecStudy.builder().studySeq(rs.getLong("STUDY_SEQ")).studyTitle(rs.getString("STUDY_TITLE"))
				.studyDate(rs.getDate("STUDY_DATE")).studyMemberLimit(rs.getInt("STUDY_MEMBER_LIMIT"))
				.studyAddress(rs.getString("STUDY_ADDRESS")).confirmCount(rs.getInt("CONFIRMED_COUNT"))
				.photoRenamedName(rs.getString("PHOTO_NAME")).likeCount(rs.getInt("LIKE_COUNT"))
				.dislikeCount(rs.getInt("DISLIKE_COUNT")).nickname(rs.getString("USER_NICKNAME")).build();
	}

}
