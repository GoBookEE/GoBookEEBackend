package com.gobookee.book.controller;

import com.gobookee.book.model.dto.Book;
import com.gobookee.book.service.BookService;
import com.gobookee.common.CommonPathTemplate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/books/bookdetail")
public class BookDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookSeq = Integer.parseInt(request.getParameter("bookSeq"));
        int userSeq = 0;
        Book book = BookService.bookService().getBookDetailBySeq(bookSeq,userSeq);
        request.setAttribute("book", book);
        request.getRequestDispatcher(CommonPathTemplate.getViewPath("/book/bookDetail")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
