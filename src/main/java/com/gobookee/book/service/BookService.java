package com.gobookee.book.service;

import com.gobookee.book.model.dao.BookDao;
import com.gobookee.book.model.dto.Book;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static com.gobookee.book.model.dao.BookDao.bookDao;
import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.getConnection;

public class BookService {
    private static final BookService SERVICE = new BookService();
    private BookService() {}
    public static BookService bookService() { return SERVICE; }
    private BookDao dao = bookDao();

    public List<Book> getAllBookList (int cPage, int numPage) {
        Connection conn = getConnection();
        List<Book> books = new ArrayList<Book>();
        books = dao.getAllBookList(conn,cPage, numPage);
        close(conn);
        return books;
    }

    public int getAllBookCount () {
        Connection conn = getConnection();
        int bookCount= dao.getAllBookCount(conn);
        close(conn);
        return bookCount;
    }
}
