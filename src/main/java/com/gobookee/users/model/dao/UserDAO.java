package com.gobookee.users.model.dao;

import com.gobookee.common.JDBCTemplate;
import com.gobookee.users.model.dto.Gender;
import com.gobookee.users.model.dto.User;
import com.gobookee.users.model.dto.UserType;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


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
            pstmt.setString(3, u.getUserName());
            pstmt.setString(4, u.getUserGender().toString());
            pstmt.setString(5, u.getUserPhone());
            pstmt.setString(6, u.getUserProfile());
            pstmt.setString(7, u.getUserIntro());
            pstmt.setString(8, u.getUserType().toString());
            pstmt.setString(9, u.getUserEmail());
            pstmt.setTimestamp(10, u.getUserCreateTime());
            pstmt.setTimestamp(11, u.getUserDeleteTime());
            pstmt.setString(12, u.getUserAddress());
            pstmt.setString(13, u.getUserAddressDetail());

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(pstmt);
        }

        return result;
    }

    public User searchUserById(Connection conn, String userId) {
        User u = null;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("searchById"));
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) u = getUser(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(rs);
            JDBCTemplate.close(pstmt);
        }
        return u;
    }

    private User getUser(ResultSet rs) throws SQLException {
        Gender gender;
        if (rs.getString("user_gender").charAt(0) == 'M') {
            gender = Gender.MALE;
        } else {
            gender = Gender.FEMALE;
        }

        UserType userType;
        if (rs.getString("user_type").charAt(0) == '0') {
            userType = UserType.USER;
        } else {
            userType = UserType.OWNER;
        }

        return User.builder()
                .UserSeq(rs.getLong("user_seq"))
                .UserId(rs.getString("user_id"))
                .UserPwd(rs.getString("user_pwd"))
                .UserName(rs.getString("user_name"))
                .UserGender(gender)
                .UserPhone(rs.getString("user_phone"))
                .UserProfile(rs.getString("user_profile"))
                .UserIntro(rs.getString("user_intro"))
                .UserType(userType)
                .UserEmail(rs.getString("user_email"))
                .UserCreateTime(rs.getTimestamp("user_create_time"))
                .UserDeleteTime(rs.getTimestamp("user_delete_time"))
                .build();
    }

}
