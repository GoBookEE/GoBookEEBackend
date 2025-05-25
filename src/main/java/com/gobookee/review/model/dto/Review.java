package com.gobookee.review.model.dto;

import java.sql.Timestamp;
import java.util.List;

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
