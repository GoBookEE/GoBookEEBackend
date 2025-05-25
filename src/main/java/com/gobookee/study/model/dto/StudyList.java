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
public class StudyList {
	private long studySeq;
	private String studyTitle;
	private Timestamp studyDate;
	private int studyMemberLimit;
	private String studyPlace;
	private int confirmedCount;
	private String photoRenamedName;
	private int likeCount;
	private int dislikeCount;
}
