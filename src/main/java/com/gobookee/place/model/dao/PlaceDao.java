package com.gobookee.place.model.dao;

import com.gobookee.place.model.dto.Place;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

import static com.gobookee.common.JDBCTemplate.close;

public class PlaceDao {
    private static PlaceDao placeDao;
    private Properties sqlProp = new Properties();
    private PreparedStatement pstmt;
    private ResultSet rs;
    private List<Place> placeList;
    private int result;

    private PlaceDao() {
        String path = PlaceDao.class.getResource("/config/place-sql.properties").getPath();
        try (FileReader fr = new FileReader(path)) {
            sqlProp.load(fr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlaceDao placeDao() {
        if (placeDao == null) {
            placeDao = new PlaceDao();
        }
        return placeDao;
    }


    public int insertPlaceAndReturnId(Connection conn, Place place) {
        pstmt = null;
        rs = null;
        result = 0;
        int generatedId = -1;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("getNextBoardSeq"));
            rs = pstmt.executeQuery();

            if (rs.next()) {
                generatedId = rs.getInt(1);
            }

            if (generatedId != -1) {
                pstmt = conn.prepareStatement(sqlProp.getProperty("createPlace"));
                pstmt.setInt(1, generatedId);
                pstmt.setString(2, place.getPlaceTitle());
                pstmt.setString(3, place.getPlaceContent());
                pstmt.setString(4, place.getPlaceAddress());
                pstmt.setDouble(5, place.getPlaceLatitude());
                pstmt.setDouble(6, place.getPlaceLongitude());
                pstmt.setLong(7, place.getUserSeq());
                result = pstmt.executeUpdate();
                if (result == 0) {
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return generatedId;
    }
}
