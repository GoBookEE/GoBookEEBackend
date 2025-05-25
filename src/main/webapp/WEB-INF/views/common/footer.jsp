<%@ page contentType="text/html;charset=UTF-8" language="java" %>
</body>

<footer class="py-2">
    <div class="container d-flex justify-content-around text-center small">
        <div class="col-2" onclick="location.assign('<%=request.getContextPath()%>/review/listpage')">
            <div><i class="bi bi-chat-text-fill fs-1"></i></div>
            <div>리뷰</div>
        </div>
        <div class="col-2" onclick="location.assign('<%=request.getContextPath()%>/study/listpage')">
            <div><i class="bi bi-book-fill fs-1"></i></div>
            <div>스터디</div>
        </div>
        <div class="col-2" onclick="location.assign('<%=request.getContextPath()%>')">
            <div><i class="bi bi-house-door-fill fs-1"></i></div>
            <div>홈</div>
        </div>
        <div class="col-2">
            <div><i class="bi bi-cursor-fill fs-1"></i></div>
            <div>채팅</div>
        </div>
        <div class="col-2">
            <div><i class="bi bi-list fs-1"></i></div>
            <div>카테고리</div>
        </div>
    </div>
</footer>
</html>
