package com.gobookee.search.model.dao;

import com.gobookee.book.model.dto.Book;
import com.gobookee.place.model.dto.Place;
import com.gobookee.search.model.dto.Search;
import com.gobookee.search.model.dto.SearchReview;
import com.gobookee.study.model.dto.Study;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SearchDao {
    private static SearchDao searchDao;
    private Properties sqlProp = new Properties();
    private PreparedStatement pstmt;
    private ResultSet rs;

    private SearchDao() {
        String path = SearchDao.class.getResource("/config/search-sql.properties").getPath();
        try (FileReader fr = new FileReader(path)) {
            sqlProp.load(fr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SearchDao searchDao() {
        if (searchDao == null) {
            searchDao = new SearchDao();
        }
        return searchDao;
    }


    public List<?> search(Connection conn, Search search) {
        switch (search.getTab()) {
            case "review":
                return searchReview(conn, search);
            case "book":
                return searchBook(conn, search);
            case "study":
                return searchStudy(conn, search);
            case "place":
                return searchPlace(conn, search);
            default:
                return new ArrayList<>();
        }
    }

    private List<SearchReview> searchReview(Connection conn, Search search) {
        long numPerPage = 5;
        List<SearchReview> list = new ArrayList<>();
        pstmt = null;
        rs = null;

        StringBuilder sql = new StringBuilder(sqlProp.getProperty("searchReviewBase"));
        String keyword = "%" + search.getKeyword().toLowerCase() + "%";
        switch (search.getFilter()) {
            case "title":
                sql.append(" AND LOWER(RV.REVIEW_TITLE) LIKE ?");
                break;
            case "writer":
                sql.append(" AND LOWER(U.USER_NICKNAME) LIKE ?");
                break;
            case "content":
                sql.append(" AND LOWER(RV.REVIEW_CONTENTS) LIKE ?");
                break;
            case "bookTitle":
                sql.append(" AND LOWER(BK.BOOK_TITLE) LIKE ?");
                break;
        }
        sql.append(" GROUP BY RV.REVIEW_SEQ, RV.REVIEW_TITLE, RV.REVIEW_CONTENTS, RV.REVIEW_CREATE_TIME, ")
                .append("RV.REVIEW_RATE, RV.REVIEW_EDIT_TIME, RV.REVIEW_IS_PUBLIC, RV.USER_SEQ, RV.BOOK_SEQ, ")
                .append("BK.BOOK_TITLE, BK.BOOK_COVER ")
                .append("ORDER BY RV.REVIEW_CREATE_TIME DESC")
                .append(") R ) WHERE RNUM BETWEEN ? AND ?");

        try {
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, keyword);
            pstmt.setLong(2, (search.getCPage() - 1) * numPerPage + 1);
            pstmt.setLong(3, search.getCPage() * numPerPage);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(SearchReview.from(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Book> searchBook(Connection conn, Search search) {
        // TODO: 책 검색 구현
        return null;
    }

    private List<Study> searchStudy(Connection conn, Search search) {
        // TODO: 스터디 검색 구현
        return null;
    }

    private List<Place> searchPlace(Connection conn, Search search) {
        // TODO: 공간 검색 구현
        return null;
    }
}
