package com.gobookee.mypage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyStudy {

	private Long userSeq;
	private String status;
	private Integer cPage;

}
