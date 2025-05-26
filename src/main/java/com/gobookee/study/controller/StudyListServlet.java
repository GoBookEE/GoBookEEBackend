package com.gobookee.study.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.common.AjaxPageBarTemplate;
import com.gobookee.common.CommonPathTemplate;
import com.gobookee.study.model.dto.StudyList;
import com.gobookee.study.service.StudyService;
import com.google.gson.Gson;

@WebServlet("/study/studylist")
public class StudyListServlet extends HttpServlet {
	private StudyService service = StudyService.studyService();
	
	private static final long serialVersionUID = 1L;
       
    public StudyListServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sort = request.getParameter("sort");
		
		int cPage;
		
    	try {
    		cPage=Integer.parseInt(request.getParameter("cPage"));
    	}catch(NumberFormatException e) {
    		cPage=1;
    	}
    	int numPerPage = 5;
    	
		AjaxPageBarTemplate pb = new AjaxPageBarTemplate(cPage, numPerPage, service.allStudyCount());
		
		List<StudyList> list;
		if("recommend".equals(sort)) {
			list=StudyService.studyService().getStudiesByRec(cPage,numPerPage);
		}else {
			list=StudyService.studyService().getStudiesByTime(cPage,numPerPage);
		}
		
		Gson gson = new Gson();
		response.setContentType("application/json;charset=UTF-8");
		Map<String, Object> result = new HashMap<>();
		result.put("studies", list);
		result.put("pageBar", pb.makePageBar(request));
		
		gson.toJson(result, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
