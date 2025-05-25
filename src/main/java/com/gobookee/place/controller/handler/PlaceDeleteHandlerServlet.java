package com.gobookee.place.controller.handler;

import com.gobookee.common.MessageRedirectTemplate;
import com.gobookee.place.service.PlaceService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "placeDeleteHandlerServlet", urlPatterns = "/place/delete")
public class PlaceDeleteHandlerServlet extends HttpServlet {
    private PlaceService placeService = PlaceService.placeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long placeSeq = Long.valueOf(request.getParameter("placeSeq"));
        boolean result = placeService.deletePlace(placeSeq);
        if (result) {
            MessageRedirectTemplate.builder()
                    .msg("매장 삭제에 성공했습니다.")
                    .loc("/place/listpage")
                    .request(request)
                    .response(response)
                    .build().forward();
        } else {
            MessageRedirectTemplate.builder()
                    .msg("매장 삭제에 실패했습니다.")
                    .loc("/place/listpage")
                    .request(request)
                    .response(response)
                    .build().forward();
        }
    }
}
