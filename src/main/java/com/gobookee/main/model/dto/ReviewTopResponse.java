package com.gobookee.main.model.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReviewTopResponse {

    private Long reviewSeq;
    private String reviewTitle;
    private String reviewContents;
    private Timestamp reviewCreateTime;
    private Integer reviewRate;
    private Timestamp reviewEditTime;

    private String bookTitle;
    private String bookCover;
    private String bookAuthor;
    private String bookPublisher;
    private Date bookPubdate;


    private Integer recommendCount;
    private Integer commentsCount;

}
