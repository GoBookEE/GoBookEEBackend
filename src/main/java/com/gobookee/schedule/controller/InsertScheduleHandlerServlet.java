package com.gobookee.schedule.controller;

import com.gobookee.common.MessageRedirectTemplate;
import com.gobookee.schedule.service.ScheduleService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;

@WebServlet(name = "insertScheduleHandlerServlet", urlPatterns = "/schedule/insert")
public class InsertScheduleHandlerServlet extends HttpServlet {
    private ScheduleService scheduleService = ScheduleService.scheduleService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String stringDate = request.getParameter("date");
        Long placeSeq = Long.valueOf(request.getParameter("placeSeq"));
        Date date = Date.valueOf(stringDate);
        Long studySeq = Long.valueOf(request.getParameter("studySeq"));

        HashMap requestParam = new HashMap();
        requestParam.put("placeSeq", placeSeq);
        requestParam.put("studySeq", studySeq);
        requestParam.put("date", date);

        boolean result = scheduleService.insertSchedule(requestParam);
        if (result) {
            MessageRedirectTemplate.builder()
                    .msg("매장 예약 성공")
                    .loc("/place/view?placeSeq=" + placeSeq)
                    .request(request)
                    .response(response)
                    .build().forward();
        } else {
            MessageRedirectTemplate.builder()
                    .msg("매장 예약 실패")
                    .loc("/place/view?placeSeq=" + placeSeq)
                    .request(request)
                    .response(response)
                    .build().forward();
        }
    }
}
