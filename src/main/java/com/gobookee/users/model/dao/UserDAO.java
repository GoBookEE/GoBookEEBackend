package com.gobookee.users.model.dao;

import com.gobookee.common.JDBCTemplate;
import com.gobookee.users.model.dto.Gender;
import com.gobookee.users.model.dto.User;
import com.gobookee.users.model.dto.UserType;
import com.gobookee.users.model.dto.UserUpdateProfile;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.gobookee.common.JDBCTemplate.close;

public class UserDAO {
	private Properties sqlProp = new Properties();
	private PreparedStatement pstmt;
	private ResultSet rs;

	private static final UserDAO userDao = new UserDAO();

	private UserDAO() {
		String path = UserDAO.class.getResource("/config/users-sql.properties").getPath();
		try (FileReader fr = new FileReader(path)) {
			sqlProp.load(fr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static UserDAO userDao() {
		return userDao;
	}

	public int insertUser(Connection conn, User u) {
		int result = 0;
		PreparedStatement pstmt = null;

		try {
			String sql = sqlProp.getProperty("insertUser");
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, u.getUserId());
			pstmt.setString(2, u.getUserPwd());
			pstmt.setString(3, u.getUserNickName());
			pstmt.setString(4, u.getUserGender().toString());
			pstmt.setString(5, u.getUserPhone());
			pstmt.setString(6, u.getUserAddress());
			pstmt.setString(7, u.getUserProfile());
			pstmt.setString(8, u.getUserIntro());
			pstmt.setString(9, u.getUserType().toString());
			pstmt.setString(10, u.getUserEmail());
			result = pstmt.executeUpdate();
			System.out.println(result);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}

		return result;
	}

	public User searchUserById(Connection conn, String userId) {
		User u = null;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("searchById"));
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if (rs.next())
				u = getUser(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return u;
	}

	public User searchUserByNickName(Connection conn, String userNickname) {
		User u = null;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("searchByNickName"));
			pstmt.setString(1, userNickname);
			rs = pstmt.executeQuery();
			if (rs.next())
				u = getUser(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return u;
	}

	public void updateUserSpeed(Connection conn, Long userSeq, int change) {
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("updateUserSpeed"));
			pstmt.setInt(1, change);
			pstmt.setLong(2, userSeq);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
	}

	public List<String> searchUserIdByEmail(Connection conn, String email) {
		pstmt = null;
		rs = null;
		List<String> idList = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("findUserIdByUserEmail"));
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				idList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return idList;
	}

	public int searchUserByIdAndEmail(Connection conn, String userId, String email) {
		int result = 0;
		pstmt = null;
		rs = null;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("searchUserByIdAndEmail"));
			pstmt.setString(1, userId);
			pstmt.setString(2, email);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return result;
	}

	public int changePwd(Connection conn, String userId, String newPassword) {
		int result = 0;
		pstmt = null;
		rs = null;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("changePwd"));
			pstmt.setString(1, newPassword);
			pstmt.setString(2, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public int updateProfile(Connection conn, UserUpdateProfile user) {
		int result = 0;
		pstmt = null;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("updateProfile"));
			pstmt.setString(1, user.getUserNickname());
			pstmt.setString(2, user.getUserPwd());
			pstmt.setString(3, user.getUserPhone());
			pstmt.setString(4, user.getProfileImage());
			pstmt.setLong(5, user.getUserSeq());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	public List<User> getSpeedRanking(Connection conn, int cPage, int numPerPage) {
		pstmt = null;
		rs = null;
		List<User> userList = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getSpeedRanking"));
			pstmt.setInt(1, (cPage - 1) * numPerPage + 1);
			pstmt.setInt(2, cPage * numPerPage);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				User user = User.builder().userSeq(rs.getLong("user_seq")).userNickName(rs.getString("user_nickname"))
						.userProfile(rs.getString("user_profile")).userSpeed(rs.getLong("user_speed"))
						.rnum(rs.getInt("rnum")).build();
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return userList;
	}

	public int countSpeedRanking(Connection conn) {
		pstmt = null;
		rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("countSpeedRanking"));
			rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	public int getUserRank(Connection conn, Long userSeq) {
		pstmt = null;
		rs = null;
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getUserRank"));
			pstmt.setLong(1, userSeq);
			rs = pstmt.executeQuery();
			if (rs.next())
				result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return result;
	}

	// resultset의 결과를 유저DTO로 변환해주는 기능
	private User getUser(ResultSet rs) throws SQLException {
		return User.builder().userSeq(rs.getLong("user_seq")).userId(rs.getString("user_id"))
				.userPwd(rs.getString("user_pwd")).userNickName(rs.getString("user_nickname"))
				.userGender(parseGender(rs.getString("user_gender"))).userPhone(rs.getString("user_phone"))
				.userProfile(rs.getString("user_profile")).userIntro(rs.getString("user_intro"))
				.userType(parseUserType(rs.getString("user_type"))).userEmail(rs.getString("user_email"))
				.userCreateTime(rs.getTimestamp("user_create_time")).userDeleteTime(rs.getTimestamp("user_delete_time"))
				.userSpeed(rs.getLong("user_speed")).build();
	}

	private Gender parseGender(String value) {
		if (value == null)
			return null;
		switch (value.toUpperCase()) {
		case "M":
			return Gender.M;
		case "F":
			return Gender.F;
		default:
			throw new IllegalArgumentException("GENDER: " + value);
		}
	}

	private UserType parseUserType(String value) {
		if (value == null)
			return null;
		switch (value) {
		case "0":
			return UserType.USER;
		case "1":
			return UserType.OWNER;
		default:
			throw new IllegalArgumentException("TYPE : " + value);
		}
	}
}
