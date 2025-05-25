package com.gobookee.schedule.controller;

import com.gobookee.common.JsonConvertTemplate;
import com.gobookee.schedule.service.ScheduleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "searchStudyCountAjaxHandlerServlet", urlPatterns = "/schedule/searchstudycount")
public class SearchStudyCountAjaxHandlerServlet extends HttpServlet {
    private ScheduleService scheduleService = ScheduleService.scheduleService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String stringDate = request.getParameter("date");
        Long placeSeq = Long.valueOf(request.getParameter("placeSeq"));
        Date date = Date.valueOf(stringDate);
        int count = scheduleService.getStudyCountByPlaceSeqAndDate(placeSeq, date);
        JsonConvertTemplate.toJson(count, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
