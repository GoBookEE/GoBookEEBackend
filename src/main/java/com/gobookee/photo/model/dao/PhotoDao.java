package com.gobookee.photo.model.dao;

import com.gobookee.photo.model.dto.Photo;
import com.gobookee.place.model.dao.PlaceDao;

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
            close(rs);
            close(pstmt);
        }
        return result;
    }

    public List<String> getPhotoListByBoardSeq(Connection conn, Long placeSeq) {
        pstmt = null;
        rs = null;
        List<String> photoList = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("getPhotoListByBoardSeq"));
            pstmt.setLong(1, placeSeq);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                photoList.add(rs.getString("PHOTO_RENAMED_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return photoList;
    }
}
