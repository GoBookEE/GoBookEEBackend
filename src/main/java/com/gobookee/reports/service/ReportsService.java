package com.gobookee.reports.service;

import com.gobookee.reports.model.dao.ReportsDAO;
import com.gobookee.users.model.dao.UserDAO;

import java.sql.Connection;

import static com.gobookee.common.JDBCTemplate.*;
import static com.gobookee.reports.model.dao.ReportsDAO.reportsDao;

public class ReportsService {
    private static final ReportsService SERVICE = new ReportsService();

    private ReportsDAO dao = reportsDao();
    private UserDAO userDAO = UserDAO.userDao();

    private ReportsService() {
    }

    public static ReportsService reportsService() {
        return SERVICE;
    }

    public int insertReport(Long userSeq, Long boardSeq, String reason, String boardType) {
        Connection conn = getConnection();
        int result = dao.insertReport(conn, userSeq, boardSeq, reason);
        if (result > 0) {
            int speedChange = 0;
            int reportCount = dao.countReports(conn, boardSeq);
            if (reportCount >= 5) {
                dao.hidePost(conn, boardSeq, boardType); // IS_PUBLIC = 'N'
                speedChange = -5;
            }
            if (speedChange != 0) {
            	userDAO.updateUserSpeed(conn, dao.getUserSeq(conn, boardSeq, boardType), speedChange);
            }
            commit(conn);
        } else {
            rollback(conn);
        }
        close(conn);
        return result;
    }

    public boolean hasReported(Long userSeq, Long boardSeq) {
        Connection conn = getConnection();
        boolean result = dao.exists(conn, userSeq, boardSeq);
        close(conn);
        return result;
    }

}
