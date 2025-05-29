package com.gobookee.reports.model.dao;

import com.gobookee.common.JDBCTemplate;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class ReportsDAO {
    private PreparedStatement pstmt;
    private ResultSet rs;
    private Properties sqlProp = new Properties();

    private static ReportsDAO dao;

    private ReportsDAO() {
        String path = ReportsDAO.class.getResource("/config/reports-sql.properties").getPath();
        try (FileReader fr = new FileReader(path)) {
            sqlProp.load(fr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ReportsDAO reportsDao() {
        if (dao == null)
            dao = new ReportsDAO();
        return dao;
    }

    public int insertReport(Connection conn, Long userSeq, Long boardSeq, String reason) {
        int result = 0;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("insertReport"));
            pstmt.setString(1, reason);
            pstmt.setLong(2, boardSeq);
            pstmt.setLong(3, userSeq);
            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }
        return result;
    }

    public int countReports(Connection conn, Long boardSeq) {
        int result = 0;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("countReports"));
            pstmt.setLong(1, boardSeq);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }
        return result;
    }

    public void hidePost(Connection conn, Long boardSeq, String boardType) {

        try {
            String key = "";
            switch (boardType) {
                case "REVIEW":
                    key = "hideReview";
                    break;
                case "COMMENTS":
                    key = "hideComment";
                    break;
                case "STUDY":
                    key = "hideStudy";
                    break;
                case "PLACE":
                    key = "hidePlace";
                    break;
                default:
                    throw new IllegalArgumentException("유효하지 않은 boardType: " + boardType);
            }

            String sql = sqlProp.getProperty(key);
            if (sql == null || sql.trim().isEmpty()) {
                throw new RuntimeException("SQL 정의가 없습니다: " + key);
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, boardSeq);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }
    }

    public boolean exists(Connection conn, Long userSeq, Long boardSeq) {
        boolean result = false;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("exists"));
            pstmt.setLong(1, userSeq);
            pstmt.setLong(2, boardSeq);
            ResultSet rs = pstmt.executeQuery();
            result = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }
        return result;
    }
}
