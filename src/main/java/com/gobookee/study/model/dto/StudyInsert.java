package com.gobookee.study.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class StudyInsert {
	
	private Long studySeq;
	private String studyTitle;
	private String studyContent;
	private String studyAddress;
	private Double studyLatitude;
	private Double studyLongitude;
	private Long studyMemberLimit;
	private String studyCategory;
	private Long userSeq;
	private Date studyDate;
	
}
