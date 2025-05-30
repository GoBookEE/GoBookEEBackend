package com.gobookee.schedule.controller;

import com.gobookee.common.JsonConvertTemplate;
import com.gobookee.schedule.model.dto.ScheduleReserve;
import com.gobookee.schedule.service.ScheduleService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "searchReservationAjaxHandlerServlet", urlPatterns = "/schedule/searchreservation")
public class SearchReservationAjaxHandlerServlet extends HttpServlet {
    private ScheduleService scheduleService = ScheduleService.scheduleService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String stringDate = request.getParameter("date");
        Long placeSeq = Long.valueOf(request.getParameter("placeSeq"));
        Date date = Date.valueOf(stringDate);
        List<ScheduleReserve> scheduleList = scheduleService.getStudyListPlaceSeqAndDate(placeSeq, date);
        JsonConvertTemplate.toJson(scheduleList, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
