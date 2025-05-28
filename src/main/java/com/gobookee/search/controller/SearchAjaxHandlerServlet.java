package com.gobookee.search.controller;

import com.gobookee.book.model.dto.Book;
import com.gobookee.common.AjaxPageBarTemplate;
import com.gobookee.common.JsonConvertTemplate;
import com.gobookee.place.model.dto.Place;
import com.gobookee.search.model.dto.Search;
import com.gobookee.search.model.dto.SearchReview;
import com.gobookee.search.service.SearchService;
import com.gobookee.study.model.dto.Study;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "searchAjaxHandlerServlet", urlPatterns = "/search/ajax")
public class SearchAjaxHandlerServlet extends HttpServlet {
    private SearchService searchService = SearchService.searchService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Search search = Search.builder()
                .tab(request.getParameter("tab"))
                .keyword(request.getParameter("keyword"))
                .filter(request.getParameter("filter"))
                .cPage(Integer.valueOf(request.getParameter("cPage")))
                .build();

        Object resultList = searchService.search(search);
        HashMap result = new HashMap();

        if ("review".equals(search.getTab())) {
            result.put("list", (List<SearchReview>) resultList);
        } else if ("book".equals(search.getTab())) {
            result.put("list", (List<Book>) resultList);
        } else if ("place".equals(search.getTab())) {
            result.put("list", (List<Place>) resultList);
        } else if ("study".equals(search.getTab())) {
            result.put("list", (List<Study>) resultList);
        }
        int totalData = ((List) resultList).size();

        result.put("tab", search.getTab());
        result.put(
                "pageBar",
                new AjaxPageBarTemplate(search.getCPage(), 5, totalData).makePageBar(request)
        );
        JsonConvertTemplate.toJson(result, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
