package com.gobookee.reports.model.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Reports {

	private Long repSeq;
	private String repReason;
	private Long repBoardSeq;
	private Timestamp repCreateTime;
	private Long userSeq;

}
