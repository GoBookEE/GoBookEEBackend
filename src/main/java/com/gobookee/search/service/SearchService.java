package com.gobookee.search.service;

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

    public int getTotalCount(Search search) {
        Connection conn = getConnection();
        int result = 0;
        switch (search.getTab()) {
            case "review":
                result = searchDao.getReviewTotalCount(conn, search);
                break;
            case "book":
                result = searchDao.getBookTotalCount(conn, search);
                break;
            case "place":
                result = searchDao.getPlaceTotalCount(conn, search);
                break;
            case "study":
                result = searchDao.getStudyTotalCount(conn, search);
                break;
        }
        close(conn);
        return result;
    }
}
