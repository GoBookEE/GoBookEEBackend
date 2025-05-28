package com.gobookee.book.controller;

import com.gobookee.book.model.dto.BookCategory;
import com.gobookee.book.service.BookCategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/books/searchaladin")
public class BookSearchAladin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<BookCategory> bookCategory = BookCategoryService.bookCategoryService().getBookCategory(0,1);
        request.setAttribute("bookCategory", bookCategory);
        request.getRequestDispatcher("/WEB-INF/views/book/bookAladinSearch.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
