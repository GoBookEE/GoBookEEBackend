package com.gobookee.reports.service;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.commit;
import static com.gobookee.common.JDBCTemplate.getConnection;
import static com.gobookee.common.JDBCTemplate.rollback;
import static com.gobookee.reports.model.dao.ReportsDAO.reportsDao;

import java.sql.Connection;

import com.gobookee.reports.model.dao.ReportsDAO;

public class ReportsService {
	private static final ReportsService SERVICE = new ReportsService();

	private ReportsDAO dao = reportsDao();

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
				dao.updateUserSpeed(conn, userSeq, speedChange);
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
