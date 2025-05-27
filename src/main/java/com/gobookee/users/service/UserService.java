package com.gobookee.users.service;

import static com.gobookee.common.JDBCTemplate.getConnection;
import static com.gobookee.common.JDBCTemplate.commit;
import static com.gobookee.common.JDBCTemplate.rollback;
import static com.gobookee.common.JDBCTemplate.close;

import java.sql.Connection;

import com.gobookee.users.model.dao.UserDAO;
import com.gobookee.users.model.dto.User;

public class UserService {
		
	private static final UserService SERVICE=new UserService();
	
	private UserService() {}
	public static UserService userService() {
		return SERVICE;
	}
	
	private UserDAO dao=UserDAO.userDao();
		
	public int insertUser(User u) {
		Connection conn=getConnection();
		int result=dao.insertUser(conn,u);
		if(result>0) commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}
	
	public User searchUserById(String userId) {
		Connection conn=getConnection();
		User u=UserDAO.userDao().searchUserById(conn,userId);
		close(conn);
		return u;
	}
	
	
	public User searchUserByNickName (String userNickname) {
		Connection conn = getConnection();
		User u = UserDAO.userDao().searchUserByNickName(conn, userNickname);
		close(conn);
		return u;
	}
	
}
