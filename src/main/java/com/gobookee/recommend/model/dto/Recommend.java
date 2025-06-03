package com.gobookee.recommend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter

public class Recommend {

	private Long recSeq;
	private String recType;
	private Long recBoardSeq;
	private Long userSeq;

}
