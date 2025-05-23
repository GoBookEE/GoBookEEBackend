package com.gobookee.review.model.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Review {
    private Long reviewSeq;
    private String reviewTitle;
    private String reviewContents;
    private Timestamp reviewCreateTime;
    private Integer reviewRate;
    private Timestamp reviewDeleteTime;
    private Timestamp reviewEditTime;
    private char reviewIsPublic;
    private Long userSeq;
    private Long bookSeq;
    private List<Comments> comments;
    
    private String bookTitle;
    private String bookAuthor;
    private String bookPublisher;
    private String bookCover;
    
    private Integer recommendCount;
    private Integer bookReviewCount;
    private Double bookAvgRate;
}
