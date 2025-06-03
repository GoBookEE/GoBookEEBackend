package com.gobookee.schedule.model.dao;

import com.gobookee.place.model.dto.PlaceAddress;
import com.gobookee.schedule.model.dto.ScheduleConfirm;
import com.gobookee.schedule.model.dto.ScheduleReserve;
import com.gobookee.study.model.dao.StudyDao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public List<ScheduleReserve> getStudyListPlaceSeqAndDate(Connection conn, Long placeSeq, Date date) {
        pstmt = null;
        rs = null;
        List<ScheduleReserve> scheduleList = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("getStudyListByPlaceSeqAndDate"));
            pstmt.setLong(1, placeSeq);
            pstmt.setDate(2, date);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                scheduleList.add(ScheduleReserve.from(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return scheduleList;
    }

    public boolean confirmSchedule(Connection conn, ScheduleConfirm scheduleConfirm, PlaceAddress placeAddress) {
        int scheduleResult = updateSchedule(conn, scheduleConfirm);
        if (scheduleConfirm.getStatus() == 'Y') {
            int scheduleCount = getSchedulesByStudySeq(conn, scheduleConfirm);
            int deleteCount = deleteSchedulesByStudySeq(conn, scheduleConfirm);
            int studyResult = updateStudy(conn, scheduleConfirm, placeAddress);
            return scheduleResult == 1 && studyResult == 1 && (deleteCount == scheduleCount);
        }
        return scheduleResult == 1;
    }

    private int updateSchedule(Connection conn, ScheduleConfirm scheduleConfirm) {
        pstmt = null;
        int result = 0;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("confirmSchedule"));
            pstmt.setString(1, String.valueOf(scheduleConfirm.getStatus()));
            pstmt.setLong(2, scheduleConfirm.getScheduleSeq());
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }
        return result;
    }

    public int updateStudy(Connection conn, ScheduleConfirm scheduleConfirm, PlaceAddress placeAddress) {
        pstmt = null;
        int result = 0;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("confirmStudy"));
            pstmt.setDate(1, scheduleConfirm.getSqlDate());
            pstmt.setString(2, placeAddress.getPlaceAddress());
            pstmt.setDouble(3, placeAddress.getPlaceLatitude());
            pstmt.setDouble(4, placeAddress.getPlaceLongitude());
            pstmt.setLong(5, scheduleConfirm.getStudySeq());
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }
        return result;
    }

    private int deleteSchedulesByStudySeq(Connection conn, ScheduleConfirm scheduleConfirm) {
        pstmt = null;
        int result = 0;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("deleteScheduleByStudySeq"));
            pstmt.setLong(1, scheduleConfirm.getStudySeq());
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }
        return result;
    }

    private int getSchedulesByStudySeq(Connection conn, ScheduleConfirm scheduleConfirm) {
        rs = null;
        pstmt = null;
        int result = 0;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("getSchedulesByStudySeq"));
            pstmt.setLong(1, scheduleConfirm.getStudySeq());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return result;
    }
}
