<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page
        import="java.sql.Timestamp,java.util.List,com.gobookee.review.model.dto.*,com.gobookee.common.DateTimeFormatUtil" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%
    List<ReviewListResponse> reviews = (List<ReviewListResponse>) request.getAttribute("list");
%>
<style>
    body {
        background-color: #f8f9fa;
        font-size: 1.15rem;
    }

    .nav-tabs .nav-link.active {
        color: #198754;
        border-bottom: 2px solid #198754;
    }

    .review-card {
        cursor: pointer;
    }

    .review-card img {
        border-radius: 10px;
    }

    .review-meta {
        font-size: 0.85rem;
        color: #6c757d;
    }

    .sort-pill-select {
        width: auto;
        border: none;
        border-radius: 999px;
        padding: 0.375rem 2.5rem 0.375rem 1.25rem; /* 오른쪽 padding을 넉넉히 */
        background-color: #6fcf97;
        color: white;
        font-weight: 500;
        appearance: none;
        background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='white' viewBox='0 0 16 16'%3E%3Cpath d='M1.5 5.5l6 6 6-6'/%3E%3C/svg%3E");
        background-repeat: no-repeat;
        background-position: right 1rem center; /* 화살표를 오른쪽으로 이동 */
        background-size: 12px;
        margin-bottom: 20px;
    }

    .sort-pill-select:focus {
        outline: none;
        box-shadow: 0 0 0 0.2rem rgba(111, 207, 151, 0.4);
    }

    .card-text {
        display: -webkit-box;
        -webkit-line-clamp: 3; /* 최대 줄 수 */
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .line-clamp {
        display: -webkit-box;
        -webkit-line-clamp: 1; /* 보이는 줄 수 조절 */
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
        /* 추가: 긴 단어 줄바꿈 처리 */
        word-break: break-word;
        overflow-wrap: break-word;
        line-height: 1.4;
        max-height: calc(1.4em * 1); /* line-height × 줄 수 */
    }

    .review-meta {
        font-size: 0.9rem;
        color: #6c757d;
    }

    .review-meta i.bi {
        font-size: 1rem;
        vertical-align: middle;
    }
</style>
<main>
    <div class="container py-4">
        <h5 class="mb-3 fw-bold">이번 달 베스트 리뷰</h5>

        <% if (reviews != null && !reviews.isEmpty()) { %>
        <div class="row row-cols-4 row-cols-md-4 g-4 mb-5">
            <% for (ReviewListResponse b : reviews) { %>
            <div class="col">
                <div class="card h-100 review-card"
                     onclick="location.assign('<%=request.getContextPath()%>/review/view?seq=<%=b.getReviewSeq() %>')">
                    <div class="card-img-wrapper mb-2 d-flex justify-content-center">
                        <img src="<%=b.getBookCover() %>" class="card-img-top" alt="book1"
                             style="width: 100px; height: 130px;">
                    </div>
                    <div class="card-body">
                        <h6 class="card-title"><%=b.getReviewTitle() %>
                        </h6>
                        <p class="card-text small line-clamp"><%=b.getReviewContents()%>
                        </p>
                    </div>
                    <div class="card-footer bg-white border-top-0">
                        <small class="review-meta">
                            <i class="bi bi-hand-thumbs-up me-1"></i> <%=b.getRecommendCount()%>
                            <i class="bi bi-chat"></i> <%=b.getCommentsCount()%>
                        </small>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
        <% } else { %>
        <div>
            <p>조회된 데이터가 없습니다.</p>
        </div>
        <% } %>


        <div class="form-container">
            <select id="sortSelect" class="sort-pill-select">
                <option value="latest">최신순</option>
                <option value="recommend">추천순</option>
            </select>
            <div id="reviewContainer" class="list-group"></div>

            <div id="pageBar"></div>
        </div>
    </div>
</main>
<script>
    let currentSort = "latest"; // 현재 정렬 기준 기억

    //정렬 드롭다운 이벤트
    $(document).ready(function () {
        loadReviews("latest");

        $("#sortSelect").on("change", function () {
            currentSort = $(this).val();
            loadReviews(currentSort, 1);
        });

    });

    //페이지바 클릭 이벤트
    $(document).on("click", "#pageBar a.go-page-link", function (e) {
        e.preventDefault();
        const page = $(this).data("page");
        if (page) {
            loadReviews(currentSort, page);
        }
    });

    function loadReviews(sortType, cPage = 1) {
        $.ajax({
            url: "<%=request.getContextPath()%>/review/sortlist",
            type: "GET",
            data: {sort: sortType, cPage: cPage},
            dataType: "json",
            success: function (response) {
                const container = $("#reviewContainer");
                const pageBarDiv = $("#pageBar");

                container.empty();
                pageBarDiv.empty();
                console.log(Array.isArray(response.reviews));
                const reviews = response.reviews;
                const pageBar = response.pageBar;
                console.log(reviews);
                console.log(pageBar);
                if (!reviews || reviews.length === 0) {
                    container.append("<div>리뷰가 없습니다.</div>");
                } else {
                    reviews.forEach(function (b) {

                        const itemHtml = `
                    	<div class="list-group-item list-group-item-action d-flex gap-3 py-3 align-items-start position-relative"
                    	     onclick="location.assign('<%=request.getContextPath()%>/review/view?seq=\${b.reviewSeq}')">

                    	
                    	  <div style="flex-shrink: 0; width: 120px; height: 150px;">
                    	    <img src='\${b.bookCover}' alt='book cover' class='rounded w-100 h-100 object-fit-cover'>
                    	  </div>

                    	
                    	  <div class="d-flex flex-column flex-grow-1">
                    	    <strong class="mb-1">\${b.reviewTitle}</strong>
                    	    <small class="text-muted line-clamp mb-1">\${b.reviewContents}</small>
                   			<hr>
                    	    <small class="text-muted line-clamp">\${b.bookTitle}</small>
                    	  </div>
						
                    	 
                    	  <div class="position-absolute bottom-0 end-0 me-2 mb-2 d-flex align-items-center gap-3">
                    	    <div class="d-flex align-items-center gap-1 text-muted">
                    	    <i class="bi bi-hand-thumbs-up me-1" style="font-size: 0.9rem;"></i> \${b.recommendCount}
                    	    </div>
                    	    <div class="d-flex align-items-center gap-1 text-muted">
                    	      <i class="bi bi-chat" style="font-size: 0.9rem;"></i> \${b.commentsCount}
                    	    </div>
                    	  </div>
                    	</div>`;

                        console.log(itemHtml);
                        container.append(itemHtml);
                    });
                }

                pageBarDiv.html(pageBar);
            },
            error: function () {
                alert("리뷰 불러오는 중 오류.");
            }
        });
    }

    document.addEventListener("DOMContentLoaded", function () {
        const fabToggle = document.getElementById("fabToggle");
        const fabMenu = document.getElementById("fabMenu");
        let isOpen = false;

        fabToggle.addEventListener("click", function () {
            isOpen = !isOpen;
            fabMenu.style.display = isOpen ? "flex" : "none";
            fabToggle.innerHTML = isOpen ? '<i class="bi bi-x-lg"></i>' : '<i class="bi bi-plus-lg"></i>';
        });
    });
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>