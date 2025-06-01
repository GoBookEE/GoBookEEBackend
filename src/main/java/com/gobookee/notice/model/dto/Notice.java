package com.gobookee.notice.model.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Notice {
	
	private Long noticeSeq;
	
	private String noticeTitle;
	
	private Long noticeOrder;
	
	private Timestamp noticeCreateTime;
	
	private Timestamp noticeEditTime;
	
	private Timestamp noticeDeleteTime;
	
	private String noticeContents;
}
