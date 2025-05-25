package com.gobookee.schedule.service;

import com.gobookee.schedule.model.dao.ScheduleDao;

import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;

import static com.gobookee.common.JDBCTemplate.*;

public class ScheduleService {
    private static ScheduleService scheduleService;
    private ScheduleDao scheduleDao = ScheduleDao.scheduleDao();
    private Connection conn;

    private ScheduleService() {
    }

    public static ScheduleService scheduleService() {
        if (scheduleService == null) {
            scheduleService = new ScheduleService();
        }
        return scheduleService;
    }


    public boolean insertSchedule(HashMap requestParam) {
        conn = getConnection();
        int result = scheduleDao.insertSchedule(conn, requestParam);
        if (result > 0) {
            commit(conn);
            close(conn);
            return true;
        }
        rollback(conn);
        close(conn);
        return false;
    }

    public int getStudyCountByPlaceSeqAndDate(Long placeSeq, Date date) {
        conn = getConnection();
        int count = scheduleDao.getStudyCountByPlaceSeqAndDate(conn, placeSeq, date);
        close(conn);
        return count;
    }

}
