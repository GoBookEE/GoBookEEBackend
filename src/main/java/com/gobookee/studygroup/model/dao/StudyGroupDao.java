package com.gobookee.studygroup.model.dao;

import static com.gobookee.common.JDBCTemplate.close;

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

import com.gobookee.study.model.dao.StudyDao;
import com.gobookee.study.model.dto.StudyList;

public class StudyGroupDao {
	
	PreparedStatement pstmt;
	ResultSet rs;
	Statement stmt;
	private Properties sql = new Properties();
	
	private static final StudyGroupDao studyGroupDao = new StudyGroupDao();
	
	private StudyGroupDao() {
		String path = StudyDao.class.getResource("/config/studygroup-sql.properties").getPath();
		try(FileReader fr = new FileReader(path)){
			sql.load(fr);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static StudyGroupDao studyGroupDao() {
		return studyGroupDao;
	}
	
	public List<StudyList> getGroupStudiesByTime(Connection conn, Long userseq, int cPage, int numPerpage){
		pstmt=null;
		rs=null;
		List<StudyList> list = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql.getProperty("getStudyGroupsByTime"));
			pstmt.setLong(1, userseq);
			pstmt.setLong(2, userseq);
			pstmt.setInt(3, (cPage-1)*numPerpage+1);
			pstmt.setInt(4, cPage*numPerpage);
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
	
	public List<StudyList> getGroupStudiesByRec(Connection conn, Long userseq, int cPage, int numPerpage){
		pstmt=null;
		rs=null;
		List<StudyList> list = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql.getProperty("getStudyGroupsByRec"));
			pstmt.setLong(1, userseq);
			pstmt.setLong(2, userseq);
			pstmt.setInt(3, (cPage-1)*numPerpage+1);
			pstmt.setInt(4, cPage*numPerpage);
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
	
	
	public int allStudyGroupsCount(Connection conn, Long userseq) {
		pstmt=null;
		rs=null;
		int result=0;
		try {
			pstmt = conn.prepareStatement(sql.getProperty("allStudyGroupsCount"));
			pstmt.setLong(1, userseq);
			pstmt.setLong(2, userseq);
			rs = pstmt.executeQuery();
			if(rs.next()) result=rs.getInt(1);
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			close(rs);
			close(stmt);
		}return result;
	}
	
//만료된 스터디그룹
	public List<StudyList> getGroupStudiesByTimeEnd(Connection conn, Long userseq, int cPage, int numPerpage){
		pstmt=null;
		rs=null;
		List<StudyList> list = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql.getProperty("getStudyGroupsByTimeEnd"));
			pstmt.setLong(1, userseq);
			pstmt.setLong(2, userseq);
			pstmt.setInt(3, (cPage-1)*numPerpage+1);
			pstmt.setInt(4, cPage*numPerpage);
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
	
	public List<StudyList> getGroupStudiesByRecEnd(Connection conn, Long userseq, int cPage, int numPerpage){
		pstmt=null;
		rs=null;
		List<StudyList> list = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql.getProperty("getStudyGroupsByRecEnd"));
			pstmt.setLong(1, userseq);
			pstmt.setLong(2, userseq);
			pstmt.setInt(3, (cPage-1)*numPerpage+1);
			pstmt.setInt(4, cPage*numPerpage);
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
	
	public int allStudyGroupsCountEnd(Connection conn, Long userseq) {
		pstmt=null;
		rs=null;
		int result=0;
		try {
			pstmt = conn.prepareStatement(sql.getProperty("allStudyGroupsCountEnd"));
			pstmt.setLong(1, userseq);
			pstmt.setLong(2, userseq);
			rs = pstmt.executeQuery();
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
				.studyDate(rs.getDate("study_date"))
			    .studyMemberLimit(rs.getInt("study_member_limit"))
			    .studyAddress(rs.getString("study_address"))
			    .confirmedCount(rs.getInt("confirmed_count"))
			    .photoRenamedName(rs.getString("photo_name"))
			    .likeCount(rs.getInt("like_count"))
			    .dislikeCount(rs.getInt("dislike_count"))
				.build();
	}
}
