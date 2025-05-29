<%@ page import="com.gobookee.book.model.dto.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.gobookee.book.model.dto.BookCategory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<script>
<%--    헤더 변경해주기--%>
$("header").html(
    `<div class="container d-flex justify-content-between align-items-center text-center small">
        <a class="col-1" style="color:black" href="<%=request.getContextPath()%>/books/booklist">
            <i class="bi bi-x fs-1"></i>
        </a>
    </div>`
)

</script>
<% List<BookCategory> categories = (List<BookCategory>) request.getAttribute("bookCategory"); %>
<main class="flex-fill">

    <div class="form-container">
        <div class="book-detail">
            <form action="<%=request.getContextPath()%>/books/searchaladinrequest" onsubmit="">
            <div class="book-detail-content">
                <div class="book-detail-sub-content d-flex justify-content-between align-items-center">
                    <div class="fw-bold" >검색어</div>
                </div>
                <div>
                    <div class="search-query-type">
                        <select id="order-by" name="QueryType">
                            <option value="Keyword">제목+저자</option>
                            <option value="Title">제목검색</option>
                            <option value="Author">저자검색</option>
                            <option value="Publisher">출판사검색</option>
                        </select>
                    </div>
                    <div class="book-search-input">
                        <input type="text" name="Query">
                    </div>
                </div>
                <div class="book-detail-sub-content d-flex justify-content-between align-items-center">
                    <div class="fw-bold" >카테고리</div>
                </div>
                <div class="category-selects">
                    <div>
                        <select id="dept1" name="dept1">
                            <option value="">선택하세요</option>
                            <% for (BookCategory category : categories) {%>
                            <option value="<%=category.getBcCid()%>"><%=category.getBcCidName()%></option>
                            <%}%>
                        </select>
                    </div>
                    <div class="hide">
                        <select id="dept2" name="dept2">

                        </select>
                    </div>
                    <div>
                        <select id="dept3" name="dept3">

                        </select>
                    </div>
                    <div>
                        <select id="dept4" name="dept4">

                        </select>
                    </div>
                    <div>
                        <select id="dept5" name="dept5">

                        </select>
                    </div>
                </div>
                <button type="submit">검색!</button>
            </div>
            </form>
            <div class="book-detail-row">

                <div class="book-detail-content">
                    <div class="book-detail-title"></div>
                    <div class="book-detail-info">

                        <span class="author"></span>
                        <span></span>
                    </div>
                    <div class="d-flex justify-content-between align-items-center">
                        <div class="book-detail-info">
                            <div></div>
                            <div><i class="bi bi-star-fill"> </i></div>
                        </div>
                        <div class="book-detail-icon d-flex justify-content-between align-items-center">
                            <div>
                                <i class="bi bi-bookmark"></i>
                            </div>
                            <div>
                                <i class="bi bi-share-fill"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <hr>

                <div class="book-detail-sub-content">
                    <div>도서정보</div>
                </div>
                <hr style="height: 1px">
                <div class="book-detail-sub-content">
                    <div class="fw-bold">저자</div>
                    <div class="book-detail-desc d-flex align-items-center"><i class="bi bi-person-circle fs-4" style="margin-right: 10px"></i></div>
                </div>
                <div class="book-detail-sub-content">
                    <div class="fw-bold">책 정보</div>
                    <div class="book-detail-desc">
                        <div>
                            <span style="margin-right: 10px; font-size: clamp(12px, 2vw, 14px)">분야</span><span style="font-size: clamp(14px, 2vw, 16px)"></span>
                        </div>
                        <div>
                            <span style="margin-right: 10px; font-size: clamp(12px, 2vw, 14px)">출판사</span><span style="font-size: clamp(14px, 2vw, 16px)"></span>
                        </div>
                        <div id="review-move-focus">
                            <span style="font-size: clamp(14px, 2vw, 16px)"></span>
                        </div>
                    </div>
                </div>
                <hr>

            </div>
        </div>

    </div>
</main>
<script>
    $("#dept1").on("change", function(e){
        const cid = $(e.target).val();
        getCategory(cid,2)
    });
    $("#dept2").on("change", function(e){
        const cid = $(e.target).val();
        getCategory(cid,3)
    });
    $("#dept4").on("change", function(e){
        const cid = $(e.target).val();
        getCategory(cid,4)
    });
    $("#dept5").on("change", function(e){
        const cid = $(e.target).val();
        getCategory(cid,5)
    });

    const getCategory=(cid,level)=>{

        $.ajax({
            url:"<%=request.getContextPath()%>/books/category/ajaxgetcategory",
            type : "GET",
            data : {cid: cid, level : level},
            success:(response)=>{
                $("#dept"+level).html("");
                const category = response;
                let html = "";
                category.forEach(c => {
                    html += '<option value = "'+c.bcCid+'" >'+c.bcCidName+'</option>';
                });
                $("#dept"+level).append(html);
            }

        })
    }
</script>
<%@include file="/WEB-INF/views/common/footer.jsp" %>

