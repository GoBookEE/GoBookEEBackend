package com.gobookee.book.controller;

import com.gobookee.book.model.dto.Book;
import com.gobookee.book.service.BookService;
import com.gobookee.common.CommonPathTemplate;
import com.gobookee.common.PageBarTemplate;
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

@WebServlet("/books/searchaladinrequest")
public class BookSearchAladinRequest extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String queryType=request.getParameter("QueryType");
        String query=request.getParameter("Query");
        String d5 = request.getParameter("dept5");
        String d4 = request.getParameter("dept4");
        String d3 = request.getParameter("dept3");
        String d2 = request.getParameter("dept2");
        String d1 = request.getParameter("dept1");
        String s = request.getParameter("start");
        int start = 1;
        if (s!=null && !s.isEmpty()){
            start = Integer.parseInt(s);
        }

        int categoryId = 0;
        if (d5 != null && !d5.isEmpty()) {
            categoryId = Integer.parseInt(d5);
        } else if (d4 != null && !d4.isEmpty()) {
            categoryId = Integer.parseInt(d4);
        } else if (d3 != null && !d3.isEmpty()) {
            categoryId = Integer.parseInt(d3);
        } else if (d2 != null && !d2.isEmpty()) {
            categoryId = Integer.parseInt(d2);
        } else if (d1 != null && !d1.isEmpty()) {
            categoryId = Integer.parseInt(d1);
        }

        String json = aladinApiClient().getBookSearchJson(queryType, query, categoryId, start);
        if (json != null) {
            try {
                List<Book> books = new ArrayList<>();
                JSONObject root = new JSONObject(json);
                JSONArray items = root.getJSONArray("item");
                int numPerpage = 5;

                int totalData= root.optInt("totalResults");
                int totalPage = (int)Math.ceil((double)totalData/numPerpage);
                StringBuffer pageBar = PageBarTemplate.builder()
                        .cPage(start)
                        .numPerPage(numPerpage)
                        .totalData(totalData)
                        .build()
                        .makePageBar(request);
                String q = "QueryType=" + queryType + "&Query=" + query + "&dept1=" + categoryId + "&start=";
                String modifiedPageBar = pageBar.toString().replace("cPage=", q);
                pageBar = new StringBuffer(modifiedPageBar);
                request.setAttribute("pageBar", pageBar);
                request.setAttribute("query", query);

                int result = 0;
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);

                    String pubDateStr = item.optString("pubDate");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date pubDate = null;

                    try {
                        java.util.Date utilDate = sdf.parse(pubDateStr); // java.util.Date
                        pubDate = new java.sql.Date(utilDate.getTime()); // java.sql.Date로 변환

                    } catch (Exception e) {
                        // 무시
                        e.printStackTrace();
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
                    books.add(book);
                }
                request.setAttribute("bookList", books);
                request.getRequestDispatcher(CommonPathTemplate.getViewPath("/book/bookAladinSearchList")).forward(request, response);
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
