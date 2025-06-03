package com.gobookee.book.service;

import com.gobookee.book.model.dao.BookCategoryDao;
import com.gobookee.book.model.dto.BookCategory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static com.gobookee.book.model.dao.BookCategoryDao.bookCategoryDao;
import static com.gobookee.common.JDBCTemplate.getConnection;

public class BookCategoryService {
    private static final BookCategoryService SERVICE = new BookCategoryService();
    private BookCategoryService() {}
    public static BookCategoryService bookCategoryService() { return SERVICE; }
    private BookCategoryDao dao = bookCategoryDao();

    public List<BookCategory> getBookCategory (int cid, int level) {
        List<BookCategory> catogoryList = new ArrayList<>();
        Connection conn = getConnection();
        catogoryList = dao.getCategoryList(conn, cid, level);
        return catogoryList;
    }
}
