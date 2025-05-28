package com.gobookee.search.service;

import com.gobookee.review.model.dao.ReviewDAO;
import com.gobookee.search.model.dao.SearchDao;
import com.gobookee.search.model.dto.Search;

import java.sql.Connection;
import java.util.List;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.getConnection;

public class SearchService {
    private static SearchService searchService;
    private Connection conn;
    private SearchDao searchDao = SearchDao.searchDao();
    private ReviewDAO reviewDAO = ReviewDAO.reviewDao();

    private SearchService() {
    }

    public static SearchService searchService() {
        if (searchService == null) {
            searchService = new SearchService();
        }
        return searchService;
    }


    public Object search(Search search) {
        conn = getConnection();
        List<?> searchList = searchDao.search(conn, search);
        close(conn);
        return searchList;
    }
}
