<%@ page import="com.gobookee.common.CommonPathTemplate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page
        import="com.gobookee.users.model.dto.User,com.gobookee.review.model.dto.*,java.util.List" %>
<%@ page import="com.gobookee.book.model.dto.Book" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%
    List<Book> bookList = (List<Book>) request.getAttribute("wishBookList");
    StringBuffer pageBar = (StringBuffer) request.getAttribute("pageBar");

%>

<link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
        rel="stylesheet"/>
<link
        href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css"
        rel="stylesheet">
<script
        src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
<style>
    header, footer {
        display: none !important;
    }

    body {
        background-color: #f8f9fa;
    }

    .search-tab {
        border-bottom: 2px solid #dee2e6;
        display: flex;
        justify-content: space-around;
        margin-bottom: 20px;
    }

    .search-tab button {
        flex: 1;
        border: none;
        background: none;
        padding: 12px;
        font-weight: bold;
        border-bottom: 3px solid transparent;
        color: #6c757d;
    }

    .search-tab button.active {
        color: #28a745;
        border-bottom-color: #28a745;
    }

    .dropdown-filter {
        margin-bottom: 15px;
    }

    .search-result {
        margin-top: 20px;
    }

    .search-result .list-group-item, .search-result .card {
        border: 1px solid #dee2e6;
        border-radius: 10px;
        padding: 1rem;
        margin-bottom: 1rem;
        background-color: #fff;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        transition: transform 0.2s ease, box-shadow 0.2s ease;
    }

    .search-result .list-group-item:hover, .search-result .card:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    .search-result .card img, .search-result .list-group-item img {
        border-radius: 6px;
    }

    .search-result h5, .search-result .card-title, .search-result strong {
        font-weight: 600;
        color: #343a40;
    }

    .search-result .text-muted {
        color: #6c757d !important;
    }
</style>


<main class="container py-4" style="max-width: 600px;">
    <div class="d-flex align-items-center mb-4">
        <button class="btn btn-link text-dark text-decoration-none me-2"
                onclick="history.back()">
            <i class="bi bi-arrow-left"></i>
        </button>
        <h5 class="fw-bold mb-0">나의 찜 목록</h5>

    </div>
    <div id="wishListContainer" class="mt-4">
        <%
            if (!bookList.isEmpty()) {
                for (Book b : bookList) {
        %>
        <div onclick='location.assign("<%=request.getContextPath()%>/books/bookdetail?bookSeq=<%=b.getBookSeq()%>")'>
            <div class="p-4 book-card">
                <div class="row book-card-row">
                    <div class="book-card-img col col-5">
                        <img src="<%=b.getBookCover().replace("coversum","cover500")%>">
                    </div>
                    <div class="book-card-content col col-7">
                        <div class="book-card-title"><%=b.getBookTitle()%>
                        </div>
                        <div class="book-card-desc"><%=b.getBookDescription()%>
                        </div>
                        <div>
                            <%
                                List aut = List.of(b.getBookAuthor().split(", "));
                                for (Object a : aut) {
                            %>
                            <span><%=a.toString().split(" ")[0]%></span>
                            <%}%>
                        </div>
                        <div><%=b.getBookPublisher()%> | <%=b.getBookPubdate()%>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%}%>
        <div id="pageBar" class="mt-3 text-center">
            <%=pageBar%>
        </div>
        <%} else {%>
        <div class="mt-3 text-center" style="color:#AFAFAF">
            찜한 목록이 없습니다.
        </div>
        <%}%>
    </div>
</main>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>