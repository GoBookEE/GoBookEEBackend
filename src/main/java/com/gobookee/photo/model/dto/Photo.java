package com.gobookee.photo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Photo {
	private Long photoSeq;
	private Long photoBoardSeq;
	private String photoRenamedName;
}
