package com.gobookee.studygroup.service;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.getConnection;

import java.sql.Connection;
import java.util.List;

import com.gobookee.common.JDBCTemplate;
import com.gobookee.study.model.dto.StudyList;
import com.gobookee.study.service.StudyService;
import com.gobookee.studygroup.model.dao.StudyGroupDao;

public class StudyGroupService {

	private StudyGroupDao dao = StudyGroupDao.studyGroupDao();
	
	private static final StudyGroupService SERVICE = new StudyGroupService();
	
	Connection conn;
	
	public static StudyGroupService studyGroupService() {
		return SERVICE;
	}
	
	public List<StudyList> getGroupStudiesByTime(Long userseq, int cPage, int numPerpage){
		conn = JDBCTemplate.getConnection();
		List<StudyList> result = dao.getGroupStudiesByTime(conn, userseq, cPage, numPerpage);
		JDBCTemplate.close(conn);
		return result;
	}
	
	public List<StudyList> getGroupStudiesByRec(Long userseq, int cPage, int numPerpage){
		conn = JDBCTemplate.getConnection();
		List<StudyList> result = dao.getGroupStudiesByRec(conn, userseq, cPage, numPerpage);
		JDBCTemplate.close(conn);
		return result;
	}
	
	public int allStudyGroupsCount(Long userseq) {
		conn = getConnection();
		int totalData = dao.allStudyGroupsCount(conn, userseq);
		close(conn);
		return totalData;
	}
	
	public List<StudyList> getGroupStudiesByTimeEnd(Long userseq, int cPage, int numPerpage){
		conn = JDBCTemplate.getConnection();
		List<StudyList> result = dao.getGroupStudiesByTimeEnd(conn, userseq, cPage, numPerpage);
		JDBCTemplate.close(conn);
		return result;
	}
	
	public List<StudyList> getGroupStudiesByRecEnd(Long userseq, int cPage, int numPerpage){
		conn = JDBCTemplate.getConnection();
		List<StudyList> result = dao.getGroupStudiesByRecEnd(conn, userseq, cPage, numPerpage);
		JDBCTemplate.close(conn);
		return result;
	}
	
	public int allStudyGroupsCountEnd(Long userseq) {
		conn = getConnection();
		int totalData = dao.allStudyGroupsCountEnd(conn, userseq);
		close(conn);
		return totalData;
	}
}
