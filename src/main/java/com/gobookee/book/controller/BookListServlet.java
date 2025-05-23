package com.gobookee.book.controller;
import com.gobookee.book.model.dto.Book;
import com.gobookee.book.service.BookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/books/booklist")
public class BookListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cPage;
        try {
            cPage = Integer.parseInt(request.getParameter("cPage"));
        }catch(NumberFormatException e) {
            cPage = 1;
        }

        int numPerpage = 5;
        List<Book> bookList = BookService.bookService().getAllBookList(cPage,numPerpage);
        for (Book book : bookList) {
            System.out.println(book.getBookPubdate());
        }

        int totalData=BookService.bookService().getAllBookCount();
        int totalPage = (int)Math.ceil((double)totalData/numPerpage);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("bookList", bookList);
        request.setAttribute("cPage", cPage);
        request.setAttribute("numPerpage", numPerpage);
        request.getRequestDispatcher("/book/booklist.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
