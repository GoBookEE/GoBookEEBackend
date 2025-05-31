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
import com.gobookee.study.model.dto.StudyRequest;
import com.gobookee.study.model.dto.StudyView;

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
    
    public StudyView getStudyView(Connection conn, Long studySeq){
        pstmt = null;
        rs = null;
        StudyView studyView = null;
        try{
        	pstmt = conn.prepareStatement(sql.getProperty("studyView"));
        	pstmt.setLong(1, studySeq);
        	rs = pstmt.executeQuery();
        	if(rs.next()) {
        		studyView = getStudyViews(rs);
        	}
        }catch (SQLException e){
        	e.printStackTrace();
        }finally {
        	close(rs);
        	close(pstmt);
        }
        return studyView;
    }
    
    private StudyView getStudyViews(ResultSet rs) throws SQLException{
    	return StudyView.builder()
    			.studySeq(rs.getLong("study_seq"))
    			.userSeq(rs.getLong("user_seq"))
    			.studyTitle(rs.getString("study_title"))
    			.studyDate(rs.getDate("study_date"))
    			.studyContent(rs.getString("study_content"))
    			.studyMemberLimit(rs.getLong("study_member_limit"))
    			.studyAddress(rs.getString("study_address"))
    			.studyLatitude(rs.getDouble("study_latitude"))
    			.studyLongitude(rs.getDouble("study_longitude"))
    			.userNickName(rs.getString("user_nickname"))
    			.userSpeed(rs.getLong("user_speed"))
    			.userProfile(rs.getString("user_profile"))
    			.studyCategory(rs.getString("study_category"))
    			.photoName(rs.getString("photo_name"))
    			.confirmedCount(rs.getLong("confirmed_count"))
    			.likeCount(rs.getLong("like_count"))
    			.dislikeCount(rs.getLong("dislike_count"))
    			.build();
    }
    
    public List<StudyView> getStudyViewUser(Connection conn, Long studySeq){
        pstmt = null;
        rs = null;
        List<StudyView> list = new ArrayList<>();
        try {
        	pstmt = conn.prepareStatement(sql.getProperty("studyConfirmedUsers"));
        	pstmt.setLong(1, studySeq);
        	rs = pstmt.executeQuery();
        	while(rs.next()) {
        		StudyView s = getStudyViewUsers(rs);
        		list.add(s);
        	}
        }catch (SQLException e){
        	e.printStackTrace();
        }finally {
        	close(rs);
        	close(pstmt);
        }
        return list;
    }
    
    private StudyView getStudyViewUsers(ResultSet rs)throws SQLException{
    	return StudyView.builder()
    			.userSeq(rs.getLong("user_seq"))
    			.userNickName(rs.getString("user_nickname"))
    			.userProfile(rs.getString("user_profile"))
    			.userSpeed(rs.getLong("user_speed"))
    			.build();
    }
    
    public List<StudyView> getStudyNotConfirmedUser(Connection conn, Long studySeq){
        pstmt = null;
        rs = null;
        List<StudyView> list = new ArrayList<>();
        try {
        	System.out.println(sql.getProperty("studyNotConfirmedUsers"));
        	pstmt = conn.prepareStatement(sql.getProperty("studyNotConfirmedUsers"));
        	pstmt.setLong(1, studySeq);
        	rs = pstmt.executeQuery();
        	while(rs.next()) {
        		StudyView s = getstudyNotConfirmedUsers(rs);
        		list.add(s);
        	}
        }catch (SQLException e){
        	e.printStackTrace();
        }finally {
        	close(rs);
        	close(pstmt);
        }
        return list;
    }
    
    private StudyView getstudyNotConfirmedUsers(ResultSet rs)throws SQLException{
    	return StudyView.builder()
    			.userSeq(rs.getLong("user_seq"))
    			.requestConfirm(rs.getString("request_confirm"))
    			.build();
    }
    
    public int insertStudyRequest(Connection conn, Long studySeq, Long userSeq, String requestMsg) throws SQLException {
    	String sql = this.sql.getProperty("insertStudyRequest");
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, studySeq);
            pstmt.setLong(2, userSeq);
            pstmt.setString(3, requestMsg);
            return pstmt.executeUpdate();
        }
    }
    
    public List<StudyRequest> getStudyRequests(Connection conn, Long studySeq) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<StudyRequest> studyrequest = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql.getProperty("studyRequests"));
            pstmt.setLong(1, studySeq);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                studyrequest.add(getStudyRequest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }

        return studyrequest;
    }
    
    private StudyRequest getStudyRequest(ResultSet rs)throws SQLException{
    	return StudyRequest.builder()
    			.studySeq(rs.getLong("study_seq"))
    			.studyMemberLimit(rs.getLong("study_member_limit"))
    			.userSeq(rs.getLong("user_seq"))
    			.userProfile(rs.getString("user_profile"))
    			.userNickName(rs.getString("user_nickname"))
    			.userSpeed(rs.getLong("user_speed"))
    			.requestConfirm(rs.getString("request_confirm"))
    			.requestMsg(rs.getString("request_msg"))
    			.hostSeq(rs.getLong("host_seq"))
    			.build();
    }
    
    public int updateRequestConfirm(Connection conn, Long userSeq, Long studySeq, String confirmStatus) {
        PreparedStatement pstmt = null;
        
        int result = 0;
        try {
            pstmt = conn.prepareStatement(sql.getProperty("updateRequestConfirm"));
            pstmt.setString(1, confirmStatus);
            pstmt.setLong(2, studySeq);
            pstmt.setLong(3, userSeq);
            result = pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }
        return result;
    }
    
}
