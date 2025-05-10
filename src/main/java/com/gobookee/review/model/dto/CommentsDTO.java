package com.gobookee.review.model.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentsDTO {
	private Long commentsSeq;
    private String commentsContents;
    private Timestamp commentsCreateTime;
    private Timestamp commentsDeleteTime;
    private Timestamp commentsEditTime;
    private Long userSeq;
    private Long reviewSeq;
}
