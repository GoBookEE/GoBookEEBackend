package com.gobookee.book.model.dao;

import com.gobookee.book.model.dto.Book;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.gobookee.common.JDBCTemplate.close;

public class BookDao {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Properties sql = new Properties();

    private static BookDao dao;
    public static BookDao bookDao() {
        if (dao == null) { dao = new BookDao(); }
        return dao;
    }
    private BookDao() {
        String path = BookDao.class.getResource("/config/book-sql.properties").getPath();
        try(FileReader fr = new FileReader(path)){
            sql.load(fr);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public List<Book> getAllBookList(Connection conn, int cPage, int numPage, int userSeq){
        List<Book> bookList = new ArrayList<Book>();
        try{
            pstmt = conn.prepareStatement(sql.getProperty("getBookListPaging"));
            pstmt.setInt(1, userSeq);
            pstmt.setInt(2, (cPage-1)*numPage+1);
            pstmt.setInt(3, cPage*numPage);
            rs=pstmt.executeQuery();
            while(rs.next()){
                Book book = getBook(rs);
                book.setReviewCount(rs.getInt("REVIEW_COUNT"));
                book.setReviewRateAvg(rs.getDouble("REVIEW_RATE_AVG"));
                book.setWishCount(rs.getInt("WISH_CHECK"));
                bookList.add(book);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return bookList;
    }

    public int getAllBookCount(Connection conn){
        int bookCount = 0;
        try{
            pstmt = conn.prepareStatement(sql.getProperty("getBookCount"));
            rs=pstmt.executeQuery();
            while(rs.next()) bookCount =rs.getInt(1);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return bookCount;
    }
    public Book getBookDetailBySeq(Connection conn, int bookSeq){
        Book book = null;
        try{
            pstmt = conn.prepareStatement(sql.getProperty("getBookDetailBySeq"));
            pstmt.setInt(1, bookSeq);
            rs=pstmt.executeQuery();
            while(rs.next()){
                book = getBook(rs);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return book;
    }
    public int insertBook(Connection conn, Book b){
        int result = 0;
        try{
            pstmt = conn.prepareStatement(sql.getProperty("insertBook"));
            pstmt.setLong(1,b.getBookID());//BOOK_ID
            pstmt.setString(2,b.getBookTitle());//BOOK_TITLE
            pstmt.setString(3,b.getBookLink());//BOOK_LINK
            pstmt.setString(4,b.getBookAuthor());//BOOK_AUTHOR
            pstmt.setDate(5,b.getBookPubdate());//BOOK_PUBDATE
            pstmt.setString(6,b.getBookDescription());//BOOK_DESCRIPTION
            pstmt.setString(7,b.getBookIsbn());//BOOK_ISBN
            pstmt.setString(8,b.getBookIsbn13());//BOOK_ISBN13
            pstmt.setInt(9,b.getBookPriceSales());//BOOK_PRICESALES
            pstmt.setInt(10,b.getBookPriceStandard());//BOOK_PRICESTANDARD
            pstmt.setString(11,b.getBookMallType());//BOOK_MALLTYPE
            pstmt.setString(12,b.getBookStockStatus());//BOOK_STOCKSTATUS
            pstmt.setInt(13,b.getBookMileage());//BOOK_MILEAGE
            pstmt.setString(14,b.getBookCover());//BOOK_COVER
            pstmt.setString(15,b.getBookCategoryId());//BOOK_CATEGORYID
            pstmt.setString(16,b.getBookCategoryName());//BOOK_CATEGORYNAME
            pstmt.setString(17,b.getBookPublisher());//BOOK_PUBLISHER
            pstmt.setInt(18,b.getBookSalesPoint());//BOOK_SALESPOINT
            pstmt.setString(19,b.getBookAdult());//BOOK_ADULT
            pstmt.setString(20,b.getBookFixedPrice());//BOOK_FIXEDPRICE
            pstmt.setInt(21,b.getBookCustomerReviewRank());//BOOK_CUSTOMERREVIEWRANK
            pstmt.setString(22,b.getBookSeriesId());//BOOK_SERIESID
            pstmt.setString(23,b.getBookSeriesLink());//BOOK_SERIESLINK
            pstmt.setString(24,b.getBookSeriesName());//BOOK_SERIESNAME
            pstmt.setString(25,b.getBookSubInfo());//BOOK_SUBINFO
            result = pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            close(pstmt);
        }
        return result;
    }

    public Book getBook(ResultSet rs) throws SQLException {
        new Book();
        return Book.builder()
                .bookSeq(rs.getLong("BOOK_SEQ"))
                .bookID(rs.getLong("BOOK_ID"))
                .bookTitle(rs.getString("BOOK_TITLE"))
                .bookLink(rs.getString("BOOK_LINK"))
                .bookAuthor(rs.getString("BOOK_AUTHOR"))
                .bookPubdate(rs.getDate("BOOK_PUBDATE"))
                .bookDescription(rs.getString("BOOK_DESCRIPTION"))
                .bookIsbn(rs.getString("BOOK_ISBN"))
                .bookIsbn13(rs.getString("BOOK_ISBN13"))
                .bookPriceSales(rs.getInt("BOOK_PRICESALES"))
                .bookPriceStandard(rs.getInt("BOOK_PRICESTANDARD"))
                .bookMallType(rs.getString("BOOK_MALLTYPE"))
                .bookStockStatus(rs.getString("BOOK_STOCKSTATUS"))
                .bookMileage(rs.getInt("BOOK_MILEAGE"))
                .bookCover(rs.getString("BOOK_COVER"))
                .bookCategoryId(rs.getString("BOOK_CATEGORYID"))
                .bookCategoryName(rs.getString("BOOK_CATEGORYNAME"))
                .bookPublisher(rs.getString("BOOK_PUBLISHER"))
                .bookSalesPoint(rs.getInt("BOOK_SALESPOINT"))
                .bookAdult(rs.getString("BOOK_ADULT"))
                .bookFixedPrice(rs.getString("BOOK_FIXEDPRICE"))
                .bookCustomerReviewRank(rs.getInt("BOOK_CUSTOMERREVIEWRANK"))
                .bookSeriesId(rs.getString("BOOK_SERIESID"))
                .bookSeriesLink(rs.getString("BOOK_SERIESLINK"))
                .bookSeriesName(rs.getString("BOOK_SERIESNAME"))
                .bookSubInfo(rs.getString("BOOK_SUBINFO"))
                .build();
    }
}
