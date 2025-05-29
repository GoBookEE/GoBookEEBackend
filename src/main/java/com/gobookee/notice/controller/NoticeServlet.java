package com.gobookee.notice.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gobookee.common.PageBarTemplate;
import com.gobookee.notice.model.dto.Notice;
import com.gobookee.notice.model.service.NoticeService;

/**
 * Servlet implementation class NoticeServlet
 */
@WebServlet("/notice/listpage")
public class NoticeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//필요한 것 
		//cpage = 현재페이지 => 없으면 1 
		int cPage;
		try {
			cPage = Integer.parseInt(request.getParameter("cPage"));
		}catch(NumberFormatException e) {
			cPage = 1;
		}
		int numPerpage = 5;
		//numperpage = 가져오는갯수
		int totalData = NoticeService.noticeService().noticeCount();
		//rownum쿼리로 가져오기 
		//총개수 쿼리로 구해오기 
		//템플릿사용 
	    StringBuffer pageBar = PageBarTemplate.builder().cPage(cPage).numPerPage(numPerpage).totalData(totalData).build().makePageBar(request);
		
	    List<Notice> notices = NoticeService.noticeService().searchNoticeAll(cPage, numPerpage);
	    request.setAttribute("pageBar", pageBar);
	    request.setAttribute("notices", notices);
	    
		request.getRequestDispatcher("/WEB-INF/views/notice/notice.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
