<%@ page import="com.gobookee.book.model.dto.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<%
    List<Book> bookList = (List<Book>) request.getAttribute("bookList");
    StringBuffer pageBar = (StringBuffer) request.getAttribute("pageBar");

%>
<main class="flex-fill">

    <div class="form-container">
        <%for(Book b : bookList){%>
        <a href="<%=request.getContextPath()%>/books/bookdetail?bookSeq=<%=b.getBookSeq()%>">
        <div class="p-4 book-card">
            <div class="row book-card-row">
                <div class="book-card-img col col-5">
                    <img src="<%=b.getBookCover().replace("coversum","cover500")%>">
                    <i class="bi bi-bookmark-fill"></i>
                </div>
                <div class="book-card-content col col-7" >
                    <div class="book-card-title"><%=b.getBookTitle()%></div>
                    <div class="book-card-desc"><%=b.getBookDescription()%></div>
                    <div>
                        <%
                            List aa = new ArrayList();
                            List aut = List.of(b.getBookAuthor().split(", "));
                            for(Object a : aut){
                        %>
                        <span><%=a.toString().split(" ")[0]%></span>
                        <%}%>
                    </div>
                    <div><%=b.getBookPublisher()%> | <%=b.getBookPubdate()%></div>
                    <div class="book-card-review">리뷰 <span>10개</span> <i class="bi bi-star-fill"> </i>4.0</div>
                </div>
            </div>
        </div>
        </a>
        <%}%>
        <div>
            <%=pageBar%>
        </div>
    </div>
</main>
<script src="https://unpkg.com/fast-average-color/dist/index.browser.min.js"></script>
<script>
    const fac = new FastAverageColor();

    document.querySelectorAll('.book-card-img > img').forEach(img => {
        // CORS 허용 안 되는 경우 자동 처리 시도
        img.crossOrigin = 'anonymous';

        if (img.complete) {
            applyAvgColor(img);
        } else {
            img.onload = () => applyAvgColor(img);
        }
    });

    function applyAvgColor(img) {
        fac.getColorAsync(img)
            .then(color => {
                img.parentElement.style.backgroundColor = color.hex;
            })
            .catch(err => {
                console.warn('색상 추출 실패:', err);
            });
    }

    $('header').append("<div class='book-card-head'>책리스트</div>");
</script>
<%@include file="/WEB-INF/views/common/footer.jsp" %>

