package com.gobookee.study.model.dao;

import com.gobookee.study.model.dto.SearchStudyResponse;
import com.gobookee.study.model.dto.StudyList;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.gobookee.common.JDBCTemplate.close;

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

    public int getStudyCountByPlaceSeqAndDate(Connection conn, Long placeSeq, Date date) {
        pstmt = null;
        rs = null;
        int count = 0;
        try {
            pstmt = conn.prepareStatement(sql.getProperty("getStudyCountByPlaceSeqAndDate"));
            pstmt.setLong(1, placeSeq);
            pstmt.setDate(2, date);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private StudyList getStudyList(ResultSet rs) throws SQLException {
        return StudyList.builder()
                .studySeq(rs.getLong("study_seq"))
                .studyTitle(rs.getString("study_title"))
                .studyDate(rs.getTimestamp("study_date"))
                .studyMemberLimit(rs.getInt("study_member_limit"))
                .studyPlace(rs.getString("study_place"))
                .confirmedCount(rs.getInt("confirmed_count"))
                .photoRenamedName(rs.getString("photo_name"))
                .build();
    }
}
