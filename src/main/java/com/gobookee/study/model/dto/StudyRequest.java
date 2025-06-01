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
public class StudyRequest {

	private Long requestSeq;
	private String requestMsg;
	private String requestConfirm;
	private Timestamp requestDeleteTime;
	private Long userSeq;
	private Long studySeq;
	private Long studyMemberLimit;
	
	private String userProfile;
	private String userNickName;
	private Long userSpeed;
	private Long hostSeq;
}
