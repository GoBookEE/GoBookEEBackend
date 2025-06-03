package com.gobookee.book.controller;

import com.gobookee.book.model.dto.Book;
import com.gobookee.book.service.BookService;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static com.gobookee.book.external.AladinApiClient.aladinApiClient;
import static com.gobookee.book.service.AladinService.aladinService;

@WebServlet("/books/aladin/insertbookbyid")
public class BookInsertFromAladinByIdServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> res = new HashMap<>();

        String bookId = request.getParameter("bookId");
        int bookSeq = BookService.bookService().getBookDetailByBookID(Integer.parseInt(bookId));
        if (bookSeq != 0) {
            res.put("success", true);
            res.put("bookSeq", bookSeq);
            gson.toJson(res, response.getWriter());
            return;
        }
        Book book = new Book();
        String json = aladinApiClient().getBookByIdJson(bookId);

        if (json != null) {
            try {
                JSONObject root = new JSONObject(json);
                JSONArray items = root.getJSONArray("item");
                long result = 0;
                JSONObject item = items.getJSONObject(0);

                String pubDateStr = item.optString("pubDate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date pubDate = null;

                try {
                    java.util.Date utilDate = sdf.parse(pubDateStr); // java.util.Date
                    pubDate = new Date(utilDate.getTime()); // java.sql.Date로 변환

                } catch (Exception e) {
                    // 무시
                    e.printStackTrace();
                }

                book = Book.builder()
                        .bookID(item.optLong("itemId"))
                        .bookTitle(item.optString("title"))
                        .bookLink(item.optString("link"))
                        .bookAuthor(item.optString("author"))
                        .bookPubdate(pubDate)
                        .bookDescription(item.optString("description"))
                        .bookIsbn(item.optString("isbn"))
                        .bookIsbn13(item.optString("isbn13"))
                        .bookPriceSales(item.optInt("priceSales"))
                        .bookPriceStandard(item.optInt("priceStandard"))
                        .bookMallType(item.optString("mallType"))
                        .bookStockStatus(item.optString("stockStatus"))
                        .bookMileage(item.optInt("mileage"))
                        .bookCover(item.optString("cover"))
                        .bookCategoryId(item.optString("categoryId"))
                        .bookCategoryName(item.optString("categoryName"))
                        .bookPublisher(item.optString("publisher"))
                        .bookSalesPoint(item.optInt("salesPoint"))
                        .bookAdult(item.optString("adult"))
                        .bookFixedPrice(item.optString("fixedPrice"))
                        .bookCustomerReviewRank(item.optInt("customerReviewRank"))
                        .bookSeriesId(item.optString("seriesId"))
                        .bookSeriesLink(item.optString("seriesLink"))
                        .bookSeriesName(item.optString("seriesName"))
                        .bookSubInfo(item.optString("subInfo"))
                        .build();
                result = aladinService().insertDummyBooks(book);
                if (result > 0) {
                    bookSeq = BookService.bookService().getBookDetailByBookID(Integer.parseInt(bookId));
                    res.put("success", true);
                    res.put("bookSeq", bookSeq);
                } else {
                    res.put("success", false);
                    res.put("message", "등록 실패");
                }
            } catch (Exception e) {
                res.put("success", false);
                res.put("message", "처리 중 오류 발생");
                e.printStackTrace();
            }
        } else {
            res.put("success", false);
            res.put("message", "처리 중 오류 발생");
        }
        gson.toJson(res, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
