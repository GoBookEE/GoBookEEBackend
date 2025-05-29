package com.gobookee.notice.model.service;

import java.sql.Connection;
import java.util.List;
import static com.gobookee.common.JDBCTemplate.getConnection;
import static com.gobookee.common.JDBCTemplate.commit;
import static com.gobookee.common.JDBCTemplate.rollback;
import static com.gobookee.common.JDBCTemplate.close;

import com.gobookee.notice.model.dao.NoticeDAO;
import com.gobookee.notice.model.dto.Notice;

public class NoticeService {

	private static final NoticeService SERVICE = new NoticeService();
	private NoticeService() {}
	public static NoticeService noticeService() {
		return SERVICE;
	}
	
	private NoticeDAO dao = NoticeDAO.noticeDao();
	
	public List<Notice> searchNoticeAll(int cPage, int numPerpage) {
		Connection conn = getConnection();
		List<Notice> result = dao.searchNoticeAll(conn, cPage, numPerpage);
		close(conn);
		return result;
	}
	
	public int noticeCount() {
		Connection conn = getConnection();
		int totalData = dao.noticeCount(conn);
		close(conn);
		return totalData;
	}
}
