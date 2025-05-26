package com.gobookee.book.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BookReviewResponse {
	
	private Long bookSeq;
	private String bookTitle;
	private String bookAuthor;
	private String bookPublisher;
	private String bookPubDate;
	private String bookCover;

}
