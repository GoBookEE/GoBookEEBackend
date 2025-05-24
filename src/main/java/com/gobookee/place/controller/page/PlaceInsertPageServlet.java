package com.gobookee.place.controller.page;

import com.gobookee.common.CommonPathTemplate;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "placeInsertPageServlet", urlPatterns = "/place/insertpage")
public class PlaceInsertPageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(CommonPathTemplate.getViewPath("/place/placeEnroll")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
