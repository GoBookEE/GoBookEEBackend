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
	
	
	public Notice noticeBySeq(Long noticeSeq) {
		Connection conn = getConnection();
		Notice notice = dao.noticeBySeq(conn, noticeSeq);
		close(conn);
		return notice;
	}
	
	public int updateNotice(Notice n) {
		Connection conn = getConnection();
		int result = dao.updateNotice(conn, n);
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		return result;
	}
	
	public int writeNotice(Notice n) {
		Connection conn = getConnection();
		int result = dao.writeNotice(conn, n);
		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		return result;
	}
	
	public int deleteNotice(Long noticeSeq) {
		Connection conn = getConnection();
		int result = dao.deleteNotice(conn, noticeSeq);
		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		return result;
	}
}
