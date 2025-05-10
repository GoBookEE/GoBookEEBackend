package com.gobookee.review.model.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReviewDTO {
    private Long reviewSeq;
    private String reviewTitle;
    private String reviewContents;
    private Timestamp reviewCreateTime;
    private Integer reviewRate;
    private Timestamp reviewDeleteTime;
    private Timestamp reviewEditTime;
    private Long userSeq;
    private Long bookSeq;
}
