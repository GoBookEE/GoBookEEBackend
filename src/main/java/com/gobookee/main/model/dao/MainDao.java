package com.gobookee.main.model.dao;

import com.gobookee.book.model.dto.Book;
import com.gobookee.main.model.dto.ReviewTopResponse;
import com.gobookee.place.model.dto.Place;
import com.gobookee.place.model.dto.PlaceViewResponse;
import com.gobookee.review.model.dto.Review;
import com.gobookee.review.model.dto.ReviewListResponse;
import com.gobookee.study.model.dto.SearchStudyResponse;
import com.gobookee.study.model.dto.Study;
import com.gobookee.study.model.dto.StudyList;
import com.gobookee.users.model.dto.User;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MainDao {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Properties reviewProp = new Properties();
    Properties mainProp = new Properties();
    private static MainDao dao;
    public static MainDao mainDao() {
        if (dao == null) { dao = new MainDao(); }
        return dao;
    }

    private MainDao() {
        String mainPath = MainDao.class.getClassLoader().getResource("/config/main-sql.properties").getPath();
        try (FileReader fr = new FileReader(mainPath)){
            mainProp.load(fr);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ReviewTopResponse> getTop3review (Connection conn) {
        List<ReviewTopResponse> list = new ArrayList<>();
        try{
            pstmt = conn.prepareStatement(mainProp.getProperty("getBestReviewsTop3"));
            rs=pstmt.executeQuery();
            while (rs.next()){
                list.add(getReviewTopResponse(rs));
            };
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public List<ReviewListResponse> getRecent10review (Connection conn) {
        List<ReviewListResponse> list = new ArrayList<>();
        try{
            pstmt = conn.prepareStatement(mainProp.getProperty("getRecent10review"));
            rs=pstmt.executeQuery();
            while (rs.next()){
                list.add(getRecentReview(rs));
            };
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public List<Book> getHot5book (Connection conn) {
        List<Book> list = new ArrayList<>();
        try{
            pstmt = conn.prepareStatement(mainProp.getProperty("getHot5book"));
            rs=pstmt.executeQuery();
            while (rs.next()){
                list.add(getBook(rs));
            };
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public List<Book> getTop9book (Connection conn) {
        List<Book> list = new ArrayList<>();
        try{
            pstmt = conn.prepareStatement(mainProp.getProperty("getTop9book"));
            rs=pstmt.executeQuery();
            while (rs.next()){
                list.add(getTopbook(rs));
            };
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public List<StudyList> getTop9study (Connection conn) {
        List<StudyList> list = new ArrayList<>();
        try{
            pstmt = conn.prepareStatement(mainProp.getProperty("getTop9study"));
            rs=pstmt.executeQuery();
            while (rs.next()){
                list.add(getTopStudy(rs));
            };
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public List<PlaceViewResponse> getRan5place (Connection conn) {
        List<PlaceViewResponse> list = new ArrayList<>();
        try{
            pstmt = conn.prepareStatement(mainProp.getProperty("getRan5place"));
            rs=pstmt.executeQuery();
            while (rs.next()){
                list.add(getRanPlace(rs));
            };
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public List<User> getTop3user (Connection conn) {
        List<User> list = new ArrayList<>();
        try{
            pstmt = conn.prepareStatement(mainProp.getProperty("getTop3user"));
            rs=pstmt.executeQuery();
            while (rs.next()){
                list.add(getUser(rs));
            };
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    public ReviewTopResponse getReviewTopResponse(ResultSet rs) throws SQLException {
        return ReviewTopResponse.builder()
                .reviewSeq(rs.getLong("REVIEW_SEQ"))
                .reviewTitle(rs.getString("REVIEW_TITLE"))
                .reviewContents(rs.getString("REVIEW_CONTENTS"))
                .reviewCreateTime(rs.getTimestamp("REVIEW_CREATE_TIME"))
                .reviewRate(rs.getInt("REVIEW_RATE"))
                .reviewEditTime(rs.getTimestamp("REVIEW_EDIT_TIME"))
                .bookTitle(rs.getString("BOOK_TITLE"))
                .bookCover(rs.getString("BOOK_COVER"))
                .bookAuthor(rs.getString("BOOK_AUTHOR"))
                .bookPublisher(rs.getString("BOOK_PUBLISHER"))
                .bookPubdate(rs.getDate("BOOK_PUBDATE"))
                .recommendCount(rs.getInt("RECOMMEND_COUNT"))
                .commentsCount(rs.getInt("COMMENTS_COUNT")).build();
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

    private User getUser(ResultSet rs) throws SQLException {
        return User.builder()
                .userNickName(rs.getString("user_nickname"))
                .userProfile(rs.getString("user_profile"))
                .userSpeed(rs.getLong("user_speed"))
                .build();
    }

    private ReviewListResponse getRecentReview(ResultSet rs) throws SQLException {
        return ReviewListResponse.builder()
                .bookCover(rs.getString("BOOK_COVER"))
                .bookTitle(rs.getString("BOOK_TITLE"))
                .reviewTitle(rs.getString("REVIEW_TITLE"))
                .reviewContents(rs.getString("REVIEW_CONTENTS"))
                .reviewRate(rs.getInt("REVIEW_RATE"))
                .reviewCreateTime(rs.getTimestamp("REVIEW_CREATE_TIME"))
                .commentsCount(rs.getInt("COMMENTS_COUNT"))
                .build();
    }
    private Book getTopbook(ResultSet rs) throws SQLException {
        return Book.builder()
                .bookSeq(rs.getLong("BOOK_SEQ"))
                .bookCover(rs.getString("BOOK_COVER"))
                .bookTitle(rs.getString("BOOK_TITLE"))
                .bookDescription(rs.getString("BOOK_DESCRIPTION"))
                .bookAuthor(rs.getString("BOOK_AUTHOR"))
                .bookPublisher(rs.getString("BOOK_PUBLISHER"))
                .bookPubdate(rs.getDate("BOOK_PUBDATE"))
                .reviewRateAvg(rs.getDouble("REVIEW_RATE"))
                .reviewCount(rs.getInt("REVIEW_COUNT"))
                .build();
    }

    private PlaceViewResponse getRanPlace(ResultSet rs) throws SQLException {
        return PlaceViewResponse.builder()
                .placeSeq(rs.getLong("PLACE_SEQ"))
                .placeTitle(rs.getString("PLACE_TITLE"))
                .placeContents(rs.getString("PLACE_CONTENTS"))
                .photoNames(Collections.singletonList(rs.getString("PHOTO_RENAMED_NAME")))
                .placeRecCount(rs.getLong("RECOMMEND_COUNT"))
                .placeNonRecCount(rs.getLong("NON_RECOMMEND_COUNT"))
                .build();
    }
    private StudyList getTopStudy(ResultSet rs) throws SQLException {
        return StudyList.builder()
                .studySeq(rs.getLong("study_seq"))
                .studyTitle(rs.getString("study_title"))
                .studyDate(rs.getDate("study_date"))
                .studyMemberLimit(rs.getInt("study_member_limit"))
                .studyAddress(rs.getString("study_address"))
                .confirmedCount(rs.getInt("confirmed_count"))
                .photoRenamedName(rs.getString("photo_name"))
                .likeCount(rs.getInt("like_count"))
                .dislikeCount(rs.getInt("dislike_count"))
                .build();
    }
}