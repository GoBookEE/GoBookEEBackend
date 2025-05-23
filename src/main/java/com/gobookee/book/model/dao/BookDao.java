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

    public List<Book> getAllBookList(Connection conn, int cPage, int numPage){
        List<Book> bookList = new ArrayList<Book>();
        try{
            pstmt = conn.prepareStatement(sql.getProperty("getBookListPaging"));
            pstmt.setInt(1, cPage);
            pstmt.setInt(2, numPage);
            rs=pstmt.executeQuery();
            while(rs.next()){
                bookList.add(getBook(rs));
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
                .bookSeq(rs.getLong(2))
                .bookID(rs.getLong(3))
                .bookTitle(rs.getString(4))
                .bookLink(rs.getString(5))
                .bookAuthor(rs.getString(6))
                .bookPubdate(rs.getDate(7)!=null?rs.getDate(7):null)
                .bookDescription(rs.getString(8))
                .bookIsbn(rs.getString(9))
                .bookIsbn13(rs.getString(10))
                .bookPriceSales(rs.getInt(11))
                .bookPriceStandard(rs.getInt(12))
                .bookMallType(rs.getString(13))
                .bookStockStatus(rs.getString(14))
                .bookMileage(rs.getInt(15))
                .bookCover(rs.getString(16))
                .bookCategoryId(rs.getString(17))
                .bookCategoryName(rs.getString(18))
                .bookPublisher(rs.getString(19))
                .bookSalesPoint(rs.getInt(20))
                .bookAdult(rs.getString(21))
                .bookFixedPrice(rs.getString(22))
                .bookCustomerReviewRank(rs.getInt(23))
                .bookSeriesId(rs.getString(24))
                .bookSeriesLink(rs.getString(25))
                .bookSeriesName(rs.getString(26))
                .bookSubInfo(rs.getString(27))
                .build();
    }
}
