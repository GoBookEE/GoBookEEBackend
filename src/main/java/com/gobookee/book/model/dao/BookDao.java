package com.gobookee.book.model.dao;

import static com.gobookee.common.JDBCTemplate.close;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.gobookee.book.model.dto.Book;
import com.gobookee.book.model.dto.BookReviewResponse;
import com.gobookee.common.JDBCTemplate;
import com.gobookee.review.model.dto.ReviewBookSeqResponse;

public class BookDao {
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	Properties sqlProp = new Properties();

	private static BookDao dao;

	public static BookDao bookDao() {
		if (dao == null) {
			dao = new BookDao();
		}
		return dao;
	}

	private BookDao() {
		String path = BookDao.class.getResource("/config/book-sql.properties").getPath();
		try (FileReader fr = new FileReader(path)) {
			sqlProp.load(fr);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public List<Book> getAllBookList(Connection conn, int cPage, int numPage, int userSeq) {
		List<Book> bookList = new ArrayList<Book>();
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getBookListPaging"));
			pstmt.setInt(1, userSeq);
			pstmt.setInt(2, (cPage - 1) * numPage + 1);
			pstmt.setInt(3, cPage * numPage);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Book book = getBook(rs);
				book.setReviewCount(rs.getInt("REVIEW_COUNT"));
				book.setReviewRateAvg(rs.getDouble("REVIEW_RATE_AVG"));
				book.setWishCount(rs.getInt("WISH_CHECK"));
				bookList.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return bookList;
	}

	public int getAllBookCount(Connection conn) {
		int bookCount = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getBookCount"));
			rs = pstmt.executeQuery();
			while (rs.next())
				bookCount = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return bookCount;
	}

	public Book getBookDetailBySeq(Connection conn, int bookSeq, int userSeq) {
		Book book = null;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getBookDetailBySeq"));
			pstmt.setInt(1, userSeq);
			pstmt.setInt(2, bookSeq);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				book = getBook(rs);
				book.setReviewCount(rs.getInt("REVIEW_COUNT"));
				book.setReviewRateAvg(rs.getDouble("REVIEW_RATE_AVG"));
				book.setWishCount(rs.getInt("WISH_CHECK"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return book;
	}

	public int getBookDetailByBookID(Connection conn, int bookID) {
		int bookSeq = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getBookDetailByID"));
			pstmt.setInt(1, bookID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bookSeq = rs.getInt("BOOK_SEQ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return bookSeq;
	}

	public int insertBook(Connection conn, Book b) {
		int result = 0;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("insertBook"));
			pstmt.setLong(1, b.getBookID());// BOOK_ID
			pstmt.setString(2, b.getBookTitle());// BOOK_TITLE
			pstmt.setString(3, b.getBookLink());// BOOK_LINK
			pstmt.setString(4, b.getBookAuthor());// BOOK_AUTHOR
			pstmt.setDate(5, b.getBookPubdate());// BOOK_PUBDATE
			pstmt.setString(6, b.getBookDescription());// BOOK_DESCRIPTION
			pstmt.setString(7, b.getBookIsbn());// BOOK_ISBN
			pstmt.setString(8, b.getBookIsbn13());// BOOK_ISBN13
			pstmt.setInt(9, b.getBookPriceSales());// BOOK_PRICESALES
			pstmt.setInt(10, b.getBookPriceStandard());// BOOK_PRICESTANDARD
			pstmt.setString(11, b.getBookMallType());// BOOK_MALLTYPE
			pstmt.setString(12, b.getBookStockStatus());// BOOK_STOCKSTATUS
			pstmt.setInt(13, b.getBookMileage());// BOOK_MILEAGE
			pstmt.setString(14, b.getBookCover());// BOOK_COVER
			pstmt.setString(15, b.getBookCategoryId());// BOOK_CATEGORYID
			pstmt.setString(16, b.getBookCategoryName());// BOOK_CATEGORYNAME
			pstmt.setString(17, b.getBookPublisher());// BOOK_PUBLISHER
			pstmt.setInt(18, b.getBookSalesPoint());// BOOK_SALESPOINT
			pstmt.setString(19, b.getBookAdult());// BOOK_ADULT
			pstmt.setString(20, b.getBookFixedPrice());// BOOK_FIXEDPRICE
			pstmt.setInt(21, b.getBookCustomerReviewRank());// BOOK_CUSTOMERREVIEWRANK
			pstmt.setString(22, b.getBookSeriesId());// BOOK_SERIESID
			pstmt.setString(23, b.getBookSeriesLink());// BOOK_SERIESLINK
			pstmt.setString(24, b.getBookSeriesName());// BOOK_SERIESNAME
			pstmt.setString(25, "");// BOOK_SUBINFO
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	public List<ReviewBookSeqResponse> getReviewByBookSeq(Connection conn, int userSeq, Long bookSeq, String orderBy,
			int cPage, int numPerPage) {
		List<ReviewBookSeqResponse> reviewList = new ArrayList<>();
		try {
			String sql = sqlProp.getProperty("getReviewByBookSeq");
			sql = sql.replace("${orderColumn}", orderBy);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userSeq);
			pstmt.setLong(2, bookSeq);
			pstmt.setInt(3, (cPage - 1) * numPerPage + 1);
			pstmt.setInt(4, cPage * numPerPage);
			rs = pstmt.executeQuery();
			while (rs.next())
				reviewList.add(getReviewBookSeqResponse(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return reviewList;
	}

	public int getReviewByBookSeqCount(Connection conn, int bookSeq) {
		int listCount = 0;
		try {
			String sql = sqlProp.getProperty("getReviewByBookSeqCount");
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bookSeq);
			rs = pstmt.executeQuery();
			while (rs.next())
				listCount = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
		}
		return listCount;
	}

	public BookReviewResponse getBookForReview(Connection conn, Long bookSeq) {
		BookReviewResponse book = null;
		try {
			pstmt = conn.prepareStatement(sqlProp.getProperty("getBookForReview"));
			pstmt.setLong(1, bookSeq);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				book = BookReviewResponse.builder().bookSeq(bookSeq).bookTitle(rs.getString("BOOK_TITLE"))
						.bookAuthor(rs.getString("BOOK_AUTHOR")).bookPublisher(rs.getString("BOOK_PUBLISHER"))
						.bookPubDate(rs.getString("BOOK_PUBDATE")).bookCover(rs.getString("BOOK_COVER")).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return book;
	}


	public int wishCheck(Connection conn, long userSeq, long bookSeq){
		int result = 0;
		try{
			String sql = sqlProp.getProperty("wishCheck");
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1,userSeq);
			pstmt.setLong(2,bookSeq);
			result = pstmt.executeUpdate();
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		return result;
	}
	public int wishUncheck(Connection conn, long userSeq, long bookSeq){
		int result = 0;
		try{
			String sql = sqlProp.getProperty("wishUncheck");
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1,userSeq);
			pstmt.setLong(2,bookSeq);
			result = pstmt.executeUpdate();
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		return result;
	}


	public int bookWishCountByUser(Connection conn, long userSeq){
		int result = 0;
		try{
			String sql = sqlProp.getProperty("bookWishCountByUser");
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1,userSeq);
			rs = pstmt.executeQuery();
			if (rs.next()) result = rs.getInt(1);
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		return result;
	}



	public List<Book> getWishListByUserSeq(Connection conn, long userSeq,int cPage, int numPerPage){
		List<Book> bookList = new ArrayList<>();
		try{
			String sql = sqlProp.getProperty("getWishListByUserSeq");
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1,userSeq);

			pstmt.setInt(2, (cPage - 1) * numPerPage + 1);
			pstmt.setInt(3, cPage * numPerPage);
			rs = pstmt.executeQuery();
			while (rs.next()) bookList.add(getBook(rs));
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		return bookList;
	}



	public int getWishCountByUserSeq(Connection conn, long userSeq){
		int result = 0;
		try{
			String sql = sqlProp.getProperty("getWishCountByUserSeq");
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1,userSeq);
			rs = pstmt.executeQuery();
			if (rs.next()) result = rs.getInt(1);
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			close(pstmt);
		}
		return result;
	}


	public ReviewBookSeqResponse getReviewBookSeqResponse(ResultSet rs) throws SQLException {

		return ReviewBookSeqResponse.builder().reviewSeq(rs.getLong("REVIEW_SEQ"))
				.reviewTitle(rs.getString("REVIEW_TITLE")).reviewContents(rs.getString("REVIEW_CONTENTS"))
				.reviewCreateTime(rs.getTimestamp("REVIEW_CREATE_TIME")).recommendCount(rs.getInt("RECOMMEND_COUNT"))
				.nonRecommendCount(rs.getInt("NON_RECOMMEND_COUNT")).recommendType(rs.getInt("REC_TYPE"))
				.userProfile(rs.getString("USER_PROFILE")).userNickname(rs.getString("USER_NICKNAME")).build();
	}

	public Book getBook(ResultSet rs) throws SQLException {
		new Book();
		return Book.builder().bookSeq(rs.getLong("BOOK_SEQ")).bookID(rs.getLong("BOOK_ID"))
				.bookTitle(rs.getString("BOOK_TITLE")).bookLink(rs.getString("BOOK_LINK"))
				.bookAuthor(rs.getString("BOOK_AUTHOR")).bookPubdate(rs.getDate("BOOK_PUBDATE"))
				.bookDescription(rs.getString("BOOK_DESCRIPTION")).bookIsbn(rs.getString("BOOK_ISBN"))
				.bookIsbn13(rs.getString("BOOK_ISBN13")).bookPriceSales(rs.getInt("BOOK_PRICESALES"))
				.bookPriceStandard(rs.getInt("BOOK_PRICESTANDARD")).bookMallType(rs.getString("BOOK_MALLTYPE"))
				.bookStockStatus(rs.getString("BOOK_STOCKSTATUS")).bookMileage(rs.getInt("BOOK_MILEAGE"))
				.bookCover(rs.getString("BOOK_COVER")).bookCategoryId(rs.getString("BOOK_CATEGORYID"))
				.bookCategoryName(rs.getString("BOOK_CATEGORYNAME")).bookPublisher(rs.getString("BOOK_PUBLISHER"))
				.bookSalesPoint(rs.getInt("BOOK_SALESPOINT")).bookAdult(rs.getString("BOOK_ADULT"))
				.bookFixedPrice(rs.getString("BOOK_FIXEDPRICE"))
				.bookCustomerReviewRank(rs.getInt("BOOK_CUSTOMERREVIEWRANK"))
				.bookSeriesId(rs.getString("BOOK_SERIESID")).bookSeriesLink(rs.getString("BOOK_SERIESLINK"))
				.bookSeriesName(rs.getString("BOOK_SERIESNAME")).bookSubInfo(rs.getString("BOOK_SUBINFO")).build();
	}
}
