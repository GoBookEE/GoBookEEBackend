package com.gobookee.mypage.controller;

import com.gobookee.book.model.dto.Book;
import com.gobookee.book.service.BookService;
import com.gobookee.common.PageBarTemplate;
import com.gobookee.users.model.dto.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/mypage/mywish")
public class MyWishListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loginUser = (User) request.getSession().getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        int numPerPage = 5;
        int cPage;
        try {
            cPage = Integer.parseInt(request.getParameter("cPage"));
        }catch(NumberFormatException e) {
            cPage = 1;
        }
        List<Book> wishBookList = new ArrayList<>();
        wishBookList = BookService.bookService().getWishListByUserSeq(loginUser.getUserSeq(),cPage,numPerPage);
        int totalCount = BookService.bookService().getWishCountByUserSeq(loginUser.getUserSeq());
        int totalPage = (int)Math.ceil((double)totalCount/numPerPage);
        StringBuffer pageBar = new StringBuffer();
        pageBar = PageBarTemplate.builder().cPage(cPage).numPerPage(numPerPage).totalData(totalCount).build().makePageBar(request);
        request.setAttribute("pageBar", pageBar);
        request.setAttribute("wishBookList", wishBookList);
        request.getRequestDispatcher("/WEB-INF/views/mypage/myWish.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
