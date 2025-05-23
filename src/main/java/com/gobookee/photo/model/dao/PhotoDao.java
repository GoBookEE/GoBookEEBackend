package com.gobookee.photo.model.dao;

import com.gobookee.common.JDBCTemplate;
import com.gobookee.photo.model.dto.Photo;
import com.gobookee.place.model.dao.PlaceDao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class PhotoDao {
    private static PhotoDao photoDao;
    private Properties sqlProp = new Properties();
    private PreparedStatement pstmt;
    private ResultSet rs;
    private int result;


    private PhotoDao() {
        String path = PlaceDao.class.getResource("/config/photo-sql.properties").getPath();
        try (FileReader fr = new FileReader(path)) {
            sqlProp.load(fr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PhotoDao photoDao() {
        if (photoDao == null) {
            photoDao = new PhotoDao();
        }
        return photoDao;
    }

    public int createPhoto(Connection conn, Photo photo) {
        pstmt = null;
        rs = null;
        result = 0;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("createPhoto"));
            pstmt.setLong(1, photo.getPhotoBoardSeq());
            pstmt.setString(2, photo.getPhotoRenamedName());
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(rs);
            JDBCTemplate.close(pstmt);
        }
        return result;
    }
}
