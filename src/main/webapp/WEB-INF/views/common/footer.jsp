<%@ page import="com.gobookee.users.model.dto.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    /*햄버거 버튼 css*/
    .side-menu {
        position: fixed;
        top: 0;
        left: 0;
        width: 70%;
        max-width: 300px;
        height: 100%;
        background-color: #fff;
        box-shadow: 2px 0 10px rgba(0, 0, 0, 0.2);
        transform: translateX(-100%);
        transition: transform 0.3s ease-in-out;
        z-index: 1050;
        padding: 1rem;
    }

    .side-menu.open {
        transform: translateX(0);
    }

    .side-menu a {
        display: block;
        padding: 1rem 0;
        border-bottom: 1px solid #ddd;
        color: black;
        font-weight: bold;
        text-decoration: none;
    }

    .side-menu a:hover {
        color: #198754;
    }

    .close-btn {
        background: none;
        border: none;
        color: #198754;
        font-size: 2rem;
        cursor: pointer;
        position: absolute;
        top: 16px;
        right: 16px;
        z-index: 1100;
    }

</style>

<div class="fab-container">
    <button class="fab-main" id="fabToggle">
        <i class="bi bi-plus-lg"></i>
    </button>
    <div class="fab-menu" id="fabMenu">
        <a href="<%=request.getContextPath()%>/review/insertpage"
           class="fab-item"> <i class="bi bi-pencil"></i> 리뷰 쓰기
        </a> <a href="<%=request.getContextPath()%>/books/searchaladin"
                class="fab-item"> <i class="bi bi-book"></i> 책 등록하기
    </a>
        </a>
        <a href="<%=request.getContextPath()%>/place/insertpage" class="fab-item">
            <i class="bi bi-geo-alt-fill"></i> 장소 등록하기
        </a>

    </div>
</div>
</body>
<footer class="py-2">
    <div class="container d-flex justify-content-around text-center small">
        <div class="col-2"
             onclick="location.assign('<%=request.getContextPath()%>/review/listpage')">
            <div>
                <i class="bi bi-chat-text-fill fs-1"></i>
            </div>
            <div>리뷰</div>
        </div>
        <div class="col-2"
             onclick="location.assign('<%=request.getContextPath()%>/study/listpage')">
            <div>
                <i class="bi bi-book-fill fs-1"></i>
            </div>
            <div>스터디</div>
        </div>
        <div class="col-2"
             onclick="location.assign('<%=request.getContextPath()%>')">
            <div>
                <i class="bi bi-house-door-fill fs-1"></i>
            </div>
            <div>홈</div>
        </div>
        <div class="col-2">
            <div>
                <i class="bi bi-cursor-fill fs-1"></i>
            </div>
            <div>채팅</div>
        </div>
        <div class="col-2" id="openCategoryMenu">
            <div>
                <i class="bi bi-list fs-1"></i>
            </div>
            <div>카테고리</div>
        </div>
    </div>
</footer>
<!-- 왼쪽 슬라이드 메뉴 -->
<div id="sideMenu" class="side-menu">
    <button id="closeMenuBtn" class="close-btn">&times;</button>
    <ul style="padding: 0; list-style: none; margin-top: 40px;">
        <li><a href="<%=request.getContextPath()%>/notice/listpage">공지사항</a></li>
        <li><a href="<%=request.getContextPath()%>/books/booklist">책 목록</a></li>
        <li><a href="<%=request.getContextPath()%>/review/listpage">리뷰 목록</a></li>
        <li><a href="<%=request.getContextPath()%>/study/listpage">스터디 목록</a></li>
        <li><a href="<%=request.getContextPath()%>/place/listpage">장소 목록</a></li>
        <li><a href="<%=request.getContextPath()%>/ranking/speed">고북이 속도 랭킹</a></li>
    </ul>
</div>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const fabToggle = document.getElementById("fabToggle");
        const fabMenu = document.getElementById("fabMenu");
        let isOpen = false;

        fabToggle.addEventListener("click", function () {
            isOpen = !isOpen;
            fabMenu.style.display = isOpen ? "flex" : "none";
            fabToggle.innerHTML = isOpen ? '<i class="bi bi-x-lg"></i>'
                : '<i class="bi bi-plus-lg"></i>';
        });
    });

    document.getElementById("openCategoryMenu").addEventListener("click", function () {
        document.getElementById("sideMenu").classList.add("open");
    });

    document.getElementById("closeMenuBtn").addEventListener("click", function () {
        document.getElementById("sideMenu").classList.remove("open");
    });

</script>
</html>
