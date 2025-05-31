package com.gobookee.mypage.service;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.getConnection;
import static com.gobookee.mypage.model.dao.RecStudyDAO.recStudyDao;

import java.sql.Connection;
import java.util.List;

import com.gobookee.mypage.model.dao.RecStudyDAO;
import com.gobookee.mypage.model.dto.RecStudy;

public class RecStudyService {

	private static final RecStudyService SERVICE = new RecStudyService();

	private RecStudyDAO dao = recStudyDao();

	private RecStudyService() {
	}

	public static RecStudyService recStudyService() {
		return SERVICE;
	}

	public List<RecStudy> getStudiesRecByUser(Long userSeq, int cPage, int numPerPage) {
		Connection conn = getConnection();
		List<RecStudy> studies = dao.getStudiesRecByUser(conn, userSeq, cPage, numPerPage);
		close(conn);
		return studies;
	}

	public int countStudiesRecByUser(Long userSeq) {
		Connection conn = getConnection();
		int totalData = dao.countStudiesRecByUser(conn, userSeq);
		close(conn);
		return totalData;
	}
}
