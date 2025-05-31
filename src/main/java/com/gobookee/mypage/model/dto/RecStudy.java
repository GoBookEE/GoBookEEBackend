package com.gobookee.mypage.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecStudy {

	private Long studySeq;
	private String studyTitle;
	private Date studyDate;
	private Integer studyMemberLimit;
	private String studyAddress;
	private Integer confirmCount;
	private String photoRenamedName;
	private Integer likeCount;
	private Integer dislikeCount;
	private String nickname;

}
