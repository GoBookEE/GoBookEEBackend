package com.gobookee.study.model.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class StudyView {
	private Long userSeq;
	private String studyTitle;
	private Date studyDate;
	private String studyContent;
	private Long studyMemberLimit;
	private String studyAddress;
	private Double studyLatitude;
	private Double studyLongitude;
	private String userNickName;
	private Long userSpeed;
	private String userProfile;
	private String studyCategory;
	private String photoName;
	private Long confirmedCount;
	private Long likeCount;
	private Long dislikeCount;

}
