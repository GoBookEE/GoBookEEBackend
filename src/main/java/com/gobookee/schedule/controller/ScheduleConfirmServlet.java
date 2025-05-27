package com.gobookee.schedule.controller;

import com.gobookee.common.JsonConvertTemplate;
import com.gobookee.schedule.model.dto.ScheduleConfirm;
import com.gobookee.schedule.service.ScheduleService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet(name = "scheduleConfirmServlet", urlPatterns = "/schedule/confirm")
public class ScheduleConfirmServlet extends HttpServlet {
    private ScheduleService scheduleService = ScheduleService.scheduleService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Stream<String> lines = request.getReader().lines();
        String data = lines.collect(Collectors.joining());
        ScheduleConfirm scheduleConfirm = JsonConvertTemplate.fromJson(data, ScheduleConfirm.class);
        boolean result = scheduleService.confirmSchedule(scheduleConfirm);
        JsonConvertTemplate.toJson(result, response);
    }
}
