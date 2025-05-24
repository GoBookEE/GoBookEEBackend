package com.gobookee.place.controller.handler;

import com.gobookee.common.AjaxPageBarTemplate;
import com.gobookee.common.JsonConvertTemplate;
import com.gobookee.place.model.dto.PlaceListResponse;
import com.gobookee.place.service.PlaceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet("/place/list")
public class PlaceListAjaxHandlerServlet extends HttpServlet {
    private PlaceService placeService = PlaceService.placeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sort = request.getParameter("sort");
        int cPage;
        try {
            cPage = Integer.parseInt(request.getParameter("cPage"));
        } catch (NumberFormatException e) {
            cPage = 1;
        }
        int numPerPage = 5;
        StringBuffer pageBar = AjaxPageBarTemplate.builder()
                .cPage(cPage)
                .numPerPage(numPerPage)
                .totalData(placeService.placeCount())
                .build().makePageBar(request);

        List<PlaceListResponse> placeList = new ArrayList<>();
        HashMap requestParam = new HashMap();
        requestParam.put("cPage", cPage);
        requestParam.put("numPerPage", numPerPage);

        if (sort.equals("recommend")) {
            //추천순 정렬
            placeList = placeService.getAllPlaceListByRec(requestParam);
        } else {
            //등록순 정렬
            placeList = placeService.getAllPlaceList(requestParam);
        }

        HashMap result = new HashMap();
        result.put("placeList", placeList);
        result.put("pageBar", pageBar);

        JsonConvertTemplate.toJson(result, response);
    }
}
