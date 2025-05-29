<%@ page import="com.gobookee.common.CommonPathTemplate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>

<style>
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

    .search-result .list-group-item,
    .search-result .card {
        border: 1px solid #dee2e6;
        border-radius: 10px;
        padding: 1rem;
        margin-bottom: 1rem;
        background-color: #fff;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        transition: transform 0.2s ease, box-shadow 0.2s ease;
    }

    .search-result .list-group-item:hover,
    .search-result .card:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    .search-result .card img,
    .search-result .list-group-item img {
        border-radius: 6px;
    }

    .search-result h5,
    .search-result .card-title,
    .search-result strong {
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
        <h5 class="fw-bold mb-0">통합 상세 검색</h5>
    </div>
    <div class="search-tab">
        <button type="button" class="tab-btn active" data-target="review">리뷰 검색</button>
        <button type="button" class="tab-btn" data-target="book">책 검색</button>
        <button type="button" class="tab-btn" data-target="study">스터디 검색</button>
        <button type="button" class="tab-btn" data-target="place">장소 검색</button>
    </div>

    <div class="dropdown-filter d-flex align-items-center gap-2">
        <select id="filterType" class="form-select" style="width: 150px;">
            <option value="all">전체</option>
            <option value="title">제목</option>
            <option value="content">내용</option>
        </select>
        <div class="flex-grow-1">
            <input type="text" class="form-control" id="searchKeyword" placeholder="검색어를 입력하세요">
        </div>
        <button class="btn btn-outline-success" onclick="performSearch()"
                style="height: 38px; width: 38px; padding: 0;">
            <i class="bi bi-search" style="font-size: 16px;"></i>
        </button>
    </div>

    <div class="form-container">
        <div id="searchResults" class="search-result"></div>
        <div id="pageBar"></div>
    </div>
</main>

<script>
    // 탭 버튼 클릭 이벤트
    document.querySelectorAll(".tab-btn").forEach(btn => {
        btn.addEventListener("click", function () {
            // 탭 활성화 처리
            document.querySelectorAll(".tab-btn").forEach(b => b.classList.remove("active"));
            this.classList.add("active");

            // 탭 변경
            currentTab = this.getAttribute("data-target");

            // 필터 옵션 업데이트
            updateFilterOptions(currentTab);

            // ✅ 검색 결과와 페이지 바 초기화
            $("#searchResults").empty();
            $("#pageBar").empty();
            $("#searchKeyword").val("");
        });
    });


    $(document).on("click", "#pageBar a.go-page-link", function (e) {
        e.preventDefault();
        const page = $(this).data("page");
        if (page) {
            performSearch(page);
        }
    });

    let currentTab = "review";

    const filterType = document.getElementById("filterType");

    const filterOptions = {
        review: [
            {value: "title", label: "제목"},
            {value: "writer", label: "작성자"},
            {value: "content", label: "내용"},
            {value: "bookTitle", label: "책 제목"}
        ],
        book: [
            {value: "bookTitle", label: "책 제목"},
            {value: "publisher", label: "출판사"},
            {value: "author", label: "저자"},
            {value: "titleContent", label: "제목+내용"}
        ],
        study: [
            {value: "studyTitle", label: "스터디 제목"},
            {value: "studyPlace", label: "스터디 장소"},
            {value: "nickname", label: "작성자 닉네임"}
        ],
        place: [
            {value: "placeTitle", label: "장소 제목"},
            {value: "location", label: "위치"}
        ]
    };

    function updateFilterOptions(tabKey) {
        filterType.innerHTML = ""; // 기존 옵션 제거
        filterOptions[tabKey].forEach(opt => {
            const option = document.createElement("option");
            option.value = opt.value;
            option.textContent = opt.label;
            filterType.appendChild(option);
        });
    }

    // 탭 버튼 클릭 이벤트
    document.querySelectorAll(".tab-btn").forEach(btn => {
        btn.addEventListener("click", function () {
            document.querySelectorAll(".tab-btn").forEach(b => b.classList.remove("active"));
            this.classList.add("active");
            currentTab = this.getAttribute("data-target");
            updateFilterOptions(currentTab); // 필터 옵션 업데이트
        });
    });

    // 페이지 로딩 시 기본 필터 로드
    window.addEventListener("DOMContentLoaded", function () {
        updateFilterOptions(currentTab);
    });

    function performSearch(cPage = 1) {
        const keyword = $("#searchKeyword").val().trim();
        const filter = $("#filterType").val();

        if (!keyword) {
            alert("검색어를 입력해주세요.");
            return;
        }

        $.ajax({
            url: "<%=request.getContextPath()%>/search/ajax",
            type: "GET",
            data: {
                tab: currentTab,
                filter: filter,
                keyword: keyword,
                cPage: cPage
            },
            dataType: "json",
            success: function (res) {
                const container = $("#searchResults").empty();
                const pageBar = $("#pageBar").empty();

                if (res.list.length === 0) {
                    container.append(`<div class="text-muted text-center">검색 결과가 없습니다.</div>`);
                    return;
                }

                res.list.forEach(item => {
                    let html = "";
                    if (currentTab === "review") {
                        html = `
                <div class="list-group-item list-group-item-action d-flex gap-3 py-3 align-items-start position-relative"
                    	     onclick="location.assign('<%=request.getContextPath()%>/review/view?seq=\${item.reviewSeq}')">


                    	  <div style="flex-shrink: 0; width: 120px; height: 150px;">
                    	    <img src='\${item.bookCover}' alt='book cover' class='rounded w-100 h-100 object-fit-cover'>
                    	  </div>


                    	  <div class="d-flex flex-column flex-grow-1">
                    	    <strong class="mb-1">\${item.reviewTitle}</strong>
                    	    <small class="text-muted line-clamp mb-1">\${item.reviewContents}</small>
                    	    <strong class="mb-1">작성자:\${item.userNickname}</strong>
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
                    } else if (currentTab === "book") {
                        html = `
                        <div class="card mb-3 shadow-sm border-0" style="border-radius: 20px; cursor: pointer;"
                             onclick="location.assign('<%=request.getContextPath()%>/books/bookdetail?bookSeq=\${item.bookSeq}')">
                          <div class="row g-0">
                            <div class="col-4 d-flex align-items-center justify-content-center p-3">
                              <img src="\${item.bookCover}"
                                   class="img-fluid rounded shadow-sm" alt="book cover"
                                   style="max-height: 180px; object-fit: contain;"
                                   onerror="this.src='<%=request.getContextPath()%>/resources/images/default.jpg'">
                            </div>
                            <div class="col-8 p-3">
                              <div class="d-flex flex-column h-100 justify-content-between">
                                <div>
                                  <h6 class="fw-bold mb-1">\${item.bookTitle}</h6>
                                  <p class="text-muted small line-clamp mb-2">\${item.bookDescription}</p>
                                  <div class="text-secondary small mb-1">\${item.bookAuthor}</div>
                                  <div class="text-secondary small mb-1">\${item.bookPublisher} | \${item.bookPubDate}</div>
                                </div>
                                <div class="mt-2 d-flex align-items-center gap-2 small">
                                  <span class="text-muted">리뷰 \${item.reviewCount}개</span>
                                  <span class="text-success d-flex align-items-center">
                                    <i class="bi bi-star-fill me-1 text-success" style="font-size: 0.9rem;"></i> \${item.reviewRateAvg}
                                  </span>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>`;
                    } else if (currentTab === "study") {
                        html = `
                            <div class="card mb-3 shadow-sm border-0" style="border-radius: 20px; cursor: pointer;"
                             onclick="location.assign('<%=request.getContextPath()%>/study/view?studySeq=\${item.studySeq}')">
                            <div class="row g-0">
                            <div class="col-4 d-flex align-items-center justify-content-center p-3">
                              <img src="<%=request.getContextPath()%>/upload/photo/\${item.photoName}"
                                   class="img-fluid rounded shadow-sm" alt="스터디 썸네일"
                                   style="max-height: 160px; object-fit: cover;"
                                   onerror="this.src='<%=request.getContextPath()%>/resources/images/default.jpg'">
                            </div>
                            <div class="col-8 p-3">
                              <div class="d-flex flex-column h-100 justify-content-between">
                                <div>
                                  <h6 class="fw-bold mb-1">\${item.studyTitle}</h6>
                                  <div class="text-secondary small mb-1">
                                    <i class="bi bi-geo-alt-fill me-1"></i>\${item.studyAddress ? item.studyAddress : '미정'}
                                  </div>
                                  <div class="text-secondary small mb-1">
                                    <i class="bi bi-calendar-check me-1"></i>\${item.studyDate ? item.studyDate : '미정'}
                                  </div>
                                  <div class="text-secondary small mb-1">
                                    <i class="bi bi-people-fill me-1"></i>\${item.confirmCount} / \${item.studyMemberLimit}명
                                  </div>
                                  <div class="text-secondary small mb-1">
                                    <i class="bi bi-tag me-1"></i>작성자: \${item.nickname}
                                  </div>
                                </div>
                                <div class="d-flex gap-3 text-muted small mt-2">
                                  <span><i class="bi bi-hand-thumbs-up me-1"></i>\${item.likeCount}</span>
                                  <span><i class="bi bi-hand-thumbs-down me-1"></i>\${item.dislikeCount}</span>
                                </div>
                              </div>
                            </div>
                            </div>
                            </div>`;
                    } else if (currentTab === "place") {
                        html = `
                <div class="card mb-3 d-flex flex-row p-2">
                    <img src="<%=request.getContextPath()%><%=CommonPathTemplate.BASIC_UPLOAD_PATH%>place/\${item.placeThumbnail}" class="rounded me-3" style="width: 100px; height: 100px; object-fit: cover;"
                    onerror="this.src='<%=request.getContextPath()%>/resources/images/default.jpg'">
                    <div class="flex-grow-1">
                        <h5 class="mb-1">\${item.placeTitle}</h5>
                        <p class="mb-1">\${item.placeAddress}</p>
                        <p class="text-muted small mb-1">\${item.placeContents}</p>
                        <div>
                            <span class="text-success"><i class="bi bi-hand-thumbs-up"></i> \${item.placeRecCount}</span>
                            <span class="text-danger ms-2"><i class="bi bi-hand-thumbs-down"></i> \${item.placeNonRecCount}</span>
                        </div>
                    </div>
                </div>`;
                    }
                    container.append(html);
                });
                console.log(res.pageBar);
                pageBar.html(res.pageBar);
            },
            error: function () {
                alert("검색 실패");
            }
        });
    }
</script>
<style>
    .fab-container {
        position: fixed;
        bottom: 80px; /* 푸터 위로 띄우기 */
        right: 24px;
        z-index: 999;
        display: flex;
        flex-direction: column;
        align-items: flex-end;
    }

    .fab-main {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        background-color: #198754;
        color: white;
        border: none;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
        font-size: 24px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: transform 0.3s ease;
    }

    .fab-menu {
        display: none;
        flex-direction: column;
        margin-bottom: 10px;
    }

    .fab-item {
        background-color: #198754;
        color: white;
        text-decoration: none;
        padding: 8px 16px;
        border-radius: 16px;
        margin-bottom: 10px;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
        font-size: 14px;
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .fab-item:hover {
        background-color: #157347;
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
        -webkit-line-clamp: 2; /* 보이는 줄 수 조절 */
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
        /* 추가: 긴 단어 줄바꿈 처리 */
        word-break: break-word;
        overflow-wrap: break-word;
        line-height: 1.4;
        max-height: calc(1.4em * 2); /* line-height × 줄 수 */
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
<%@ include file="/WEB-INF/views/common/footer.jsp" %>