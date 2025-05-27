package com.gobookee.place.controller.handler;

import com.gobookee.common.CommonPathTemplate;
import com.gobookee.place.model.dto.PlaceViewResponse;
import com.gobookee.place.service.PlaceService;
import com.gobookee.users.model.dto.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "placeViewHandlerServlet", urlPatterns = "/place/view")
public class PlaceViewHandlerServlet extends HttpServlet {
    private PlaceService placeService = PlaceService.placeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginMember = (User) session.getAttribute("loginUser");
        Long placeSeq = Long.valueOf(request.getParameter("placeSeq"));
        PlaceViewResponse place = placeService.getPlaceBySeq(placeSeq);
        boolean isOwner = loginMember.getUserSeq().equals(place.getUserSeq());
        request.setAttribute("place", place);
        request.setAttribute("isOwner", isOwner);
        request.getRequestDispatcher(CommonPathTemplate.getViewPath("/place/placeView")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
