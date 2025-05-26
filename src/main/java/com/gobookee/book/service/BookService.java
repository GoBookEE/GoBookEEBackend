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

    public List<Book> getAllBookList (int cPage, int numPage, int userSeq) {
        Connection conn = getConnection();
        List<Book> books = new ArrayList<Book>();
        books = dao.getAllBookList(conn,cPage, numPage, userSeq);
        close(conn);
        return books;
    }

    public Book getBookDetailBySeq (int bookSeq) {
        Connection conn = getConnection();
        Book book = dao.getBookDetailBySeq(conn, bookSeq);
        close(conn);
        return book;
    }

    public int getAllBookCount () {
        Connection conn = getConnection();
        int bookCount= dao.getAllBookCount(conn);
        close(conn);
        return bookCount;
    }
}
