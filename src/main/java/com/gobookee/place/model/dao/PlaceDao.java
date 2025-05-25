package com.gobookee.place.model.dao;

import com.gobookee.place.model.dto.Place;
import com.gobookee.place.model.dto.PlaceListResponse;
import com.gobookee.place.model.dto.PlaceViewResponse;

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
        int generatedId = getNextBoardSeq(conn);
        if (generatedId == -1) {
            // 시퀀스 실패 시 즉시 반환 (INSERT 시도 X)
            return -1;
        }
        int result = insertPlace(conn, place, generatedId);
        return result > 0 ? generatedId : -1;
    }

    private int getNextBoardSeq(Connection conn) {
        int seq = -1;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("getNextBoardSeq"));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                seq = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return seq;
    }

    private int insertPlace(Connection conn, Place place, int seq) {
        int result = 0;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("createPlace"));
            pstmt.setInt(1, seq);
            pstmt.setString(2, place.getPlaceTitle());
            pstmt.setString(3, place.getPlaceContents());
            pstmt.setString(4, place.getPlaceAddress());
            pstmt.setDouble(5, place.getPlaceLatitude());
            pstmt.setDouble(6, place.getPlaceLongitude());
            pstmt.setLong(7, place.getUserSeq());
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }
        return result;
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
                placeList.add(PlaceListResponse.from(rs));
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
                placeList.add(PlaceListResponse.from(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return placeList;
    }

    public PlaceViewResponse getPlaceBySeq(Connection conn, Long placeSeq) {
        pstmt = null;
        rs = null;
        PlaceViewResponse place = null;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("getPlaceBySeq"));
            pstmt.setLong(1, placeSeq);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                place = PlaceViewResponse.from(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
        }
        return place;
    }

    public int deletePlace(Connection conn, Long placeSeq) {
        pstmt = null;
        int result = 0;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("deletePlace"));
            pstmt.setLong(1, placeSeq);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }
        return result;
    }

    public int updatePlace(Connection conn, Place updatePlace) {
        pstmt = null;
        int result = 0;
        try {
            pstmt = conn.prepareStatement(sqlProp.getProperty("updatePlace"));
            pstmt.setString(1, updatePlace.getPlaceTitle());
            pstmt.setString(2, updatePlace.getPlaceContents());
            pstmt.setString(3, updatePlace.getPlaceAddress());
            pstmt.setDouble(4, updatePlace.getPlaceLatitude());
            pstmt.setDouble(5, updatePlace.getPlaceLongitude());
            pstmt.setLong(6, updatePlace.getPlaceSeq());
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
        }
        return result;
    }
}
