package com.gobookee.book.controller;

import com.gobookee.book.service.AladinService;
import com.gobookee.book.model.dto.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/books")
public class AladinServlet extends HttpServlet {

    private AladinService bookService;

    @Override
    public void init() throws ServletException {
        bookService = new AladinService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 예: ?queryType=Bestseller&maxResults=5
        String queryType = request.getParameter("queryType");
        String maxResultsParam = request.getParameter("maxResults");

        int maxResults = 10;
        if (maxResultsParam != null) {
            try {
                maxResults = Integer.parseInt(maxResultsParam);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        List<Book> books = bookService.fetchBooks(queryType != null ? queryType : "Bestseller", maxResults);

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        JSONArray jsonArray = new JSONArray();

        for (Book book : books) {
            JSONObject obj = new JSONObject();
            obj.put("title", book.getBookTitle());
            obj.put("author", book.getBookAuthor());
            obj.put("cover", book.getBookCover());
            obj.put("pubDate", book.getBookPubdate()); // Date -> String 필요 시 SimpleDateFormat 사용
            jsonArray.put(obj);
            System.out.println(book); // 전체 필드 출력 (toString() 적용된 경우)
        }

        out.print(jsonArray.toString());
        out.flush();
    }
}
