package com.gobookee.review.service;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.commit;
import static com.gobookee.common.JDBCTemplate.getConnection;
import static com.gobookee.common.JDBCTemplate.rollback;
import static com.gobookee.review.model.dao.CommentsDAO.commentsDao;

import java.sql.Connection;

import com.gobookee.review.model.dao.CommentsDAO;
import com.gobookee.review.model.dto.Comments;

public class CommentsService {

	private static final CommentsService SERVICE = new CommentsService();

	private CommentsDAO cdao = commentsDao();

	private CommentsService() {
	}
	
	public static CommentsService commentsService() {
		return SERVICE;
	}
	
	public int insertComment(Comments dto) {
		Connection conn= getConnection();
		int result=cdao.insertComment(conn, dto);
		if(result>0) {
			commit(conn);
		}
		else {
			rollback(conn);
		}
		close(conn);
		return result;
	}
	
	public int updateComment(long commentSeq, long userSeq, String newContent) {
        Connection conn = getConnection();
        int result = cdao.updateComment(conn, commentSeq, userSeq, newContent);
        if (result > 0) {
        	commit(conn);
        }
        else {
        	rollback(conn);
        }
        close(conn);
        return result;
    }

}
