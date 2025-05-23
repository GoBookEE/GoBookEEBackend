package com.gobookee.book.controller;

import com.gobookee.book.model.dto.Book;
import com.gobookee.book.service.AladinService;
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
import java.util.ArrayList;
import java.util.List;

import static com.gobookee.book.external.AladinApiClient.aladinApiClient;
import static com.gobookee.book.service.AladinService.aladinService;

@WebServlet("/books/aladin/insertbook")
public class BookInsertFromAladinServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int categoriId =Integer.parseInt(request.getParameter("cId"));
        int start =Integer.parseInt(request.getParameter("start"));
        List<Book> books = new ArrayList<>();
        String json = aladinApiClient().getBookDummyListJson(categoriId, start);
        if (json != null) {
            try {
                JSONObject root = new JSONObject(json);
                JSONArray items = root.getJSONArray("item");
                int result = 0;
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);

                    String pubDateStr = item.optString("pubDate");
                    Date pubDate = null;
                    try {
                        pubDate = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(pubDateStr);
                    } catch (Exception e) {
                        // 무시
                    }

                    Book book = Book.builder()
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
                    result += aladinService().insertDummyBooks(book);
                }
                System.out.println(result+"건을 불러왔습니다.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
