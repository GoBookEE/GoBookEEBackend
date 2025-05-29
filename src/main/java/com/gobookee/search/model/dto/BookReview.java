package com.gobookee.search.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookReview {
    private Long bookSeq;
    private String bookTitle;
    private String bookAuthor;
    private String bookCover;
    private Date bookPubDate;
    private String bookDescription;
    private String bookPublisher;
    private Double reviewRateAvg;
    private Integer reviewCount;

    public static BookReview from(ResultSet rs) throws SQLException {
        return BookReview.builder()
                .bookSeq(rs.getLong("BOOK_SEQ"))
                .bookTitle(rs.getString("BOOK_TITLE"))
                .bookAuthor(rs.getString("BOOK_AUTHOR"))
                .bookCover(rs.getString("BOOK_COVER"))
                .bookPubDate(rs.getDate("BOOK_PUBDATE"))
                .bookDescription(rs.getString("BOOK_DESCRIPTION"))
                .reviewRateAvg(rs.getDouble("REVIEW_RATE_AVG"))
                .reviewCount(rs.getInt("REVIEW_COUNT"))
                .bookPublisher(rs.getString("BOOK_PUBLISHER"))
                .build();
    }
}
