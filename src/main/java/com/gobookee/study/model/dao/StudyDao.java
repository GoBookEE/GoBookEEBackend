package com.gobookee.study.model.dao;

import static com.gobookee.common.JDBCTemplate.*;

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
import com.gobookee.study.model.dto.Study;
import com.gobookee.study.model.dto.StudyList;

public class StudyDao {
	
	private Properties sql = new Properties();
	
	private static final StudyDao studyDao = new StudyDao();
	
	private StudyDao() {
		String path = StudyDao.class.getResource("/config/study-sql.properties").getPath();
		try(FileReader fr = new FileReader(path)){
			sql.load(fr);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static StudyDao studyDao() {
		return studyDao;
	}
	
	public List<StudyList> getStudiesByTime(Connection conn, int cPage, int numPerpage){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List<StudyList> list = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql.getProperty("getStudiesByTime"));
			pstmt.setInt(1, (cPage-1)*numPerpage+1);
			pstmt.setInt(2, cPage*numPerpage);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				StudyList s = getStudyList(rs);
				list.add(s);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close(rs);
			close(pstmt);
		}
		return list;
	}
	
	public List<StudyList> getStudiesByRec(Connection conn, int cPage, int numPerpage){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List<StudyList> list = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql.getProperty("getStudiesByRec"));
			pstmt.setInt(1, (cPage-1)*numPerpage+1);
			pstmt.setInt(2, cPage*numPerpage);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				StudyList s = getStudyList(rs);
				list.add(s);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close(rs);
			close(pstmt);
		}
		return list;
	}
	
	public int allStudyCount(Connection conn){
		Statement stmt=null;
		ResultSet rs=null;
		int result=0;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql.getProperty("allStudyCount"));
			if(rs.next()) result=rs.getInt(1);
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			close(rs);
			close(stmt);
		}return result;
	}
	
	private StudyList getStudyList(ResultSet rs)throws SQLException{
		return StudyList.builder()
				.studySeq(rs.getLong("study_seq"))
				.studyTitle(rs.getString("study_title"))
				.studyDate(rs.getTimestamp("study_date"))
			    .studyMemberLimit(rs.getInt("study_member_limit"))
			    .studyPlace(rs.getString("study_place"))
			    .confirmedCount(rs.getInt("confirmed_count"))
			    .photoRenamedName(rs.getString("photo_name"))
			    .likeCount(rs.getInt("like_count"))
			    .dislikeCount(rs.getInt("dislike_count"))
				.build();
	}
	
	
}
