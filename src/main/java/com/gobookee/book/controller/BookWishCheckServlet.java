package com.gobookee.book.controller;

import com.gobookee.book.service.BookService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/books/wishcheck")
public class BookWishCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long userSeq = Long.parseLong(request.getParameter("userSeq"));
        long bookSeq = Long.parseLong(request.getParameter("bookSeq"));
        String mode = request.getParameter("mode");
        int result = 0;
        Gson gson = new Gson();
        response.setContentType("application/json;charset=UTF-8");

        if (mode != null) {
            if(mode.equals("check")){
                result = BookService.bookService().wishCheck(userSeq,bookSeq);
            }else if(mode.equals("uncheck")){
                result = BookService.bookService().wishUncheck(userSeq,bookSeq);
            }else{
                result = 0;
            }
        }
        gson.toJson(result, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
