package com.gobookee.place.controller.page;

import com.gobookee.common.CommonPathTemplate;
import com.gobookee.place.model.dto.PlaceViewResponse;
import com.gobookee.place.service.PlaceService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "placeUpdatePageServlet", urlPatterns = "/place/updatepage")
public class PlaceUpdatePageServlet extends HttpServlet {
    private PlaceService placeService = PlaceService.placeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PlaceViewResponse place = placeService.getPlaceBySeq(Long.valueOf(request.getParameter("placeSeq")));
        request.setAttribute("place", place);
        request.getRequestDispatcher(CommonPathTemplate.getViewPath("/place/placeUpdate")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
