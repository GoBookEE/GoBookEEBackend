package com.gobookee.search.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class SearchReview {
    private Long reviewSeq;
    private String reviewTitle;
    private String reviewContents;
    private Timestamp reviewCreateTime;
    private Integer reviewRate;
    private Timestamp reviewEditTime;
    private String bookTitle;
    private String bookCover;
    private Integer recommendCount;
    private Integer commentsCount;
    private String userNickname;

    public static SearchReview from(ResultSet rs) throws SQLException {
        return SearchReview.builder()
                .reviewSeq(rs.getLong("REVIEW_SEQ"))
                .reviewTitle(rs.getString("REVIEW_TITLE"))
                .reviewContents(rs.getString("REVIEW_CONTENTS"))
                .reviewCreateTime(rs.getTimestamp("REVIEW_CREATE_TIME"))
                .reviewRate(rs.getInt("REVIEW_RATE"))
                .reviewEditTime(rs.getTimestamp("REVIEW_EDIT_TIME"))
                .bookTitle(rs.getString("BOOK_TITLE"))
                .bookCover(rs.getString("BOOK_COVER"))
                .recommendCount(rs.getInt("RECOMMEND_COUNT"))
                .commentsCount(rs.getInt("COMMENTS_COUNT"))
                .userNickname(rs.getString("USER_NICKNAME"))
                .build();
    }
}
