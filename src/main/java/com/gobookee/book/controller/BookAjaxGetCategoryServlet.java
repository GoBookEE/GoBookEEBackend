package com.gobookee.book.controller;

import com.gobookee.book.model.dto.BookCategory;
import com.gobookee.book.service.BookCategoryService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/books/category/ajaxgetcategory")
public class BookAjaxGetCategoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cid = Integer.parseInt(request.getParameter("cid"));
        int level = Integer.parseInt(request.getParameter("level"));
        List<BookCategory> bookCategory = BookCategoryService.bookCategoryService().getBookCategory(cid,level);
        response.setContentType("application/json;charset=UTF-8");
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(bookCategory));

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
