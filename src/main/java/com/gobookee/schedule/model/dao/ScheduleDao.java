package com.gobookee.schedule.model.dao;

import com.gobookee.study.model.dao.StudyDao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

import static com.gobookee.common.JDBCTemplate.close;

public class ScheduleDao {
    private static ScheduleDao scheduleDao;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private Properties sqlProp = new Properties();

    private ScheduleDao() {
        String path = StudyDao.class.getResource("/config/schedule-sql.properties").getPath();
        try (FileReader fr = new FileReader(path)) {
            sqlProp.load(fr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ScheduleDao scheduleDao() {
        if (scheduleDao == null) {
            scheduleDao = new ScheduleDao();
        }
        return scheduleDao;
    }

    public int insertSchedule(Connection conn, HashMap requestParam) {
        pstmt = null;
        int result = 0;
        Long placeSeq = (Long) requestParam.get("placeSeq");
        Long studySeq = (Long) requestParam.get("studySeq");
        Date date = (Date) requestParam.get("date");
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("insertSchedule"));
            pstmt.setDate(1, date);
            pstmt.setLong(2, studySeq);
            pstmt.setLong(3, placeSeq);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }
        return result;
    }
}
