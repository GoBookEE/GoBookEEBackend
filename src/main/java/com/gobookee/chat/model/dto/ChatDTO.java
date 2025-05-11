package com.gobookee.chat.model.dto;

import java.sql.Timestamp;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

public class ChatDTO {
	private Long chatSeq;
	private String chatMessage;
	private Timestamp chatCreateTime;
	private Long studySeq;
	private Long userSeq;
}
