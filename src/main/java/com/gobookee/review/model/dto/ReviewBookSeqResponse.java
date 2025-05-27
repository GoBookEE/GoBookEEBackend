package com.gobookee.review.model.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReviewBookSeqResponse {

	private Long reviewSeq;
	private String reviewTitle;
	private String reviewContents;
	private Timestamp reviewCreateTime;
	private Integer reviewRate;

	private Integer recommendCount;
	private Integer nonRecommendCount;
	private Integer recommendType;

	private String userProfile;
	private String userNickname;

}
