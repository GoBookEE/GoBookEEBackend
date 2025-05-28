package com.gobookee.book.service;

import com.gobookee.book.model.dao.BookDao;
import com.gobookee.book.model.dto.Book;
import com.gobookee.review.model.dto.ReviewBookSeqResponse;

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

    public Book getBookDetailBySeq (int bookSeq,int userSeq) {
        Connection conn = getConnection();
        Book book = dao.getBookDetailBySeq(conn, bookSeq, userSeq);
        close(conn);
        return book;
    }
    public int getBookDetailByBookID (int bookId) {
        Connection conn = getConnection();
        int bookSeq = dao.getBookDetailByBookID(conn, bookId);
        close(conn);
        return bookSeq;
    }

    public int getAllBookCount () {
        Connection conn = getConnection();
        int bookCount= dao.getAllBookCount(conn);
        close(conn);
        return bookCount;
    }


    public List<ReviewBookSeqResponse> getReviewByBookSeq(int userSeq, Long bookSeq, String orderBy, int cPage, int numPerPage) {
        Connection conn = getConnection();
        List<ReviewBookSeqResponse> review = dao.getReviewByBookSeq(conn,userSeq,bookSeq, orderBy,cPage,numPerPage);
        close(conn);
        return review;
    }

    public int getReviewByBookSeqCount(int bookSeq) {
        Connection conn = getConnection();
        int listCount = dao.getReviewByBookSeqCount(conn, bookSeq);
        close(conn);
        return listCount;
    }

}
