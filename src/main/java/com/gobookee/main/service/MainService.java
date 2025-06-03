package com.gobookee.main.service;

import com.gobookee.book.model.dto.Book;
import com.gobookee.main.model.dao.MainDao;
import com.gobookee.main.model.dto.ReviewTopResponse;
import com.gobookee.place.model.dto.PlaceViewResponse;
import com.gobookee.review.model.dto.ReviewListResponse;
import com.gobookee.study.model.dto.StudyList;
import com.gobookee.users.model.dto.User;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static com.gobookee.common.JDBCTemplate.close;
import static com.gobookee.common.JDBCTemplate.getConnection;
import static com.gobookee.main.model.dao.MainDao.mainDao;

public class MainService {
    private static final MainService SERVICE = new MainService();
    private MainService() {}
    public static MainService mainService() {return SERVICE;}
    private MainDao dao = mainDao();

    public List<ReviewTopResponse>  getTop3review (){
        Connection conn = getConnection();
        List<ReviewTopResponse> list = new ArrayList<>();
        list = dao.getTop3review(conn);
        close(conn);
        return list;
    }
    public List<ReviewListResponse> getRecent10review (){
        Connection conn = getConnection();
        List<ReviewListResponse> list = new ArrayList<>();
        list = dao.getRecent10review(conn);
        close(conn);
        return list;
    }
    public List<Book> getHot5book (){
        Connection conn = getConnection();
        List<Book> list = new ArrayList<>();
        list = dao.getHot5book(conn);
        close(conn);
        return list;
    }
    public List<Book> getTop9book (){
        Connection conn = getConnection();
        List<Book> list = new ArrayList<>();
        list = dao.getTop9book(conn);
        close(conn);
        return list;
    }
    public List<StudyList> getTop9study (){
        Connection conn = getConnection();
        List<StudyList> list = new ArrayList<>();
        list = dao.getTop9study(conn);
        close(conn);
        return list;
    }
    public List<PlaceViewResponse> getRan5place (){
        Connection conn = getConnection();
        List<PlaceViewResponse> list = new ArrayList<>();
        list = dao.getRan5place(conn);
        close(conn);
        return list;
    }
    public List<User> getTop3user (){
        Connection conn = getConnection();
        List<User> list = new ArrayList<>();
        list = dao.getTop3user(conn);
        close(conn);
        return list;
    }

}
