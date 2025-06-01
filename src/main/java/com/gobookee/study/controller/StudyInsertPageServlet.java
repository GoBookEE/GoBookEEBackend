package com.gobookee.study.controller;

import com.gobookee.common.AjaxFileUploadTemplate;
import com.gobookee.common.CommonPathTemplate;
import com.gobookee.common.MessageRedirectTemplate;
import com.gobookee.common.enums.FileType;
import com.gobookee.study.model.dto.StudyInsert;
import com.gobookee.study.service.StudyService;
import com.gobookee.users.model.dto.User;
import com.oreilly.servlet.MultipartRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@WebServlet("/study/upload")
public class StudyInsertPageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StudyService studyService = StudyService.studyService();

    public StudyInsertPageServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        response.setContentType("application/json;charset=UTF-8");
        if (loginUser == null) {
            request.setAttribute("msg", "로그인이 필요한 서비스입니다.");
            request.setAttribute("loc", "/loginpage");
            request.setAttribute("error", "error");
            request.getRequestDispatcher("/WEB-INF/views/common/msg.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher(CommonPathTemplate.getViewPath("/study/studyupload")).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        MultipartRequest mr = AjaxFileUploadTemplate.uploadFiles(request, FileType.STUDY, 1024 * 1024 * 100);
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        response.setContentType("application/json;charset=UTF-8");
        Long userseq = loginUser.getUserSeq();

        Enumeration<String> fileNames = mr.getFileNames();
        List<String> fileList = new ArrayList<>();
        while (fileNames.hasMoreElements()) {
            String name = fileNames.nextElement();
            String savedName = mr.getFilesystemName(name);
            if (savedName != null) {
                fileList.add(savedName);
            }
        }


        String studyTitle = mr.getParameter("studyTitle");

        String studyContent = mr.getParameter("studyContent");
        Long studyMemberLimit = Long.valueOf(mr.getParameter("studyMemberLimit"));
        String studyCategory = mr.getParameter("studyCategory");
        String studyAddress = mr.getParameter("studyAddress");
        if (studyAddress == null || studyAddress.trim().isEmpty()) {
            studyAddress = null;
        }

        Double studyLatitude = null;
        Double studyLongitude = null;
        Date studyDate = null;
        String latStr = mr.getParameter("studyLatitude");
        String lngStr = mr.getParameter("studyLongitude");
        String dateStr = mr.getParameter("studyDate");
        if (latStr != null && !latStr.isEmpty()) {
            studyLatitude = Double.valueOf(latStr);
        }
        if (lngStr != null && !lngStr.isEmpty()) {
            studyLongitude = Double.valueOf(lngStr);
        }
        if (dateStr != null && !dateStr.isEmpty()) {
            studyDate = Date.valueOf(dateStr);
        }

        System.out.println("[검증 로그]");
        System.out.println("studyTitle = " + studyTitle);
        System.out.println("studyContent = " + studyContent);
        System.out.println("studyMemberLimit = " + studyMemberLimit);
        System.out.println("studyCategory = " + studyCategory);
        System.out.println("studyAddress = " + studyAddress);
        System.out.println("studyLatitude = " + studyLatitude);
        System.out.println("studyLongitude = " + studyLongitude);
        System.out.println("studyDate = " + studyDate);
        System.out.println("userSeq = " + userseq);

        StudyInsert studyinsert = StudyInsert.builder()
                .studyTitle(studyTitle)
                .studyContent(studyContent)
                .studyMemberLimit(studyMemberLimit)
                .studyCategory(studyCategory)
                .studyAddress(studyAddress)
                .studyLatitude(studyLatitude)
                .studyLongitude(studyLongitude)
                .studyDate(studyDate)
                .userSeq(userseq)
                .build();

        boolean result = studyService.insertStudy(studyinsert, fileList);
        if (result) {
            MessageRedirectTemplate.builder()
                    .msg("스터디가 등록되었습니다.")
                    .loc("/")
                    .request(request)
                    .response(response)
                    .build().forward();
        } else {
            MessageRedirectTemplate.builder()
                    .msg("스터디 등록을 실패했습니다.")
                    .loc("/")
                    .request(request)
                    .response(response)
                    .error("error")
                    .build().forward();
        }
    }

}
