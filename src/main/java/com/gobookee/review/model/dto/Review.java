package com.gobookee.review.model.dto;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
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

}
