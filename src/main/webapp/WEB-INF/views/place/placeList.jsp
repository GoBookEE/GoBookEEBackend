<%@ page import="com.gobookee.common.CommonPathTemplate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>

<style>

    #pageBar {
        padding-bottom: 80px;
    }

    .form-container {
        padding-top: 80px; /* header 높이에 맞게 조정 */
    }

    body {
        background-color: #f8f9fa;
    }

    .place-card {
        border-radius: 16px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        margin-bottom: 20px;
        overflow: hidden;
        background: #fff;
        padding: 15px;
        display: flex;
        gap: 15px;
    }

    .place-image {
        width: 100px;
        height: 100px;
        object-fit: cover;
        border-radius: 12px;
        flex-shrink: 0;
    }

    .place-title {
        font-weight: bold;
        margin-bottom: 4px;
    }

    .place-address {
        color: #28a745;
        font-size: 0.9rem;
        margin-bottom: 4px;
    }

    .place-content {
        font-size: 0.9rem;
        color: #6c757d;
        margin-bottom: 8px;
    }

    .place-icons {
        font-size: 0.85rem;
        color: #6c757d;
        display: flex;
        gap: 15px;
    }

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

    .review-meta i.bi {
        font-size: 1rem;
        vertical-align: middle;
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

</style>

<main class="container py-3">
    <div class="form-container">
        <select id="sortSelect" class="form-select sort-pill-select">
            <option value="latest">최신순</option>
            <option value="recommend">추천순</option>
        </select>
        <div id="placeListContainer"></div>

        <div id="pageBar"></div>
        <!-- Floating Action Button -->
        <div class="fab-container">
            <button class="fab-main" id="fabToggle">
                <i class="bi bi-plus-lg"></i>
            </button>
            <div class="fab-menu" id="fabMenu">
                <a href="<%=request.getContextPath()%>/place/insertpage"
                   class="fab-item"> <i class="bi bi-pencil"></i> 리뷰 쓰기
                </a>
            </div>
        </div>
    </div>
</main>

<script>
    //정렬 드롭다운 이벤트
    $(document).ready(function () {
        loadPlaces("latest");

        $("#sortSelect").on("change", function () {
            currentSort = $(this).val();
            loadPlaces(currentSort, 1);
        });

    });

    function loadPlaces(sortType = "latest", cPage = 1) {
        $.ajax({
            url: "<%=request.getContextPath()%>/place/list",
            type: "GET",
            data: {sort: sortType, cPage: cPage},
            dataType: "json",
            success: function (response) {
                const container = $("#placeListContainer");
                const pageBarDiv = $("#pageBar");

                container.empty();
                pageBarDiv.empty();

                const placeList = response.placeList;
                const pageBar = response.pageBar;

                if (!placeList || placeList.length === 0) {
                    container.append("<div class='text-center text-muted'>등록된 장소가 없습니다.</div>");
                    return;
                }

                placeList.forEach(function (place) {
                    const itemHtml = `
                    <div class="place-card" onclick="location.href='<%=request.getContextPath()%>/place/view?placeSeq=\${place.placeSeq}'">
                        <img src='<%=request.getContextPath()%><%=CommonPathTemplate.BASIC_UPLOAD_PATH%>place/\${place.placeThumbnail}' class="place-image" alt="썸네일">
                        <div class="flex-grow-1">
                            <div class="place-title">\${place.placeTitle}</div>
                            <div class="place-address">\${place.placeAddress}</div>
                            <div class="place-content">\${place.placeContents}</div>
                            <div class="place-icons">
                                <span class="text-success"><i class="bi bi-hand-thumbs-up"></i> \${place.placeRecCount}</span>
                                <span class="text-danger"><i class="bi bi-hand-thumbs-down"></i> \${place.placeNonRecCount}</span>
                            </div>
                        </div>
                    </div>
                `;
                    container.append(itemHtml);
                });

                pageBarDiv.html(pageBar);
            },
            error: function () {
                alert("장소 목록을 불러오는 중 오류가 발생했습니다.");
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