<%@ page import="com.gobookee.users.model.dto.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .fab-container {
	    position: fixed;
	    bottom: 80px;
	    right: 24px;
	    z-index: 999;
	    display: flex;
	    flex-direction: column;
	    align-items: flex-end;
	    gap: 10px; /* 모든 요소 간 간격 통일 */
	}
	
	.fab-main {
	    width: 60px;
	    height: 60px;
	    border-radius: 50%;
	    background-color: #50A65D;
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
	    display: flex;
	    flex-direction: column;
	    align-items: flex-end;
	    gap: 10px; /* 각 A태그 간 간격 통일 */
	    overflow: hidden;
	    max-height: 0;
	    opacity: 0;
	    transform: translateY(10px);
	    transition: max-height 0.4s ease, opacity 0.4s ease, transform 0.4s ease;
	    pointer-events: none;
	    margin: 0; /* 불필요한 여백 제거 */
	}

	.fab-item {
	    background-color: #50A65D;
	    color: white;
	    text-decoration: none;
	    width: 60px;
	    height: 60px;
	    border-radius: 50%;
	    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
	    font-size: 14px;
	    display: flex;
	    align-items: center;
	    justify-content: flex-start;
	    overflow: hidden;
	    transition: width 0.3s ease, border-radius 0.3s ease, gap 0.3s ease;
	    padding-left: 18px;
	    gap: 0;
	    position: relative;
	    right: 0;
	}

	
	.fab-item i {
	    font-size: 20px;
	    flex-shrink: 0;
	    z-index: 1;
	}
	
	.fab-item:hover {
	    width: 180px;
	    border-radius: 30px;
	    gap: 10px;
	}
	
	.fab-label {
	    opacity: 0;
	    margin-left: 12px;
	    white-space: nowrap;
	    transition: opacity 0.2s ease;
	}
	
	.fab-item:hover .fab-label {
	    opacity: 1;
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
	
.fab-menu {
    overflow: hidden;
    max-height: 0;
    opacity: 0;
    transform: translateY(10px);
    transition: max-height 0.4s ease, opacity 0.4s ease, transform 0.4s ease;
    display: flex;
    flex-direction: column;
    margin-bottom: 10px;
    pointer-events: none;
}

.fab-menu.show {
    max-height: 500px; /* 충분히 큰 값으로 설정 */
    opacity: 1;
    transform: translateY(0);
    pointer-events: auto;
}

#fabIcon {
    transition: transform 0.3s ease;
}

#fabIcon.rotate {
    transform: rotate(135deg);
}
</style>
<%
    if (loginUser != null) {
    	
%>
<div class="fab-container">
    
    <div class="fab-menu" id="fabMenu">
    	<%
            if (String.valueOf(loginUser.getUserType()).equals("1")) {
        %>
        <a href="<%=request.getContextPath()%>/place/insertpage" class="fab-item">
            <i class="bi bi-geo-alt-fill"></i> <span class="fab-label">장소 등록하기</span>
        </a>
        <%
            }
        %>
        <a href="<%=request.getContextPath()%>/review/insertpage"
           class="fab-item"> <i class="bi bi-pencil"></i> <span class="fab-label">리뷰 쓰기</span>
        </a> <a href="<%=request.getContextPath()%>/books/searchaladin"
                class="fab-item"> <i class="bi bi-book-fill"></i> <span class="fab-label">책 등록하기</span>
    	</a>
    	</a> <a href="<%=request.getContextPath()%>/study/upload"
                class="fab-item"> <i class="bi bi-book"></i> <span class="fab-label">스터디 등록하기</span>
    	</a>

    </div>
    <button class="fab-main" id="fabToggle">
        <i class="bi bi-plus-lg" id="fabIcon"></i>
    </button>
</div>
<%
    }
%>
</body>
<footer class="py-2">
    <div class="container d-flex justify-content-around text-center small">
    	<div class="col-2" id="openCategoryMenu">
            <div>
                <i class="bi bi-list fs-0"></i>
            </div>
            <div>카테고리</div>
        </div>
        <div class="col-2"
             onclick="location.assign('<%=request.getContextPath()%>/review/listpage')">
            <div>
                <i class="bi bi-chat-text-fill fs-0"></i>
            </div>
            <div>리뷰</div>
        </div>

        <div class="col-2"
             onclick="location.assign('<%=request.getContextPath()%>')">
            <div>
                <i class="bi bi-house-door-fill fs-0"></i>
            </div>
            <div>홈</div>
        </div>
        <div class="col-2"
             onclick="location.assign('<%=request.getContextPath()%>/study/listpage')">
            <div>
                <i class="bi bi-book-fill fs-0"></i>
            </div>
            <div>스터디</div>
        </div>
        <div class="col-2"
        	onclick="location.assign('<%=request.getContextPath()%>/mypage')">
            <div>
                <i class="bi bi-person-fill fs-0"></i>
            </div>
            <div>마이페이지</div>
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
        <%
            if (loginUser == null) {
        %>
        <li><a href="<%=request.getContextPath()%>/loginpage">로그인</a></li>
        <%
        } else {
        %>
        <li><a href="<%=request.getContextPath()%>/logout">로그아웃</a></li>
        <%
            }
        %>
    </ul>
</div>
<script>
	document.addEventListener("DOMContentLoaded", function () {
	    const fabToggle = document.getElementById("fabToggle");
	    const fabMenu = document.getElementById("fabMenu");
	    const fabIcon = document.getElementById("fabIcon");
	    fabToggle.addEventListener("click", function () {
	        const isOpening = !fabMenu.classList.contains("show");
	
	        if (isOpening) {
	            requestAnimationFrame(() => fabMenu.classList.add("show"));
	            fabIcon.classList.add("rotate");
	        } else {
	            fabMenu.classList.remove("show");
	            fabIcon.classList.remove("rotate");
	        }
	    });
	});

    document.getElementById("openCategoryMenu").addEventListener("click", function () {
        document.getElementById("sideMenu").classList.add("open");
    });

    document.getElementById("closeMenuBtn").addEventListener("click", function () {
        document.getElementById("sideMenu").classList.remove("open");
    });
	
    document.addEventListener("click", function (e) {
        const sideMenu = document.getElementById("sideMenu");
        const openBtn = document.getElementById("openCategoryMenu");
        const closeBtn = document.getElementById("closeMenuBtn");

        // 사이드바가 열려있고, 클릭된 요소가 사이드메뉴, 열기버튼, 닫기버튼이 아닐 경우 닫음
        if (sideMenu.classList.contains("open") &&
            !sideMenu.contains(e.target) &&
            !openBtn.contains(e.target) &&
            !closeBtn.contains(e.target)) {
            sideMenu.classList.remove("open");
        }
    });
    




</script>
</html>
