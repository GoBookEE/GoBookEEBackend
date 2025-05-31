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
		<h5 class="fw-bold mb-0">내 스터디</h5>
	</div>

	<div class="tab-btn-wrapper d-flex search-tab">
		<button class="tab-btn active" data-tab="applied">신청한 스터디</button>
		<button class="tab-btn" data-tab="created">내가 만든 스터디</button>
	</div>

	<div class="dropdown my-3">
		<select id="statusFilter" class="form-select mt-3"
			style="width: 200px;">
			<option value="upcoming">진행 예정</option>
			<option value="completed">진행 완료</option>
		</select>
	</div>

	<div id="studyListContainer" class="mt-4"></div>
	<div id="pageBar" class="mt-3 text-center"></div>

</main>

<script>
let currentTab = "applied";      // "applied" 또는 "created"
let currentStatus = "upcoming";  // "upcoming" 또는 "completed"

$(document).ready(function () {
  loadMyStudies(); // 초기 로딩

  // 탭 클릭
  $(".tab-btn").on("click", function () {
    $(".tab-btn").removeClass("active");
    $(this).addClass("active");
    currentTab = $(this).data("tab");
    loadMyStudies(); // 탭 바뀌면 페이지 1로 리셋
  });

  // 드롭다운 변경
  $("#statusFilter").on("change", function () {
    currentStatus = $(this).val();
    loadMyStudies(); // 상태 바뀌면 페이지 1로 리셋
  });

  // 페이지바 클릭 이벤트
  $(document).on("click", "#pageBar a.go-page-link", function (e) {
    e.preventDefault();
    const page = $(this).data("page");
    if (page) {
      loadMyStudies(page);
    }
  });
});

function loadMyStudies(cPage = 1) {
  $.ajax({
    url: "<%=request.getContextPath()%>/mystudy/ajax",
    method: "GET",
    data: {
      tab: currentTab,
      status: currentStatus,
      cPage: cPage
    },
    dataType: "json",
    success: function (res) {
      const container = $("#studyListContainer").empty();
      const pageBar = $("#pageBar").empty();

      if (!res.list || res.list.length === 0) {
        container.append("<p class='text-muted text-center'>스터디가 없습니다.</p>");
        return;
      }

      res.list.forEach(b => {
        const itemHtml = `
        <div class="p-4 book-card" onclick="location.assign('<%=request.getContextPath()%>/study/view?seq=\${b.studySeq}')">
          <div class="row book-card-row">
            <div class="book-card-img col col-5">
              <img src="<%=request.getContextPath()%>/resources/upload/study/\${b.photoRenamedName}"
                   onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png'; this.style.width='100px'; this.style.height='100px';"
                   alt="스터디 이미지">
            </div>
            <div class="book-card-content col col-7">
              <div class="book-card-title">\${b.studyTitle}</div>
              <div class="book-card-desc">\${b.studyDate ? b.studyDate : '미정'}</div>
              <div>
                <i class="bi bi-people-fill"></i>
                <span>\${b.confirmedCount + 1}</span>/<span>\${b.studyMemberLimit}</span>
              </div>
              <div id="studyAddress">\${b.studyAddress ? b.studyAddress : '미정'}</div>
              <span><i class="bi bi-hand-thumbs-up me-1" style="font-size: 0.9rem; padding-left: 1rem;"></i>\${b.likeCount}</span>
              <span><i class="bi bi-hand-thumbs-down me-1" style="font-size: 0.9rem; padding-left: 1rem;"></i>\${b.dislikeCount}</span>
            </div>
          </div>
        </div>`;
        container.append(itemHtml);
      });

      pageBar.html(res.pageBar);
    },
    error: function () {
      alert("스터디 목록 불러오기 실패");
    }
  });
}
</script>



<%@ include file="/WEB-INF/views/common/footer.jsp"%>