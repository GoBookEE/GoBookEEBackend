package com.gobookee.schedule.service;

import com.gobookee.place.model.dao.PlaceDao;
import com.gobookee.place.model.dto.PlaceAddress;
import com.gobookee.schedule.model.dao.ScheduleDao;
import com.gobookee.schedule.model.dto.ScheduleConfirm;
import com.gobookee.schedule.model.dto.ScheduleReserve;

import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import static com.gobookee.common.JDBCTemplate.*;

public class ScheduleService {
    private static ScheduleService scheduleService;
    private ScheduleDao scheduleDao = ScheduleDao.scheduleDao();
    private PlaceDao placeDao = PlaceDao.placeDao();
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

    public List<ScheduleReserve> getStudyListPlaceSeqAndDate(Long placeSeq, Date date) {
        conn = getConnection();
        List<ScheduleReserve> scheduleList = scheduleDao.getStudyListPlaceSeqAndDate(conn, placeSeq, date);
        close(conn);
        return scheduleList;
    }

    public boolean confirmSchedule(ScheduleConfirm scheduleConfirm) {
        conn = getConnection();
        boolean result = false;
        try {
            PlaceAddress placeAddress = placeDao.getPlaceAddressBySeq(conn, scheduleConfirm.getPlaceSeq());
            result = scheduleDao.confirmSchedule(conn, scheduleConfirm, placeAddress);
            if (!result) {
                rollback(conn);
                return false;
            }
            commit(conn);
        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
        } finally {
            close(conn);
        }
        return result;
    }
}
