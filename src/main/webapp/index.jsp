<%@ page pageEncoding="UTF-8" language="java" %>
<%@include file="WEB-INF/views/common/header.jsp" %>
<%@ page import="com.gobookee.users.model.dto.User"  %>
<%@ page import="java.util.List" %>
<%@ page import="com.gobookee.review.model.dto.ReviewListResponse" %>
<%@ page import="com.gobookee.book.model.dto.Book" %>
<%@ page import="com.gobookee.study.model.dto.StudyList" %>
<%@ page import="com.gobookee.place.model.dto.PlaceViewResponse" %>
<%
User u = (User)session.getAttribute("loginUser");

    List<ReviewListResponse> top3review =  (List<ReviewListResponse>)request.getAttribute("top3review");
    List<ReviewListResponse> recent10review =  (List<ReviewListResponse>)request.getAttribute("recent10review");
    List<Book> hot5book =  (List<Book>)request.getAttribute("hot5book");
    List<Book> top9book =  (List<Book>)request.getAttribute("top9book");
    List<StudyList> top9study =  (List<StudyList>)request.getAttribute("top9study");
    List<PlaceViewResponse> ran5place =  (List<PlaceViewResponse>)request.getAttribute("ran5place");
    List<User> top3user =  (List<User>)request.getAttribute("top3user");

%>
<style>
    .scroll-container {
        display: flex;
        overflow-x: auto;
        gap: 1rem;
        padding-bottom: 1rem;
        scroll-snap-type: x mandatory;
    }

    .scroll-container::-webkit-scrollbar {
        display: none;
    }

    .scroll-item {
        flex: 0 0 auto;
        scroll-snap-align: start;
        width: 250px; /* 카드 하나의 너비 */
    }
</style>
<main>
    <%
    Object loginUser = session.getAttribute("loginUser");

    if (loginUser != null) {}
%>
    <div class="fw-bold d-flex justify-content-between align-items-center" style="padding: 20px 30px 0 20px;">
        <div style="font-size: 20px;color: #50A65D">리뷰 랭킹 TOP3 👑</div>
        <div style="font-size: 14px">더보기<i style="font-size: 14px;padding-left: 5px;" class="bi bi-chevron-right"></i></div>
    </div>
    <%
        int rank = 1;
        for (ReviewListResponse b : top3review) {
    %>

    <div class="book-card" style="box-shadow: none"
         onclick="location.assign('<%=request.getContextPath()%>/review/view?seq=<%=b.getReviewSeq()%>')">
        <div class="row book-card-row">
            <div class="book-card-img col col-4" style="overflow: hidden; padding: 20px ;">
                <img src='<%=b.getBookCover()%>' alt='book cover' >
            </div>
            <div class="book-card-content col col-7">
                <div class="d-flex flex-column flex-grow-1">
                    <strong class="mb-1"><%=b.getReviewTitle()%></strong>
                    <small class="text-muted line-clamp mb-1"><%=b.getReviewContents()%></small>
                    <br>
                    <small class="text-muted"><%=b.getBookTitle()%></small>
                </div>
                <div class="position-absolute bottom-0 end-0 me-2 mb-2 d-flex align-items-center gap-3">
                    <div class="d-flex align-items-center gap-1 text-muted">
                        <i class="bi bi-heart" style="font-size: 0.9rem;"></i> <%=b.getRecommendCount()%>
                    </div>
                    <div class="d-flex align-items-center gap-1 text-muted">
                        <i class="bi bi-chat" style="font-size: 0.9rem;"></i> <%=b.getCommentsCount()%>
                    </div>
                </div>
            </div>

        </div>
    </div>
    <% rank++;
    } %>

    <h5 class="mb-3 fw-bold">따끈따끈한 리뷰! 🔥</h5>
    <div class="scroll-container mb-5">
    <% for (ReviewListResponse b : recent10review) { %>
    <div class="card h-100 review-card scroll-item" onclick="location.assign('<%=request.getContextPath()%>/review/view?seq=<%=b.getReviewSeq() %>')">
        <div class="card-img-wrapper mb-2 d-flex justify-content-center">
            <img src="<%=b.getBookCover() %>" class="card-img-top" alt="book1"
                 style="width: 100px; height: 130px;">
        </div>
        <div class="card-body">
            <h6 class="card-title"><%=b.getReviewTitle() %></h6>
            <p class="card-text small line-clamp"><%=b.getReviewContents()%></p>
        </div>
        <div class="card-footer bg-white border-top-0">
            <small class="review-meta"><i class="bi bi-heart" style="font-size: 0.9rem;"></i>
                <%=b.getRecommendCount()%> | <i class="bi bi-chat"></i> <%=b.getCommentsCount()%>
            </small>
        </div>
    </div>
    <% } %>
    </div>

    <h5 class="mb-3 fw-bold">리뷰가 많은 도서 랭킹 📝</h5>
    <div class="scroll-container mb-5">
        <% for (Book b : top9book) { %>
        <div class="card h-100 review-card scroll-item"
             onclick="location.assign('<%=request.getContextPath()%>/books/bookdetail?bookSeq=<%=b.getBookSeq()%>')">
            <div class="card-img-wrapper mb-2 d-flex justify-content-center">
                <img src="<%=b.getBookCover() %>" class="card-img-top" alt="book1"
                     style="width: 100px; height: 130px;">
            </div>
        </div>
        <% } %>
    </div>
    <div class="form-container" style="padding: 70px 0">
        <%for(Book b : top9book){%>
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
                                List aut = List.of(b.getBookAuthor().split(", "));
                                for(Object a : aut){
                            %>
                            <span><%=a.toString().split(" ")[0]%></span>
                            <%}%>
                        </div>
                        <div><%=b.getBookPublisher()%> | <%=b.getBookPubdate()%></div>
                        <div class="book-card-review">리뷰 <span><%=b.getReviewCount()%>개</span> <i class="bi bi-star-fill"> </i><%=Math.ceil(b.getReviewRateAvg()*100)/100%></div>
                    </div>
                </div>
            </div>
        </a>
        <%}%>
    </div>
    <br>
    top9study<br>
    ran5place<br>
    top3user<br>

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
</script>
<%@include file="WEB-INF/views/common/footer.jsp" %>