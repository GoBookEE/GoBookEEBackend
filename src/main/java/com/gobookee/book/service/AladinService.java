package com.gobookee.book.service;

import static com.gobookee.book.external.AladinApiClient.aladinApiClient;
import static com.gobookee.book.model.dao.BookDao.bookDao;
import static com.gobookee.common.JDBCTemplate.*;

import com.gobookee.book.model.dto.Book;
import com.gobookee.book.model.dao.BookDao;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class AladinService {
    private static final AladinService SERVICE = new AladinService();

    private AladinService() {
    }

    public static AladinService aladinService() {
        return SERVICE;
    }
    private BookDao dao = bookDao();

    public int insertDummyBooks(Book b) {
        int result = 0;
        Connection conn = getConnection();
        result = dao.insertBook(conn,b);
        if(result >0)commit(conn);
        else rollback(conn);
        close(conn);
        return result;
    }
}
