package com.gobookee.study.service;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.getConnection;

import java.sql.Connection;
import java.util.List;

import com.gobookee.common.JDBCTemplate;
import com.gobookee.study.model.dao.StudyDao;
import com.gobookee.study.model.dto.StudyList;

public class StudyService {
	
	private StudyDao dao = StudyDao.studyDao();
	
	private static final StudyService SERVICE = new StudyService();
	
	public static StudyService studyService() {
		return SERVICE;
	}
	
	public List<StudyList> getStudiesByTime(int cPage, int numPerpage){
		Connection conn = JDBCTemplate.getConnection();
		List<StudyList> result = dao.getStudiesByTime(conn, cPage, numPerpage);
		JDBCTemplate.close(conn);
		return result;
	}
	
	public List<StudyList> getStudiesByRec(int cPage, int numPerpage){
		Connection conn = JDBCTemplate.getConnection();
		List<StudyList> result = dao.getStudiesByRec(conn, cPage, numPerpage);
		JDBCTemplate.close(conn);
		return result;
	}
	
	public int allStudyCount() {
		Connection conn = getConnection();
		int totalData = dao.allStudyCount(conn);
		close(conn);
		return totalData;
	}
	  
}
