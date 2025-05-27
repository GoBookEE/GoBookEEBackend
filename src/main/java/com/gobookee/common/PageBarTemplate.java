package com.gobookee.common;

import lombok.Builder;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

/**
 * 페이지 바 구현 템플릿
 * 사용법 : 페이지 바 로직으로 HTML 태그를 문자열로 만들어서 반환
 */
@Getter
public class PageBarTemplate {
    private int cPage;
    private int numPerPage;
    private int totalData;
    private int pageBarSize; //페이지 바 사이즈는 5로 고정
    private int pageNo;
    private int totalPage;
    private int pageEnd;

    @Builder
    public PageBarTemplate(int cPage, int numPerPage, int totalData) {
        this.cPage = cPage;
        this.numPerPage = numPerPage;
        this.totalData = totalData;
        this.pageBarSize = 5;
        this.pageNo = ((cPage - 1) / pageBarSize) * pageBarSize + 1;
        this.totalPage = (int) Math.ceil((double) totalData / numPerPage);
        this.pageEnd = pageNo + pageBarSize - 1;
    }

    public StringBuffer makePageBar(HttpServletRequest request) {
        StringBuffer pageBar = new StringBuffer("<ul class='go-pagination justify-content-center'>");

        if (pageNo == 1) {
            pageBar.append("<li class = 'go-page-item disabled'>")
                    .append("<a class = 'go-page-link' href='#'><i class='bi bi-arrow-left-short'></i></a>")
                    .append("</li>");
        } else {
            pageBar.append("<li class = 'go-page-item'>")
                    .append("<a class = 'go-page-link' href='" + request.getRequestURI() + "?cPage=" + (pageNo - 1) + "'><i class='bi bi-arrow-left-short'></i></a>")
                    .append("</li>");
        }

        while (!(pageNo > pageEnd || pageNo > totalPage)) {
            if (pageNo == cPage) {
                pageBar.append("<li class = 'go-page-item disabled'>")
                        .append("<a class = 'go-page-link active' href='#'>" + pageNo + "</a>")
                        .append("</li>");
            } else {
                pageBar.append("<li class = 'go-page-item'>")
                        .append("<a class = 'go-page-link' href='" + request.getRequestURI() + "?cPage=" + (pageNo) + "'>" + pageNo + "</a>")
                        .append("</li>");
            }
            pageNo++;
        }

        if (pageNo > totalPage) {
            pageBar.append("<li class = 'go-page-item disabled'>")
                    .append("<a class = 'go-page-link' href='#'><i class='bi bi-arrow-right-short'></i></a>")
                    .append("</li>");
        } else {
            pageBar.append("<li class = 'go-page-item'>")
                    .append("<a class = 'go-page-link' href='" + request.getRequestURI() + "?cPage=" + (pageNo) + "'><i class='bi bi-arrow-right-short'></i></a>")
                    .append("</li>");
        }
        pageBar.append("</ul>");

        return pageBar;
    }
}
