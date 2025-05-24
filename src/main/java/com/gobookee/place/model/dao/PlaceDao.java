package com.gobookee.place.model.dao;

import com.gobookee.place.model.dto.Place;
import com.gobookee.place.model.dto.PlaceListResponse;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static com.gobookee.common.JDBCTemplate.close;

public class PlaceDao {
    private static PlaceDao placeDao;
    private final Properties sqlProp = new Properties();
    private PreparedStatement pstmt;
    private ResultSet rs;

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
        int result = 0;
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
                pstmt.setString(3, place.getPlaceContents());
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

    public int placeCount(Connection conn) {
        pstmt = null;
        rs = null;
        int dataCnt = 0;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("getPlaceCount"));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                dataCnt = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(rs);
            close(pstmt);
        }
        return dataCnt;
    }

    public List<PlaceListResponse> getAllPlaceList(Connection conn, HashMap requestParam) {
        pstmt = null;
        rs = null;
        List<PlaceListResponse> placeList = new ArrayList<>();
        int cPage = (int) requestParam.get("cPage");
        int numPerPage = (int) requestParam.get("numPerPage");
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("getPlaceList"));
            pstmt.setInt(1, (cPage - 1) * numPerPage + 1);
            pstmt.setInt(2, cPage * numPerPage);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                placeList.add(getPlaceResponse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return placeList;
    }

    public List<PlaceListResponse> getAllPlaceListByRec(Connection conn, HashMap requestParam) {
        pstmt = null;
        rs = null;
        List<PlaceListResponse> placeList = new ArrayList<>();
        int cPage = (int) requestParam.get("cPage");
        int numPerPage = (int) requestParam.get("numPerPage");
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("getPlaceListByRec"));
            pstmt.setInt(1, (cPage - 1) * numPerPage + 1);
            pstmt.setInt(2, cPage * numPerPage);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                placeList.add(getPlaceResponse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return placeList;
    }

    private Place getPlace(ResultSet rs) throws SQLException {
        return Place.builder()
                .placeSeq(rs.getLong("PLACE_SEQ"))
                .placeTitle(rs.getString("PLACE_TITLE"))
                .placeContents(rs.getString("PLACE_CONTENTS"))
                .placeCreateTime(rs.getTimestamp("PLACE_CREATE_TIME"))
                .placeAddress(rs.getString("PLACE_ADDRESS"))
                .placeLatitude(rs.getDouble("PLACE_LATITUDE"))
                .placeLongitude(rs.getDouble("PLACE_LONGITUDE"))
                .placeIsPublic(rs.getString("PLACE_IS_PUBLIC").charAt(0))
                .userSeq(rs.getLong("USER_SEQ"))
                .build();
    }

    private PlaceListResponse getPlaceResponse(ResultSet rs) throws SQLException {
        return PlaceListResponse.builder()
                .placeTitle(rs.getString("PLACE_TITLE"))
                .placeContents(rs.getString("PLACE_CONTENTS"))
                .placeAddress(rs.getString("PLACE_ADDRESS"))
                .userNickname(rs.getString("WRITER_NICKNAME"))
                .recommendCount(rs.getLong("RECOMMEND_COUNT"))
                .build();
    }
}
