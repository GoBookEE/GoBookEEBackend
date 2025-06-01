<%@ page import="com.gobookee.common.CommonPathTemplate"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
	import="com.gobookee.users.model.dto.User,com.gobookee.review.model.dto.*,java.util.List"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%
User loginUser = (User) session.getAttribute("loginUser");
%>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet" />
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
		<h5 class="fw-bold mb-0">내가 쓴 글 목록</h5>
	</div>

	<!-- 탭 -->
	<div class="search-tab">
		<button type="button" class="tab-btn active" data-tab="review">리뷰</button>
		<button type="button" class="tab-btn" data-tab="comment">댓글</button>
		<%if (loginUser != null && "1".equals(loginUser.getUserType().toString())) { %>
		<button type="button" class="tab-btn" data-tab="place">공간</button>
		<%} %>
	</div>

	<!-- AJAX로 바뀌는 부분 -->
	<div id="searchResults" class="search-result"></div>
	<div id="pageBar" class="mt-4"></div>
</main>

<script>
let currentTab = "review";
// 탭 클릭 이벤트
$(".tab-btn").on("click", function () {
   $(".tab-btn").removeClass("active");
   $(this).addClass("active");
   currentTab = $(this).data("tab");
   loadMyPosts(currentTab);});

$(document).ready(function () {
   loadMyPosts(currentTab); // 최초 리뷰 탭 로딩
});

//페이지바 클릭 이벤트
$(document).on("click", "#pageBar a.go-page-link", function (e) {
 e.preventDefault();
 const page = $(this).data("page");
 if (page) {
     loadMyPosts(currentTab, page);
 }
});

function loadMyPosts(tab, cPage = 1) {
    $.ajax({
        url: "<%=request.getContextPath()%>/myboard/ajax",
        method: "GET",
        data: {
            tab: tab,
            cPage: cPage
        },
        dataType: "json",
        success: function (res) {
            const container = $("#searchResults").empty();
            const pageBar = $("#pageBar").empty();
			console.log(res);
            if (!res.list || res.list.length === 0) {
                container.append("<p class='text-muted text-center'>작성한 글이 없습니다.</p>");
                return;
            }

            res.list.forEach(item => {
                let html = "";
                if (tab === "review") {
                    html = `
                    	<div class="list-group-item list-group-item-action d-flex gap-3 py-3 align-items-start position-relative"
               	     onclick="location.assign('<%=request.getContextPath()%>/review/view?seq=\${item.reviewSeq}')">

               	
               	  <div style="flex-shrink: 0; width: 120px; height: 150px;">
               	    <img src='\${item.bookCover}' alt='book cover' class='rounded w-100 h-100 object-fit-cover'>
               	  </div>

               	
               	  <div class="d-flex flex-column flex-grow-1">
               	    <strong class="mb-1">\${item.reviewTitle}</strong>
               	    <small class="text-muted line-clamp mb-1">\${item.reviewContents}</small>
               	    <br>
               	    <small class="text-muted">\${item.bookTitle}</small>
               	  </div>
					
               	 
               	  <div class="position-absolute bottom-0 end-0 me-2 mb-2 d-flex align-items-center gap-3">
               	    <div class="d-flex align-items-center gap-1 text-muted">
               	      <i class="bi bi-heart" style="font-size: 0.9rem;"></i> \${item.recommendCount}
               	    </div>
               	    <div class="d-flex align-items-center gap-1 text-muted">
               	      <i class="bi bi-chat" style="font-size: 0.9rem;"></i> \${item.commentsCount}
               	    </div>
               	  </div>
               	</div>`;
                } else if (tab === "comment") {
                    html = `
                        <div class="card mb-3" onclick="location.assign('<%=request.getContextPath()%>/review/view?seq=\${item.reviewSeq}')">
                            <div class="card-body">
                                <p class="mb-2">\${item.commentsContents}</p>
                                <div class="text-end text-muted small">게시글 제목: \${item.reviewTitle}</div>
                                <div class="text-end text-muted small">\${item.commentsCreateTime}</div>
                            </div>
                        </div>`;
                } else if (tab === "place") {
                    html = `
                    	<div class="card mb-3 d-flex flex-row p-2" onclick="location.assign('<%=request.getContextPath()%>/place/view?placeSeq=\${item.placeSeq}')">
                        <img src="<%=request.getContextPath()%><%=CommonPathTemplate.BASIC_UPLOAD_PATH%>place/\${item.placeThumbnail}" class="rounded me-3" style="width: 100px; height: 100px; object-fit: cover;"
                        onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png'">
                        <div class="flex-grow-1">
                            <h5 class="mb-1">\${item.placeTitle}</h5>
                            <p class="mb-1">\${item.placeAddress}</p>
                            <p class="text-muted small mb-1">\${item.placeContents}</p>
                            <div>
                                <span><i class="bi bi-hand-thumbs-up me-1" style="font-size: 0.9rem;"></i> \${item.placeRecCount}</span>
                                <span><i class="bi bi-hand-thumbs-down me-1" style="font-size: 0.9rem;"></i> \${item.placeNonRecCount}</span>
                            </div>
                        </div>
                    </div>`;
                }
                container.append(html);
            });

            pageBar.html(res.pageBar);
        },
        error: function () {
            alert("내 글 목록 불러오기 실패");
        }
    });
}



</script>


<%@ include file="/WEB-INF/views/common/footer.jsp"%>