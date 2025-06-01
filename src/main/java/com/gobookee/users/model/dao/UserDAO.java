package com.gobookee.users.model.dao;

import com.gobookee.users.model.dto.Gender;
import com.gobookee.users.model.dto.User;
import com.gobookee.users.model.dto.UserType;

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
            if (rs.next()) u = getUser(rs);
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
            if (rs.next()) u = getUser(rs);
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

    //resultset의 결과를 유저DTO로 변환해주는 기능
    private User getUser(ResultSet rs) throws SQLException {
        return User.builder()
                .UserSeq(rs.getLong("user_seq"))
                .UserId(rs.getString("user_id"))
                .UserPwd(rs.getString("user_pwd"))
                .UserNickName(rs.getString("user_nickname"))
                .UserGender(parseGender(rs.getString("user_gender")))
                .UserPhone(rs.getString("user_phone"))
                .UserProfile(rs.getString("user_profile"))
                .UserIntro(rs.getString("user_intro"))
                .UserType(parseUserType(rs.getString("user_type")))
                .UserEmail(rs.getString("user_email"))
                .UserCreateTime(rs.getTimestamp("user_create_time"))
                .UserDeleteTime(rs.getTimestamp("user_delete_time"))
                .UserSpeed(rs.getLong("user_speed"))
                .build();
    }

    private Gender parseGender(String value) {
        if (value == null) return null;
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
        if (value == null) return null;
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
