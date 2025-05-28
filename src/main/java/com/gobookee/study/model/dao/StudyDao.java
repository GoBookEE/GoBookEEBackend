package com.gobookee.study.model.dao;

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

import com.gobookee.study.model.dto.SearchStudyResponse;
import com.gobookee.study.model.dto.StudyInsert;
import com.gobookee.study.model.dto.StudyList;

public class StudyDao {

    private Properties sql = new Properties();
    private PreparedStatement pstmt;
    private ResultSet rs;

    private static final StudyDao studyDao = new StudyDao();

    private StudyDao() {
        String path = StudyDao.class.getResource("/config/study-sql.properties").getPath();
        try (FileReader fr = new FileReader(path)) {
            sql.load(fr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StudyDao studyDao() {
        return studyDao;
    }

    public List<StudyList> getStudiesByTime(Connection conn, int cPage, int numPerpage) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<StudyList> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql.getProperty("getStudiesByTime"));
            pstmt.setInt(1, (cPage - 1) * numPerpage + 1);
            pstmt.setInt(2, cPage * numPerpage);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                StudyList s = getStudyList(rs);
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return list;
    }

    public List<StudyList> getStudiesByRec(Connection conn, int cPage, int numPerpage) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<StudyList> list = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql.getProperty("getStudiesByRec"));
            pstmt.setInt(1, (cPage - 1) * numPerpage + 1);
            pstmt.setInt(2, cPage * numPerpage);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                StudyList s = getStudyList(rs);
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return list;
    }

    public int allStudyCount(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        int result = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.getProperty("allStudyCount"));
            if (rs.next()) result = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
        }
        return result;
    }

    public List<SearchStudyResponse> getStudyListByHostUserSeq(Connection conn, Long userId) {
        pstmt = null;
        rs = null;
        List<SearchStudyResponse> studyList = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql.getProperty("getStudyListByHostUserSeq"));
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                studyList.add(SearchStudyResponse.from(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return studyList;
    }
    
    public int insertStudyAndReturnId(Connection conn, StudyInsert studyinsert){
    	int generatedId = getNextBoardSeq(conn);
    	if(generatedId == -1) {
    		return -1;
    	}
    	int result = insertStudy(conn, studyinsert, generatedId);
    	return result > 0 ? generatedId : -1;
    }
    
    private int getNextBoardSeq(Connection conn) {
        int seq = -1;
        try {
            pstmt = conn.prepareStatement(sql.getProperty("getNextBoardSeq"));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                seq = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return seq;
    }
    
    public int insertStudy(Connection conn, StudyInsert studyinsert, int seq) {
    	int result = 0;
    	pstmt = null;
    	rs = null;
    	List<StudyInsert> studylist = new ArrayList<>();
    	try {
    		pstmt = conn.prepareStatement(sql.getProperty("insertStudy"));
    		pstmt.setLong(1, seq);
    		pstmt.setString(2, studyinsert.getStudyTitle());
    		pstmt.setString(3, studyinsert.getStudyContent());
    		pstmt.setDate(4, studyinsert.getStudyDate());
    		pstmt.setLong(5, studyinsert.getStudyMemberLimit());
    		pstmt.setString(6, studyinsert.getStudyAddress());
    		pstmt.setObject(7, studyinsert.getStudyLatitude(), java.sql.Types.DOUBLE);
    		pstmt.setObject(8, studyinsert.getStudyLongitude(), java.sql.Types.DOUBLE);
    		pstmt.setString(9, studyinsert.getStudyCategory());
    		pstmt.setLong(10, studyinsert.getUserSeq());
    		result = pstmt.executeUpdate();
    	}catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }
        return result;
    }

    private StudyList getStudyList(ResultSet rs) throws SQLException {
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
