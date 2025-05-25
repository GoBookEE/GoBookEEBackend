package com.gobookee.place.controller.handler;

import com.gobookee.common.AjaxFileUploadTemplate;
import com.gobookee.common.MessageRedirectTemplate;
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

@WebServlet(name = "placeUpdateHandlerServlet", urlPatterns = "/place/update")
public class PlaceUpdateHandlerServlet extends HttpServlet {
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
        Long placeSeq = Long.valueOf(mr.getParameter("placeSeq"));

        Place updatePlace = Place.builder()
                .placeTitle(placeTitle)
                .placeContents(placeContent)
                .placeAddress(placeAddress)
                .placeLatitude(placeLatitude)
                .placeLongitude(placeLongitude)
                .placeSeq(placeSeq)
                .build();

        boolean result = placeService.updatePlace(updatePlace, fileList);
        if (result) {
            MessageRedirectTemplate.builder()
                    .msg("매장 정보 수정에 성공했습니다.")
                    .loc("/place/view?placeSeq=" + placeSeq)
                    .response(response)
                    .request(request)
                    .build().forward();
        } else {
            MessageRedirectTemplate.builder()
                    .msg("매장 정보 수정에 실패했습니다.")
                    .loc("/place/view?placeSeq=" + placeSeq)
                    .response(response)
                    .request(request)
                    .build().forward();
        }
    }
}
