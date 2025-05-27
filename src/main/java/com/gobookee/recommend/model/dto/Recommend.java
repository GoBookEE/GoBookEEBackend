package com.gobookee.recommend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Recommend {

	private Long recSeq;
	private String recType;
	private Long recBoardSeq;
	private Long userSeq;

}
