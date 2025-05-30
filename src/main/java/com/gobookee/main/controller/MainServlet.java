package com.gobookee.main.controller;
import com.gobookee.book.model.dto.Book;
import com.gobookee.main.service.MainService;
import com.gobookee.place.model.dto.Place;
import com.gobookee.place.model.dto.PlaceViewResponse;
import com.gobookee.review.model.dto.Review;
import com.gobookee.review.model.dto.ReviewListResponse;
import com.gobookee.study.model.dto.Study;
import com.gobookee.study.model.dto.StudyList;
import com.gobookee.users.model.dto.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/home")
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGet");
        List<ReviewListResponse> top3review = new ArrayList<>();
        top3review = MainService.mainService().getTop3review();
        List<ReviewListResponse> recent10review = new ArrayList<>();
        recent10review = MainService.mainService().getRecent10review();
        List<Book> hot5book = new ArrayList<>();
        hot5book = MainService.mainService().getHot5book();
        List<Book> top9book = new ArrayList<>();
        top9book = MainService.mainService().getTop9book();
        List<StudyList> top9study = new ArrayList<>();
        top9study = MainService.mainService().getTop9study();
        List<PlaceViewResponse> ran5place = new ArrayList<>();
        ran5place = MainService.mainService().getRan5place();
        List<User> top3user = new ArrayList<>();
        top3user = MainService.mainService().getTop3user();

        request.setAttribute("top3review", top3review);
        request.setAttribute("recent10review", recent10review);
        request.setAttribute("hot5book", hot5book);
        request.setAttribute("top9book", top9book);
        request.setAttribute("top9study", top9study);
        request.setAttribute("ran5place", ran5place);
        request.setAttribute("top3user", top3user);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
