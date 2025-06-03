package com.gobookee.review.model.dto;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReviewListResponse {

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

}
