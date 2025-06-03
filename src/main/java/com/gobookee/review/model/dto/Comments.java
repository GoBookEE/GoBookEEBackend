package com.gobookee.review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Comments {

	private Long commentsSeq;
	private String commentsContents;
	private Timestamp commentsCreateTime;
	private Timestamp commentsDeleteTime;
	private Timestamp commentsEditTime;
	private Long commentsParentSeq;
	private char commentsIsPublic;
	private Long userSeq;
	private Long reviewSeq;

}
