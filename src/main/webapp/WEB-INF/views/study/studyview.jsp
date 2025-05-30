<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.gobookee.study.model.dto.StudyView, java.util.*" %>
<%@ page import="com.gobookee.users.model.dto.User" %>
<%
    StudyView studyview = (StudyView) request.getAttribute("studyView");
    List<StudyView> studyviewuser = (List<StudyView>) request.getAttribute("studyViewUser");
    List<StudyView> studynotconfirmeduser = (List<StudyView>) request.getAttribute("studyNotConfirmedUser");
%>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=2d59386dd09d43d5d2ad8f433a1eb0e3&libraries=services"></script>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<script>
    $("header").html(
        `<div class="container d-flex justify-content-between align-items-center text-center small">
<a class="col-1" style="color:black" href="<%=request.getContextPath()%>/study/listpage">
    <i class="bi bi-x fs-1"></i>
</a>
<div class="col-1">
<i class="bi bi-three-dots-vertical fs-1"></i>
</a>
</div>
</div>`);
</script>
<main>
    <img src="<%=request.getContextPath()%>/resources/upload/study/<%=studyview.getPhotoName() %>"
         onerror="this.onerror=null; this.src='<%=request.getContextPath()%>/resources/images/default.png'; this.classList.remove('w-100'); this.style.width='100px'; this.style.height='100px';">
    <div>
		<span>
			<%
                String profile = studyview.getUserProfile();
                if (profile == null || profile.trim().isEmpty()) {
            %>
			<img src="<%=request.getContextPath()%>/resources/images/default.png" width='50' height='50'>
			<%} else { %>
			<img src="<%=request.getContextPath()%>/resources/upload/study/<%=studyview.getUserProfile() %>" width="50" height="50">
			<%} %>
		</span>
        <small><%= studyview.getUserNickName() %>
        </small>
        <span>거북이 속도 <%= studyview.getUserSpeed() %></span>
    </div>
    <div><span style="color: #999999"><%= studyview.getStudyCategory() %></span></div>
    <div><strong><%= studyview.getStudyTitle() %>
    </strong></div>
    <div><%= studyview.getStudyContent()%>
    </div>
    <div>
        <i class=" bi bi-calendar-date" style="color: #50A65D"> </i>
        <%= studyview.getStudyDate()%>
    </div>
    <div>
        <i class=" bi bi-people-fill" style="color: #50A65D"></i>
        <%= studyview.getConfirmedCount() + 1%>
        <span>/</span>
        <%= studyview.getStudyMemberLimit()%>
    </div>
    <div>
        <span>스터디 장소</span>
        <span><%= studyview.getStudyAddress() %></span>
    </div>
    <% %>
    <div id="map" style="width:100%; height:300px;"></div>
    <%
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        Long userseq = null;
        userseq = loginUser.getUserSeq();
        Long host = studyview.getUserSeq();
        if (loginUser != null) {
            if (userseq.equals(host)) {
    %>
    <div style="display: flex;">
        <button
                class="d-flex align-items-center p-0 border-0 bg-transparent"
                data-type="STUDY" data-seq="<%=studyview.getStudySeq()%>"
                data-rec="0">
            <i class="bi bi-hand-thumbs-up me-1" style="font-size: 0.9rem;"></i> <span
                class="count"><%=studyview.getLikeCount()%></span>
        </button>

        <!-- 댓글 비추천 버튼 -->
        <button
                class="d-flex align-items-center p-0 border-0 bg-transparent"
                data-type="STUDY" data-seq="<%=studyview.getStudySeq()%>"
                data-rec="1">
            <i class="bi bi-hand-thumbs-down me-1" style="font-size: 0.9rem;"></i> <span
                class="count"><%=studyview.getDislikeCount()%></span>
        </button>
    </div>
    <% } else {%>
    <div style="display: flex;">
        <button
                class="btn-recommend-action d-flex align-items-center p-0 border-0 bg-transparent"
                data-type="STUDY" data-seq="<%=studyview.getStudySeq()%>"
                data-rec="0">
            <i class="bi bi-hand-thumbs-up me-1" style="font-size: 0.9rem;"></i> <span
                class="count"><%=studyview.getLikeCount()%></span>
        </button>
        <!-- 댓글 비추천 버튼 -->
        <button
                class="btn-recommend-action d-flex align-items-center p-0 border-0 bg-transparent"
                data-type="STUDY" data-seq="<%=studyview.getStudySeq()%>"
                data-rec="1">
            <i class="bi bi-hand-thumbs-down me-1" style="font-size: 0.9rem;"></i> <span
                class="count"><%=studyview.getDislikeCount()%></span>
        </button>
    </div>
    <% }
    }
    %>
    <div></div>
    <div>참여자 목록</div>
    <div id="study-member-list">
        <% for (StudyView s : studyviewuser) { %>
        <div>
            <%
                String users = studyviewuser.get(0).getUserProfile();
                if (profile == null || profile.trim().isEmpty()) {
            %>
            <img src="<%=request.getContextPath()%>/resources/images/default.png" width="50" height="50">
            <%} else { %>
            <img src="<%=request.getContextPath()%>/resources/upload/study/<%=studyviewuser.get(0).getUserProfile() %>"
                 width="50" height="50">
            <%} %>
            <%= s.getUserNickName() %>
            <%= s.getUserSpeed() %>
        </div>
        <% } %>

    </div>

    <% if (userseq.equals(host)) { %>
    <div class="d-grid gap-2">
        <button class="btn btn-outline-dark"
                style="border-radius: 30px; margin-bottom: 10px;"
                onclick="location.href='<%=request.getContextPath()%>/study/request?studySeq=<%=studyview.getStudySeq()%>'">
            신청자 목록
        </button>
    </div>
    <% } else {
        boolean isJoined = false;
        for (int i = 0; i < studyviewuser.size(); i++) {
            if (studyviewuser.get(i).getUserSeq().equals(userseq)) {
                isJoined = true;
                break;
            }
        }

        if (!isJoined) {
            boolean found = false;
            for (int j = 0; j < studynotconfirmeduser.size(); j++) {
                if (studynotconfirmeduser.get(j).getUserSeq().equals(userseq)) {
                    String confirm = studynotconfirmeduser.get(j).getRequestConfirm();
                    found = true;
                    if ("R".equals(confirm)) {
    %>
    <div class="d-grid gap-2">
        <button class="btn-outline-danger btn" style="border-radius: 30px; margin-bottom: 10px;" id="study-application">
            거절됨
        </button>
    </div>
    <%
    } else if ("N".equals(confirm)) {
    %>
    <div class="d-grid gap-2">
        <button class="btn-outline-success btn" style="border-radius: 30px; margin-bottom: 10px;"
                id="study-application">신청완료
        </button>
    </div>
    <%
                }
                break;
            }
        }
        if (!found) {
    %>
    <!-- 신청하기 버튼 -->
    <!-- 신청 버튼 (모달 트리거) -->
    <div class="d-grid gap-2">
        <button type="button" class="btn btn-outline-dark" style="border-radius: 30px; margin-bottom: 10px;"
                data-bs-toggle="modal" data-bs-target="#applyModal">
            신청하기
        </button>
    </div>

    <!-- 모달 -->
    <div class="modal fade" id="applyModal" tabindex="-1" aria-labelledby="applyModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <form action="<%=request.getContextPath()%>/study/view" method="post">
                <div class="modal-content">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="applyModalLabel">신청 멘트 작성</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div>신청 멘트</div>
                        <div>
                            <input type="text" name="requestMsg" class="form-control">
                            <input type="hidden" name="studySeq" value="<%=studyview.getStudySeq()%>">
                            <input type="hidden" name="userSeq" value="<%=loginUser.getUserSeq()%>">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                        <button type="submit" class="btn btn-success">신청하기</button>
                    </div>
                </div>
            </form>
        </div>
    </div>


    <%
                }
            }
        }
    %>


