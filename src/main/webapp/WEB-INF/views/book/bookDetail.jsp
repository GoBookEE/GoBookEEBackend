<%@ page import="com.gobookee.book.model.dto.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<%
    Book b = (Book) request.getAttribute("book");

%>
<main class="flex-fill">
    <div class="form-container">
        <div class="book-detail">
            <div class="book-detail-img">
                <img src="<%=b.getBookCover().replace("coversum","cover500")%>">
            </div>
            <div class="book-detail-row">
                <div class="book-detail-content">
                    <div class="book-detail-title"><%=b.getBookTitle()%></div>
                    <div class="book-detail-info">
                        <%
                            List aut = List.of(b.getBookAuthor().split(", "));
                            for(Object a : aut){
                        %>
                        <span class="author"><%=a.toString().split(" ")[0]%></span>
                        <%}%>
                        <span>| <%=b.getBookPublisher()%> | <%=b.getBookPubdate()%></span>
                    </div>
                    <div class="d-flex justify-content-between align-items-center">
                        <div class="book-detail-review">
                            <div>리뷰 <span>10개</span></div>
                            <div><i class="bi bi-star-fill"> </i>4.0</div>
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
                    <div class="book-detail-desc d-flex align-items-center"><i class="bi bi-person-circle fs-4" style="margin-right: 10px"></i><%=b.getBookAuthor()%></div>
                </div>
                <div class="book-detail-sub-content">
                    <div class="fw-bold">책 정보</div>
                    <div class="book-detail-desc">
                        <div>
                            <span style="margin-right: 10px; font-size: clamp(12px, 2vw, 14px)">분야</span><span style="font-size: clamp(14px, 2vw, 16px)"><%=b.getBookCategoryName()%></span>
                        </div>
                        <div>
                            <span style="margin-right: 10px; font-size: clamp(12px, 2vw, 14px)">출판사</span><span style="font-size: clamp(14px, 2vw, 16px)"><%=b.getBookPublisher()%></span>
                        </div>
                        <div>
                            <span style="font-size: clamp(14px, 2vw, 16px)"><%=b.getBookDescription()%></span>
                        </div>
                    </div>
                </div>
                <hr>
                <div class="book-detail-sub-content d-flex justify-content-between align-items-center">
                    <div class="fw-bold">리뷰 <span style="color: #50A65D">22</span></div>
                    <div class="sort">
                        최신 순<i class="bi bi-caret-down-fill fs-5" style="margin-left: 10px"></i>
                    </div>
                </div>
            </div>
        </div>

    </div>
</main>
<script src="https://unpkg.com/fast-average-color/dist/index.browser.min.js"></script>
<script>
    const fac = new FastAverageColor();

    document.querySelectorAll('.book-detail-img > img').forEach(img => {
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
</script>
<%@include file="/WEB-INF/views/common/footer.jsp" %>

