package com.gobookee.recommend.service;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.commit;
import static com.gobookee.common.JDBCTemplate.getConnection;
import static com.gobookee.common.JDBCTemplate.rollback;
import static com.gobookee.recommend.model.dao.RecommendDAO.recommendDao;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.gobookee.recommend.model.dao.RecommendDAO;

public class RecommendService {

	private static final RecommendService SERVICE = new RecommendService();

	private RecommendDAO dao = recommendDao();

	private RecommendService() {
	}

	public static RecommendService recommendService() {
		return SERVICE;
	}

	public Map<String, Object> toggleRecommend(Long userSeq, Long boardSeq) {
		Connection conn = getConnection();
		Map<String, Object> result = new HashMap<>();

		try {
			if (dao.exists(conn, userSeq, boardSeq, "0")) {// 추천이 있는상태면
				dao.delete(conn, userSeq, boardSeq); // 추천 취소
			} else { // 추천이 없는 상태여도
				dao.delete(conn, userSeq, boardSeq); // 비추천도 있을 수 있으니 삭제
				dao.insert(conn, userSeq, boardSeq, "0"); // 추천등록
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

	public Map<String, Object> toggleNonRecommend(Long userSeq, Long boardSeq) {
		Connection conn = getConnection();
		Map<String, Object> result = new HashMap<>();
		try {
			if (dao.exists(conn, userSeq, boardSeq, "1")) {
				dao.delete(conn, userSeq, boardSeq);
			} else {
				dao.delete(conn, userSeq, boardSeq);
				dao.insert(conn, userSeq, boardSeq, "1");
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