</main>
<script>


    const map = new kakao.maps.Map(document.getElementById('map'), {
        center: new kakao.maps.LatLng(<%= studyview.getStudyLatitude()%>, <%= studyview.getStudyLongitude()%>),
        level: 3
    });
    new kakao.maps.Marker({
        position: new kakao.maps.LatLng(<%= studyview.getStudyLatitude()%>, <%= studyview.getStudyLongitude()%>)
    }).setMap(map);

    $(document).on("click", ".btn-recommend-action", function () {
        const $btn = $(this);
        const targetType = $btn.data("type"); // "REVIEW" or "COMMENT"
        const targetSeq = $btn.data("seq");
        const recType = $btn.data("rec");     // 0: 추천, 1: 비추천

        $.ajax({
            url: "<%=request.getContextPath()%>/recommend/insert",
            type: "POST",
            data: {
                boardSeq: targetSeq,
                recType: recType
            },
            success: function (data) {
                if (data.success) {
                    // 추천
                    $(`.btn-recommend-action[data-type='\${targetType}'][data-seq='\${targetSeq}'][data-rec='0']`)
                        .find(".count")
                        .text(data.recommendCount);

                    // 비추천
                    $(`.btn-recommend-action[data-type='\${targetType}'][data-seq='\${targetSeq}'][data-rec='1']`)
                        .find(".count")
                        .text(data.nonRecommendCount);
                } else {
                    alert(data.message || "이미 처리된 항목입니다.");
                }
            },
            error: function () {
                alert("추천/비추천 처리 중 오류 발생!");
            }
        });
    });


</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>