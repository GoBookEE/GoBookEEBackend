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
                        <div class="book-detail-info">
                            <div>리뷰 <span><%=b.getReviewCount()%></span></div>
                            <div><i class="bi bi-star-fill"> </i><%=Math.ceil(b.getReviewRateAvg()*100)/100%></div>
                        </div>
                        <div class="book-detail-icon d-flex justify-content-between align-items-center">
                            <%if(b.getWishCount() != 0){%>
                            <div class="wish-mark" id="wish-check">
                                <i class="bi bi-bookmark-fill"></i>
                            </div>
                            <%}else{%>
                            <div class="wish-mark" id="wish-uncheck">
                                <i class="bi bi-bookmark"></i>
                            </div>
                            <%}%>
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
                        <div id="review-move-focus">
                            <span style="font-size: clamp(14px, 2vw, 16px)"><%=b.getBookDescription()%></span>
                        </div>
                    </div>
                </div>
                <hr>
                <div class="book-detail-sub-content d-flex justify-content-between align-items-center">
                    <div class="fw-bold">리뷰 <span style="color: #50A65D"><%=b.getReviewCount()%></span></div>
                    <div class="sort">
                        <select id="order-by" name="orderBy">
                            <option value="CREATD">최신 순</option>
                            <option value="CREATA">오래된 순</option>
                            <option value="RECD">추천 순</option>
                            <option value="DISD">비추천 순</option>
                        </select>
                    </div>
                </div>
                <div class="book-detail-review" >
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

<script>
    const reviewCount = '<%=b.getReviewCount()%>';
    const bookSeq = '<%=b.getBookSeq()%>';
    const userSeq = '<%=loginUser.getUserSeq()%>';
    // 문서 전체에 위임하지 말고, 항상 존재하는 부모에 위임
    jQuery(document).on("click", "a.go-page-link", function (e) {
        e.preventDefault();
        const currentSort = $("#order-by").val();
        const page = $(this).data("page");
        if (page) {
            getSortReview(currentSort, page);
        }
    });
    jQuery(document).ready(function () {
        getSortReview("CREATD",1);
    });
    $("#order-by").on("change",function (e){
        getSortReview($(e.target).val(),1);  // 오타 수정
    })


    const getSortReview = (orderBy,cPage=null) =>{
        $.ajax({
            url: "<%=request.getContextPath()%>/review/ajax/reviewbookseq",
            type : "GET",
            data : {orderBy: orderBy, cPage : cPage, bookSeq:"<%=b.getBookSeq()%>"},
            success:(response)=>{
                const reviews =response.reviews;
                let html = "";
                if(reviewCount > 0){
                reviews.forEach(r => {
                    html += '<div class="review-profile d-flex align-items-center">';
                    if(r.userProfile !== undefined){
                        html += '<img src="'+r.userProfile+'">';
                    }else{
                        html += '<i class="bi bi-person-circle"></i>';
                    }
                    html += '<div class="review-meta">';
                    html += '<div class="review-meta-nick">';
                    html += r.userNickname
                    html += '</div>';
                    html += '<div>';
                    html += formatReviewTime(r.reviewCreateTime);
                    html += '</div>';
                    html += '</div>';
                    html += '</div>';
                    html += '<a href="<%=request.getContextPath()%>/review/view?seq='+r.reviewSeq+'"><div class="review-content">';
                    html += r.reviewContents;
                    html += '</div></a>';
                    html += '<div class="review-stats d-flex justify-content-between">';
                    html += '<div >';
                    html += '<i class="bi bi-hand-thumbs-up"></i><span>'+r.recommendCount+'</span>';
                    html += '<i class="bi bi-hand-thumbs-down"></i><span>'+r.nonRecommendCount+'</span>';
                    html += '</div>';
                    html += '<div >';
                    html += '<i class="bi bi-share-fill"></i>';
                    html += '</div>';
                    html += '</div>';
                    html += '<hr/>';
                });
                const pb = (response.pageBar).replaceAll("href='#'","href='#review-move-focus'");
                html += pb;
                }else {
                    html += '<div class="review-empty text-center">';
                    html += '<div>리뷰가 없습니다. 새로운 리뷰를 등록해 보세요!</div>'
                    html += '<div><button onclick="reviewInsert()">리뷰등록하기</button></div>'
                    html += '</div>'
                }

                $(".book-detail-review").html(html);
            },
            error: (response) => {
                console.error("리뷰 로딩 실패", response);
                $(".book-detail-review").html("<p>리뷰를 불러오지 못했습니다.</p>");
            }

        })
    }

    $(".wish-mark").on("click", function (){
        let mode = '';
        if ($(this).attr('id') === 'wish-check') {
            $(this).attr('id', 'wish-uncheck').html('<i class="bi bi-bookmark"></i>');
            mode = 'uncheck';
        } else if ($(this).attr('id') === 'wish-uncheck') {
            $(this).attr('id', 'wish-check').html('<i class="bi bi-bookmark-fill"></i>');
            mode = 'check';
        }

        $.ajax({
            url: "<%=request.getContextPath()%>/books/wishcheck",
            type: "POST",
            data: {mode:mode, userSeq:userSeq, bookSeq:bookSeq},
            success:(response)=>{
                if(response == 1){
                    if(mode == "uncheck"){
                        alert("찜목록에서 삭제했습니다.");
                    }else if(mode == "check"){
                        alert("찜목록에 추가했습니다.");
                    }
                }else{
                    alert("오류가 발생했습니다. 다시 시도해주세요.");
                }
            },
            error: (response)=>{
                alert("오류가 발생했습니다. 다시 시도해주세요.");
            }

        })
    })


    function formatReviewTime(dateString) {
        const reviewDate = new Date(dateString); // e.g., "May 3, 2025, 10:24:22 AM"
        const now = new Date();
        const diffMs = now - reviewDate;

        const diffMinutes = Math.floor(diffMs / 60000);
        const diffHours = Math.floor(diffMs / 3600000);

        if (diffMinutes < 60) {
            return diffMinutes+'분 전';
        } else if (diffHours < 24) {
            return diffHours+'시간 전';
        } else {
            const yyyy = reviewDate.getFullYear();
            const mm = String(reviewDate.getMonth() + 1).padStart(2, '0');
            const dd = String(reviewDate.getDate()).padStart(2, '0');
            const hh = String(reviewDate.getHours()).padStart(2, '0');
            const min = String(reviewDate.getMinutes()).padStart(2, '0');
            const sec = String(reviewDate.getSeconds()).padStart(2, '0');
            return yyyy+'-'+mm+'-'+dd+' '+hh+':'+min+':'+sec;
        }
    }
</script>
<%@include file="/WEB-INF/views/common/footer.jsp" %>

