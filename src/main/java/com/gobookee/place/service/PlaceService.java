package com.gobookee.place.service;

import com.gobookee.photo.model.dao.PhotoDao;
import com.gobookee.photo.model.dto.Photo;
import com.gobookee.place.model.dao.PlaceDao;
import com.gobookee.place.model.dto.Place;

import java.sql.Connection;
import java.util.List;

import static com.gobookee.common.JDBCTemplate.*;

public class PlaceService {
    private static PlaceService placeService;
    private static PhotoDao photoDao = PhotoDao.photoDao();
    private Connection conn;
    private PlaceDao placeDao = PlaceDao.placeDao();

    private PlaceService() {
    }

    public static PlaceService placeService() {
        if (placeService == null) {
            placeService = new PlaceService();
        }
        return placeService;
    }

    public boolean createPlace(Place place, List<String> fileList) {
        //하나의 트랜잭션으로 사진 등록 및 매장 등록 처리
        conn = getConnection();
        long boardSeq = placeDao.insertPlaceAndReturnId(conn, place);
        if (boardSeq != -1) {
            int photoResult = 0;
            for (String fileName : fileList) {
                photoResult += photoDao.createPhoto(
                        conn, Photo.builder()
                                .photoRenamedName(fileName)
                                .photoBoardSeq(boardSeq)
                                .build()
                );
            }
            if (fileList.size() == photoResult) {
                commit(conn);
                close(conn);
                return true;
            } else {
                rollback(conn);
                close(conn);
            }
        }
        return false;
    }
}
