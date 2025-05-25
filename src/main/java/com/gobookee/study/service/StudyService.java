package com.gobookee.study.service;

import com.gobookee.common.JDBCTemplate;
import com.gobookee.study.model.dao.StudyDao;
import com.gobookee.study.model.dto.SearchStudyResponse;
import com.gobookee.study.model.dto.StudyList;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.getConnection;

public class StudyService {

    private StudyDao dao = StudyDao.studyDao();
    private Connection conn;

    private static final StudyService SERVICE = new StudyService();

    public static StudyService studyService() {
        return SERVICE;
    }

    public List<StudyList> getStudiesByTime(int cPage, int numPerpage) {
        Connection conn = JDBCTemplate.getConnection();
        List<StudyList> result = dao.getStudiesByTime(conn, cPage, numPerpage);
        close(conn);
        return result;
    }

    public List<StudyList> getStudiesByRec(int cPage, int numPerpage) {
        Connection conn = JDBCTemplate.getConnection();
        List<StudyList> result = dao.getStudiesByRec(conn, cPage, numPerpage);
        close(conn);
        return result;
    }

    public int allStudyCount() {
        Connection conn = getConnection();
        int totalData = dao.allStudyCount(conn);
        close(conn);
        return totalData;
    }

    public List<SearchStudyResponse> getStudyListByHostUserSeq(Long userSeq) {
        conn = getConnection();
        List<SearchStudyResponse> studyList = dao.getStudyListByHostUserSeq(conn, userSeq);
        close(conn);
        return studyList;
    }

    public int getStudyCountByPlaceSeqAndDate(Long placeSeq, Date date) {
        conn = getConnection();
        int count = dao.getStudyCountByPlaceSeqAndDate(conn, placeSeq, date);
        close(conn);
        return count;
    }
}
