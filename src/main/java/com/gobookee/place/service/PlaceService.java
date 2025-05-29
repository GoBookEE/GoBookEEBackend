package com.gobookee.place.service;

import com.gobookee.photo.model.dao.PhotoDao;
import com.gobookee.photo.model.dto.Photo;
import com.gobookee.place.model.dao.PlaceDao;
import com.gobookee.place.model.dto.Place;
import com.gobookee.place.model.dto.PlaceListResponse;
import com.gobookee.place.model.dto.PlaceViewResponse;
import com.gobookee.users.model.dao.UserDAO;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import static com.gobookee.common.JDBCTemplate.*;

public class PlaceService {
    private static PlaceService placeService;
    private static PhotoDao photoDao = PhotoDao.photoDao();
    private PlaceDao placeDao = PlaceDao.placeDao();
    private UserDAO userDAO = UserDAO.userDao();
    private Connection conn;

    private PlaceService() {
    }

    public static PlaceService placeService() {
        if (placeService == null) {
            placeService = new PlaceService();
        }
        return placeService;
    }

    public boolean createPlace(Place place, List<String> fileList) {
        Connection conn = getConnection();
        boolean isSuccess = false;

        //매장 등록 & 매장 시퀀스 반환
        try {
            long boardSeq = placeDao.insertPlaceAndReturnId(conn, place);
            if (boardSeq == -1) {
                rollback(conn);
                return false;
            }

            //사진 등록
            int insertedPhotos = 0;
            for (String fileName : fileList) {
                Photo photo = Photo.builder()
                        .photoRenamedName(fileName)
                        .photoBoardSeq(boardSeq)
                        .build();
                insertedPhotos += photoDao.createPhoto(conn, photo);
            }

            //사진 등록 다 성공 시 트랜잭션 커밋처리 하나라도 실패 시 롤백처리
            if (insertedPhotos != fileList.size()) {
                rollback(conn);
                return false;
            }

            userDAO.updateUserSpeed(conn, place.getUserSeq(), 3);

            isSuccess = true;
            commit(conn);
        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
        } finally {
            close(conn);
        }
        return isSuccess;
    }

    public int placeCount() {
        conn = getConnection();
        int totalData = placeDao.placeCount(conn);
        close(conn);
        return totalData;
    }

    public List<PlaceListResponse> getAllPlaceList(HashMap requestParam) {
        conn = getConnection();
        List<PlaceListResponse> placeList = placeDao.getAllPlaceList(conn, requestParam);
        for (PlaceListResponse placeListResponse : placeList) {
            String photoName = photoDao.getFirstPhotoNameByBoardSeq(conn, placeListResponse.getPlaceSeq());
            placeListResponse.setPlaceThumbnail(photoName);
        }
        close(conn);
        return placeList;
    }

    public List<PlaceListResponse> getAllPlaceListByRec(HashMap requestParam) {
        conn = getConnection();
        List<PlaceListResponse> placeList = placeDao.getAllPlaceListByRec(conn, requestParam);
        for (PlaceListResponse placeListResponse : placeList) {
            String photoName = photoDao.getFirstPhotoNameByBoardSeq(conn, placeListResponse.getPlaceSeq());
            placeListResponse.setPlaceThumbnail(photoName);
        }
        close(conn);
        return placeList;
    }

    public PlaceViewResponse getPlaceBySeq(Long placeSeq) {
        conn = getConnection();
        PlaceViewResponse place = placeDao.getPlaceBySeq(conn, placeSeq);
        List<String> photoList = photoDao.getPhotoListByBoardSeq(conn, placeSeq);
        place.setPhotoNames(photoList);
        close(conn);
        return place;
    }

    public boolean deletePlace(Long placeSeq) {
        conn = getConnection();
        boolean isSuccess = false;
        try {
            int result = placeDao.deletePlace(conn, placeSeq);
            if (result == 0) {
                rollback(conn);
                return false;
            }

            if (!photoDao.deletePhotoByPlaceSeq(conn, placeSeq)) {
                rollback(conn);
                return false;
            }

            isSuccess = true;
            commit(conn);
        } catch (Exception e) {
            e.printStackTrace();
            rollback(conn);
        } finally {
            close(conn);
        }
        return isSuccess;
    }

    public boolean updatePlace(Place updatePlace, List<String> fileList) {
        Connection conn = getConnection();
        boolean isSuccess = false;
        try {
            //매장 정보 수정
            int updateData = placeDao.updatePlace(conn, updatePlace);
            if (updateData == 0) {
                rollback(conn);
                return false;
            }

            //기존 사진 삭제
            if (!photoDao.deletePhotoByPlaceSeq(conn, updatePlace.getPlaceSeq())) {
                rollback(conn);
                return false;
            }

            //새 사진 등록
            if (!insertPhotos(conn, updatePlace.getPlaceSeq(), fileList)) {
                rollback(conn);
                return false;
            }
            //모든 작업 성공 시 커밋
            commit(conn);
            isSuccess = true;
        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
        } finally {
            close(conn);
        }
        return isSuccess;
    }

    private boolean insertPhotos(Connection conn, Long placeSeq, List<String> fileList) {
        int insertedPhotos = 0;
        for (String fileName : fileList) {
            Photo photo = Photo.builder()
                    .photoRenamedName(fileName)
                    .photoBoardSeq(placeSeq)
                    .build();
            insertedPhotos += photoDao.createPhoto(conn, photo);
        }
        return insertedPhotos == fileList.size();
    }
}