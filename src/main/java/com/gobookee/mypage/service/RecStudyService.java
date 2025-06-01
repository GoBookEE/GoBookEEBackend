package com.gobookee.mypage.service;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.getConnection;
import static com.gobookee.mypage.model.dao.RecStudyDAO.recStudyDao;

import java.sql.Connection;
import java.util.List;

import com.gobookee.mypage.model.dao.RecStudyDAO;
import com.gobookee.mypage.model.dto.MyStudy;
import com.gobookee.mypage.model.dto.RecStudy;
import com.gobookee.study.model.dto.StudyList;

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

	public List<StudyList> getAppliedByStatus(MyStudy mystudy) {
		Connection conn = getConnection();
		List<StudyList> studies = dao.getAppliedByStatus(conn, mystudy);
		close(conn);
		return studies;
	}

	public int countAppliedByStatus(MyStudy mystudy) {
		Connection conn = getConnection();
		int totalData = dao.countAppliedByStatus(conn, mystudy);
		close(conn);
		return totalData;
	}

	public List<StudyList> getCreatedByStatus(MyStudy mystudy) {
		Connection conn = getConnection();
		List<StudyList> studies = dao.getCreatedByStatus(conn, mystudy);
		close(conn);
		return studies;
	}

	public int countCreatedByStatus(MyStudy mystudy) {
		Connection conn = getConnection();
		int totalData = dao.countCreatedByStatus(conn, mystudy);
		close(conn);
		return totalData;
	}
}
