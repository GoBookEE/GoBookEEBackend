package com.gobookee.study.controller;

import com.gobookee.common.CommonPathTemplate;
import com.gobookee.common.MessageRedirectTemplate;
import com.gobookee.study.service.StudyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/study/view")
public class StudyViewPageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StudyService service = StudyService.studyService();

    public StudyViewPageServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long seq = Long.valueOf(request.getParameter("seq"));
        System.out.println(seq);
        System.out.println(service.getStudyView(seq));
        System.out.println(service.getStudyViewUser(seq));
        request.setAttribute("studyView", service.getStudyView(seq));
        request.setAttribute("studyViewUser", service.getStudyViewUser(seq));
        request.setAttribute("studyNotConfirmedUser", service.getStudyNotConfirmedUser(seq));
        request.getRequestDispatcher(CommonPathTemplate.getViewPath("/study/studyview")).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long studySeq = Long.parseLong(request.getParameter("studySeq"));
        Long userSeq = Long.parseLong(request.getParameter("userSeq"));
        String requestMsg = request.getParameter("requestMsg");

        int result = service.insertStudyRequest(studySeq, userSeq, requestMsg);

        if (result > 0) {
            MessageRedirectTemplate.builder()
                    .msg("스터디 신청에 성공했습니다.")
                    .loc("/study/view?seq=" + studySeq)
                    .request(request)
                    .response(response)
                    .build().forward();
        } else {
            MessageRedirectTemplate.builder()
                    .msg("스터디 신청에 실패했습니다.")
                    .loc("/study/view?seq=" + studySeq)
                    .request(request)
                    .response(response)
                    .error("error")
                    .build().forward();
        }
    }

}
