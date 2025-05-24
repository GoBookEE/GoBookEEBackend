package com.gobookee.place.controller.handler;

import com.gobookee.common.AjaxFileUploadTemplate;
import com.gobookee.common.JsonConvertTemplate;
import com.gobookee.common.enums.FileType;
import com.gobookee.place.model.dto.Place;
import com.gobookee.place.service.PlaceService;
import com.oreilly.servlet.MultipartRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@WebServlet(name = "placeInsertAjaxHandlerServlet", urlPatterns = "/place/insert")
public class PlaceInsertAjaxHandlerServlet extends HttpServlet {
    private PlaceService placeService = PlaceService.placeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //파일 업로드
        MultipartRequest mr = AjaxFileUploadTemplate.uploadFiles(request, FileType.PLACE, 1024 * 1024 * 100);

        //업로드된 파일들 모두 가져와서 rename된 파일명 List에 담기
        Enumeration<String> fileNames = mr.getFileNames();
        List<String> fileList = new ArrayList<>();
        while (fileNames.hasMoreElements()) {
            fileList.add(mr.getFilesystemName(fileNames.nextElement()));
        }

        //파일 외에 넘어온 파라미터 처리하기
        String placeTitle = mr.getParameter("placeTitle");
        String placeContent = mr.getParameter("placeContent");
        String placeAddress = mr.getParameter("placeAddress");
        Double placeLatitude = Double.valueOf(mr.getParameter("placeLatitude"));
        Double placeLongitude = Double.valueOf(mr.getParameter("placeLongitude"));
        Long userSeq = Long.valueOf(mr.getParameter("placeUserSeq"));

        Place place = Place.builder()
                .placeTitle(placeTitle)
                .placeContents(placeContent)
                .placeAddress(placeAddress)
                .placeLatitude(placeLatitude)
                .placeLongitude(placeLongitude)
                .userSeq(userSeq)
                .build();

        boolean result = placeService.createPlace(place, fileList);
        JsonConvertTemplate.toJson(result,response);
    }
}
