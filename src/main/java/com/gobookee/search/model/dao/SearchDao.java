package com.gobookee.search.model.dao;

import com.gobookee.common.JDBCTemplate;
import com.gobookee.place.model.dto.Place;
import com.gobookee.search.model.dto.SearchBook;
import com.gobookee.search.model.dto.Search;
import com.gobookee.search.model.dto.SearchReview;
import com.gobookee.search.model.dto.SearchStudy;

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
                .append("BK.BOOK_TITLE, BK.BOOK_COVER, U.USER_NICKNAME ")
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

    private List<SearchBook> searchBook(Connection conn, Search search) {
        long numPerPage = 5;
        List<SearchBook> list = new ArrayList<>();
        pstmt = null;
        rs = null;

        StringBuilder sql = new StringBuilder(sqlProp.getProperty("searchBookBase"));
        String keyword = "%" + search.getKeyword().toLowerCase() + "%";

        // 동적 필터 조건 추가
        switch (search.getFilter()) {
            case "bookTitle":
                sql.insert(sql.indexOf("ORDER BY"), " AND LOWER(B.BOOK_TITLE) LIKE ? ");
                break;
            case "publisher":
                sql.insert(sql.indexOf("ORDER BY"), " AND LOWER(B.BOOK_PUBLISHER) LIKE ? ");
                break;
            case "author":
                sql.insert(sql.indexOf("ORDER BY"), " AND LOWER(B.BOOK_AUTHOR) LIKE ? ");
                break;
            case "titleContent":
                sql.insert(sql.indexOf("ORDER BY"), " AND (LOWER(B.BOOK_TITLE) LIKE ? OR LOWER(B.BOOK_DESCRIPTION) LIKE ?) ");
                break;
        }

        try {
            pstmt = conn.prepareStatement(sql.toString());

            int idx = 1;

            pstmt.setString(idx++, keyword);
            if ("titleContent".equals(search.getFilter())) {
                pstmt.setString(idx++, keyword); // description도 한 번 더
            }

            pstmt.setLong(idx++, (search.getCPage() - 1) * numPerPage + 1);
            pstmt.setLong(idx++, search.getCPage() * numPerPage);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(SearchBook.from(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(rs);
            JDBCTemplate.close(pstmt);
        }

        return list;
    }


    private List<SearchStudy> searchStudy(Connection conn, Search search) {
        long numPerPage = 5;
        List<SearchStudy> list = new ArrayList<>();
        pstmt = null;
        rs = null;

        StringBuilder sql = new StringBuilder(sqlProp.getProperty("searchStudyBase"));
        String keyword = "%" + search.getKeyword().toLowerCase() + "%";

        switch (search.getFilter()) {
            case "studyTitle":
                sql.insert(sql.indexOf("ORDER BY"), " AND LOWER(S.STUDY_TITLE) LIKE ? ");
                break;
            case "studyPlace":
                sql.insert(sql.indexOf("ORDER BY"), " AND LOWER(S.STUDY_ADDRESS) LIKE ? ");
                break;
            case "nickname":
                // 닉네임 검색은 JOIN 필요: STUDY ← USER
                sql.insert(sql.indexOf("ORDER BY"), " AND LOWER(U.USER_NICKNAME) LIKE ? ");
                break;
        }

        try {
            pstmt = conn.prepareStatement(sql.toString());
            int idx = 1;

            pstmt.setString(idx++, keyword);
            pstmt.setLong(idx++, (search.getCPage() - 1) * numPerPage + 1);
            pstmt.setLong(idx++, search.getCPage() * numPerPage);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(SearchStudy.from(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCTemplate.close(rs);
            JDBCTemplate.close(pstmt);
        }
        return list;
    }


    private List<Place> searchPlace(Connection conn, Search search) {
        // TODO: 공간 검색 구현
        return null;
    }

    public int getReviewTotalCount(Connection conn, Search search) {
        int total = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM REVIEW RV ");
        sql.append("LEFT JOIN GO_USER U ON RV.USER_SEQ = U.USER_SEQ ");
        sql.append("LEFT JOIN BOOK BK ON RV.BOOK_SEQ = BK.BOOK_SEQ ");
        sql.append("WHERE RV.REVIEW_DELETE_TIME IS NULL ");
        sql.append("AND RV.REVIEW_IS_PUBLIC = 'Y' ");

        String keyword = "%" + search.getKeyword().toLowerCase() + "%";

        switch (search.getFilter()) {
            case "title":
                sql.append(" AND LOWER(RV.REVIEW_TITLE) LIKE ? ");
                break;
            case "writer":
                sql.append(" AND LOWER(U.USER_NICKNAME) LIKE ? ");
                break;
            case "content":
                sql.append(" AND LOWER(RV.REVIEW_CONTENTS) LIKE ? ");
                break;
            case "bookTitle":
                sql.append(" AND LOWER(BK.BOOK_TITLE) LIKE ? ");
                break;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setString(1, keyword);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public int getBookTotalCount(Connection conn, Search search) {
        int total = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM BOOK B WHERE 1=1 ");
        String keyword = "%" + search.getKeyword().toLowerCase() + "%";

        switch (search.getFilter()) {
            case "bookTitle":
                sql.append(" AND LOWER(B.BOOK_TITLE) LIKE ? ");
                break;
            case "publisher":
                sql.append(" AND LOWER(B.BOOK_PUBLISHER) LIKE ? ");
                break;
            case "author":
                sql.append(" AND LOWER(B.BOOK_AUTHOR) LIKE ? ");
                break;
            case "titleContent":
                sql.append(" AND (LOWER(B.BOOK_TITLE) LIKE ? OR LOWER(B.BOOK_DESCRIPTION) LIKE ?) ");
                break;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            pstmt.setString(idx++, keyword);
            if ("titleContent".equals(search.getFilter())) {
                pstmt.setString(idx++, keyword);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public int getPlaceTotalCount(Connection conn, Search search) {
        return 0;
    }

    public int getStudyTotalCount(Connection conn, Search search) {
        int total = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ");
        sql.append("FROM STUDY S ");
        sql.append("LEFT JOIN GO_USER U ON S.USER_SEQ = U.USER_SEQ ");
        sql.append("WHERE S.STUDY_DELETE_TIME IS NULL ");
        sql.append("AND S.STUDY_IS_PUBLIC = 'Y' ");

        String keyword = "%" + search.getKeyword().toLowerCase() + "%";

        switch (search.getFilter()) {
            case "studyTitle":
                sql.append(" AND LOWER(S.STUDY_TITLE) LIKE ? ");
                break;
            case "studyPlace":
                sql.append(" AND LOWER(S.STUDY_ADDRESS) LIKE ? ");
                break;
            case "nickname":
                sql.append(" AND LOWER(U.USER_NICKNAME) LIKE ? ");
                break;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setString(1, keyword);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

}
