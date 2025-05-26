package com.gobookee.studygroup.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gobookee.common.AjaxPageBarTemplate;
import com.gobookee.study.model.dto.StudyList;
import com.gobookee.studygroup.service.StudyGroupService;
import com.gobookee.users.model.dto.User;
import com.google.gson.Gson;

@WebServlet("/studygroup/studygrouplist")
public class StudyGroupListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudyGroupService service = StudyGroupService.studyGroupService();
    public StudyGroupListServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User loginUser=(User)session.getAttribute("loginUser");
		response.setContentType("application/json;charset=UTF-8");
	    if (loginUser == null) {
	        
	        response.getWriter().write("{\"error\":\"로그인이 필요한 서비스입니다.\"}");
	        return;
	    }
		Long userseq = loginUser.getUserSeq();
		String sort = request.getParameter("sort");
		int cPage;
		
    	try {
    		cPage=Integer.parseInt(request.getParameter("cPage"));
    	}catch(NumberFormatException e) {
    		cPage=1;
    	}
    	int numPerPage = 5;
    	
		AjaxPageBarTemplate pb = new AjaxPageBarTemplate(cPage, numPerPage, service.allStudyGroupsCount(userseq));
		
		List<StudyList> list;
		if("recommend".equals(sort)) {
			list=StudyGroupService.studyGroupService().getGroupStudiesByRec(userseq, cPage, numPerPage);
		}else {
			list=StudyGroupService.studyGroupService().getGroupStudiesByTime(userseq, cPage, numPerPage);
		}
		
		Gson gson = new Gson();
		Map<String, Object> result = new HashMap<>();
		result.put("studygroups", list);
		result.put("pageBar", pb.makePageBar(request));
		
		gson.toJson(result, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
