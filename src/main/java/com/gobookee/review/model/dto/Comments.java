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
