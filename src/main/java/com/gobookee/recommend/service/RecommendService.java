package com.gobookee.recommend.service;

import com.gobookee.recommend.model.dao.RecommendDAO;
import com.gobookee.recommend.model.dto.Recommend;
import com.gobookee.reports.model.dao.ReportsDAO;
import com.gobookee.users.model.dao.UserDAO;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import static com.gobookee.common.JDBCTemplate.*;
import static com.gobookee.recommend.model.dao.RecommendDAO.recommendDao;

public class RecommendService {

    private static final RecommendService SERVICE = new RecommendService();

    private RecommendDAO dao = recommendDao();
    private UserDAO userDAO = UserDAO.userDao();
    private ReportsDAO rdao= ReportsDAO.reportsDao();

    private RecommendService() {
    }

    public static RecommendService recommendService() {
        return SERVICE;
    }

    public Map<String, Object> toggleRecommend(Long userSeq, Long boardSeq, String newType, String boardType) {
        Connection conn = getConnection();
        Map<String, Object> result = new HashMap<>();
        try {
            Recommend rec = dao.getRecommendBySeq(conn, userSeq, boardSeq);
            int speedChange = 0;
            if (rec != null && newType.equals(rec.getRecType())) {
                // 같은 버튼 다시 누름 → 토글 취소
                dao.delete(conn, rec.getRecSeq());
                speedChange = "0".equals(newType) ? -1 : +1;
            } else {
                // 상태 변경 or 새로 누름
                if (rec != null) {
                    speedChange += "0".equals(rec.getRecType()) ? -1 : +1; // 기존 것 취소
                }
                speedChange += "0".equals(newType) ? +1 : -1; // 새로 누른 것 반영
                dao.merge(conn, userSeq, boardSeq, newType);
            }

            // 점수 업데이트
            if (speedChange != 0) {
            	userDAO.updateUserSpeed(conn, rdao.getUserSeq(conn, boardSeq, boardType), speedChange);
            }

            result.put("recommendCount", dao.countByType(conn, boardSeq, "0"));
            result.put("nonRecommendCount", dao.countByType(conn, boardSeq, "1"));

            commit(conn);
        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
        } finally {
            close(conn);
        }

        return result;
    }

}
