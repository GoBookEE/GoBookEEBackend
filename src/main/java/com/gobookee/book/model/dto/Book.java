package com.gobookee.book.model.dto;

import java.sql.Date;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Book {
	private Long bookSeq;						//책시퀀스
	private Long bookID;						//책아이디
	private String bookTitle;					//책제목
	private String bookLink;					//책 링크
	private String bookAuthor;					//책 저자
	private Date bookPubdate;				//책 출판일
	private String bookDescription;				//책 설명
	private String bookIsbn;					//책 ISBN
	private String bookIsbn13;					//책 ISBN13
	private Integer bookPriceSales;				//책 판매가격
	private Integer bookPriceStandard;			//책 판매정가
	private String bookMallType;				//책 몰타입
	private String bookStockStatus;				//책 재고 상태
	private Integer bookMileage;				//책 마일리지
	private String bookCover;					//책 커버이미지
	private String bookCategoryId;				//책 카테고리 아이디
	private String bookCategoryName;			//책 카테고리 네임
	private String bookPublisher;				//책 출판사
	private Integer bookSalesPoint;				//책 판매지수
	private String bookAdult;					//책 성인등급여부
	private String bookFixedPrice;				//책 정가제 여부
	private Integer bookCustomerReviewRank;		//회원 리뷰 평점
	private String bookSeriesId;				//시리즈 ID
	private String bookSeriesLink;				//해당 시리즈의 조회 URL
	private String bookSeriesName;				//시리즈 이름
	private String bookSubInfo;					//부가정

	private Integer reviewCount;
	private Double reviewRateAvg;
	private Integer wishCount;
}
