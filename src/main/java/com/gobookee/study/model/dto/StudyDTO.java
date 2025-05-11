package com.gobookee.study.model.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class StudyDTO {
	private Long studySeq;
	private String studyTitle;
	private Timestamp studyDate;
	private Integer studyMemberLimit;
	private Timestamp studyCreateTime;
	private Timestamp studyEditTime;
	private String studyPlace;
	private Long userSeq ;
	private Long placeSeq ;
}
