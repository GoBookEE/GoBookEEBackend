package com.gobookee.book.service;
import com.gobookee.book.external.AladinApiClient;
import com.gobookee.book.model.dto.Book;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class AladinService {

    private final AladinApiClient apiClient;

    public AladinService() {
        this.apiClient = new AladinApiClient();
    }

    public List<Book> fetchBooks(String queryType, int maxResults) {
        List<Book> books = new ArrayList<>();
        String json = apiClient.getBookDummyListJson(queryType, maxResults);

        if (json != null) {
            try {
                JSONObject root = new JSONObject(json);
                JSONArray items = root.getJSONArray("item");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);

                    // 문자열로 된 날짜 처리 예시
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

                    books.add(book);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return books;
    }
}
