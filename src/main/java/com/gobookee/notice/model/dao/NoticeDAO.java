package com.gobookee.notice.model.dao;

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
import com.gobookee.notice.model.dto.Notice;

public class NoticeDAO {
	
	private static final NoticeDAO DAO = new NoticeDAO();
	private NoticeDAO() {
		String path = NoticeDAO.class.getResource("/config/notice-sql.properties").getPath();
		try(FileReader fr = new FileReader(path)) {
			sqlProp.load(fr);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static NoticeDAO noticeDao() {
		return DAO;
	}
	
	private Properties sqlProp=new Properties();
	
	public List<Notice> searchNoticeAll (Connection conn, int cPage, int numPerpage) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Notice> list = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("searchNoticeAll"));
			
			pstmt.setInt(1, (cPage-1)*numPerpage+1);
			pstmt.setInt(2, cPage*numPerpage+1);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(getNotice(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}
	
	public int noticeCount (Connection conn) {
		Statement stmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			stmt = conn.createStatement();                      
			rs = stmt.executeQuery(sqlProp.getProperty("noticeCount"));
			if(rs.next()) 
				result = rs.getInt(1);
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(stmt);
		}
		return result;
	}
	
	public Notice noticeBySeq(Connection conn, Long noticeSeq) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Notice n = new Notice();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("noticeBySeq"));
			pstmt.setLong(1, noticeSeq);
			rs = pstmt.executeQuery();
			if(rs.next()) n=getNotice(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return n;
	}
	
	public int updateNotice (Connection conn, Notice n) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("updateNotice"));
			pstmt.setString(1, n.getNoticeTitle());
			pstmt.setString(2, n.getNoticeContents());
			pstmt.setLong(3, n.getNoticeOrder());
			pstmt.setLong(4, n.getNoticeSeq());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	public int writeNotice (Connection conn, Notice n) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("writeNotice"));
			pstmt.setString(1, n.getNoticeTitle());
			pstmt.setString(2, n.getNoticeContents());
			pstmt.setLong(3, n.getNoticeOrder());
			result = pstmt.executeUpdate();
		}  catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	public int deleteNotice (Connection conn, Long noticeSeq) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("deleteNotice"));
			pstmt.setLong(1, noticeSeq);
			result = pstmt.executeUpdate();
		}  catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	private Notice getNotice (ResultSet rs) throws SQLException {
		return Notice.builder()
				.noticeSeq(rs.getLong("notice_seq"))
				.noticeTitle(rs.getString("notice_title"))
				.noticeContents(rs.getString("notice_contents"))
				.noticeOrder(rs.getLong("notice_order"))
				.noticeCreateTime(rs.getTimestamp("notice_create_time"))
				.noticeDeleteTime(rs.getTimestamp("notice_delete_time"))
				.noticeEditTime(rs.getTimestamp("notice_edit_time"))
				.build();
				
			
	}
}
