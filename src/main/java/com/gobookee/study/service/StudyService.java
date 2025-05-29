package com.gobookee.study.service;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.commit;
import static com.gobookee.common.JDBCTemplate.getConnection;
import static com.gobookee.common.JDBCTemplate.rollback;

import java.sql.Connection;
import java.util.List;

import com.gobookee.common.JDBCTemplate;
import com.gobookee.photo.model.dao.PhotoDao;
import com.gobookee.photo.model.dto.Photo;
import com.gobookee.study.model.dao.StudyDao;
import com.gobookee.study.model.dto.SearchStudyResponse;
import com.gobookee.study.model.dto.StudyInsert;
import com.gobookee.study.model.dto.StudyList;
import com.gobookee.study.model.dto.StudyView;

public class StudyService {

    private StudyDao dao = StudyDao.studyDao();
    private PhotoDao photodao = PhotoDao.photoDao();
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
    
    public boolean insertStudy(StudyInsert studyinsert, List<String> fileList){
        Connection conn = getConnection();
        boolean isSuccess = false;
        
        try {
        	long boardSeq = dao.insertStudyAndReturnId(conn, studyinsert);
            if (boardSeq == -1) {
                rollback(conn);
                return false;
            }
            
            int insertedPhotos = 0;
            for (String fileName : fileList) {
                Photo photo = Photo.builder()
                        .photoRenamedName(fileName)
                        .photoBoardSeq(boardSeq)
                        .build();
                insertedPhotos += photodao.createPhoto(conn, photo);
            }

            //사진 등록 다 성공 시 트랜잭션 커밋처리 하나라도 실패 시 롤백처리
            if (insertedPhotos != fileList.size()) {
                rollback(conn);
                return false;
            }
            isSuccess = true;
            commit(conn);
        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
        } finally {
            close(conn);
        }
        return isSuccess;
    }
    
    public List<StudyView> getStudyView (Long studySeq){
    	conn = getConnection();
    	List<StudyView> studyview = dao.getStudyView(conn, studySeq);
    	close(conn);
    	return studyview;
    }
    
    public List<StudyView> getStudyViewUser (Long studySeq){
    	conn = getConnection();
    	List<StudyView> studyviewuser = dao.getStudyViewUser(conn, studySeq);
    	close(conn);
    	return studyviewuser;
    }
    
}
