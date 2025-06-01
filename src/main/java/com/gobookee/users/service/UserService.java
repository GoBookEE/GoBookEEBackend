package com.gobookee.users.service;

import com.gobookee.users.model.dao.UserDAO;
import com.gobookee.users.model.dto.User;
import com.gobookee.users.model.dto.UserUpdateProfile;

import java.sql.Connection;
import java.util.List;

import static com.gobookee.common.JDBCTemplate.*;
import static com.gobookee.users.model.dao.UserDAO.userDao;

public class UserService {

	private static final UserService SERVICE = new UserService();

	private UserService() {
	}

	public static UserService userService() {
		return SERVICE;
	}

	private UserDAO dao = userDao();

	public int insertUser(User u) {
		Connection conn = getConnection();
		int result = dao.insertUser(conn, u);
		if (result > 0)
			commit(conn);
		else
			rollback(conn);
		close(conn);
		return result;
	}

	public User searchUserById(String userId) {
		Connection conn = getConnection();
		User u = userDao().searchUserById(conn, userId);
		close(conn);
		return u;
	}

	public User searchUserByNickName(String userNickname) {
		Connection conn = getConnection();
		User u = userDao().searchUserByNickName(conn, userNickname);
		close(conn);
		return u;
	}

	public List<String> searchUserIdByEmail(String email) {
		Connection conn = getConnection();
		List<String> idList = userDao().searchUserIdByEmail(conn, email);
		close(conn);
		return idList;
	}

	public boolean searchUserByIdAndEmail(String userId, String email) {
		Connection conn = getConnection();
		int result = userDao().searchUserByIdAndEmail(conn, userId, email);
		close(conn);
		return result == 1;
	}

	public boolean changePwd(String userId, String newPassword) {
		Connection conn = getConnection();
		int result = userDao().changePwd(conn, userId, newPassword);
		close(conn);
		return result == 1;
	}

	public boolean updateProfile(UserUpdateProfile user) {
		Connection conn = getConnection();
		int result = userDao().updateProfile(conn, user);
		close(conn);
		return result == 1;
	}

	public List<User> getSpeedRanking(int cPage, int numPerPage) {
		Connection conn = getConnection();
		List<User> userList = userDao().getSpeedRanking(conn, cPage, numPerPage);
		close(conn);
		return userList;
	}

	public int countSpeedRanking() {
		Connection conn = getConnection();
		int totalData = dao.countSpeedRanking(conn);
		close(conn);
		return totalData;
	}

	public int getUserRank(Long userSeq) {
		Connection conn = getConnection();
		int result = dao.getUserRank(conn, userSeq);
		close(conn);
		return result;
	}

}