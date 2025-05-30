<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.gobookee.study.model.dto.StudyView, java.util.*" %>
<%@ page import="com.gobookee.users.model.dto.User" %>
<%
    StudyView studyview = (StudyView) request.getAttribute("studyView");
    List<StudyView> studyviewuser = (List<StudyView>) request.getAttribute("studyViewUser");
    List<StudyView> studynotconfirmeduser = (List<StudyView>) request.getAttribute("studyNotConfirmedUser");
    User loginUser = (User) request.getSession().getAttribute("loginUser");
    Long userseq = loginUser.getUserSeq();
    Long host = studyview.getUserSeq();
    boolean isHost = userseq.equals(host);
    boolean isFull = studyview.getConfirmedCount()+1 >= studyview.getStudyMemberLimit();
%>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=2d59386dd09d43d5d2ad8f433a1eb0e3&libraries=services"></script>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<script>
    $("header").html(`
    <div class="container d-flex justify-content-between align-items-center text-center small">
        <a class="col-1" style="color:black" href="<%=request.getContextPath()%>/study/listpage">
            <i class="bi bi-x fs-1"></i>
        </a>
        <div class="col-1">
            <i class="bi bi-three-dots-vertical fs-0.5"></i>
        </div>
    </div>`);
</script>
<main>
<div class="text-center" style="width: 100%; max-width: 800px; margin: 0 auto;">
  <img src="<%=request.getContextPath()%>/resources/upload/study/<%=studyview.getPhotoName() %>"
       onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png';"
       style="width: 100%; height: auto; object-fit: cover; border-radius: 12px;"
       alt="스터디 이미지">
</div>
<div class="d-flex align-items-center">
    <span style="width:10%;">
        <img src="<%=request.getContextPath()%>/resources/upload/study/<%=Optional.ofNullable(studyview.getUserProfile()).orElse("default.png") %>"
             onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png';"
             width="50" height="50">
    </span>
    <small style="width:30%;"><%= studyview.getUserNickName() %></small>
    <span class="ms-auto">거북이 속도 <%= studyview.getUserSpeed() %></span>
</div>
<div><span style="color: #999999"><%= studyview.getStudyCategory() %></span></div>
<div><strong><%= studyview.getStudyTitle() %></strong></div>
<div><%= studyview.getStudyContent()%></div>
<div><i class="bi bi-calendar-date" style="color: #50A65D"></i> <%= studyview.getStudyDate()%></div>
<div><i class="bi bi-people-fill" style="color: #50A65D"></i> <%= studyview.getConfirmedCount() + 1%>/<%= studyview.getStudyMemberLimit()%></div>
<div><span>스터디 장소</span> <span><%= studyview.getStudyAddress() %></span></div>
<div id="map" style="width:100%; height:300px;"></div>
<div style="display: flex;">
<% if (isHost) { %>
    <button class="d-flex align-items-center p-0 border-0 bg-transparent" data-type="STUDY" data-seq="<%=studyview.getStudySeq()%>" data-rec="0">
        <i class="bi bi-hand-thumbs-up me-1" style="font-size: 0.9rem;"></i> <span class="count"><%=studyview.getLikeCount()%></span>
    </button>
    <button class="d-flex align-items-center p-0 border-0 bg-transparent" data-type="STUDY" data-seq="<%=studyview.getStudySeq()%>" data-rec="1">
        <i class="bi bi-hand-thumbs-down me-1" style="font-size: 0.9rem;"></i> <span class="count"><%=studyview.getDislikeCount()%></span>
    </button>
<% } else { %>
    <button class="btn-recommend-action d-flex align-items-center p-0 border-0 bg-transparent" data-type="STUDY" data-seq="<%=studyview.getStudySeq()%>" data-rec="0">
        <i class="bi bi-hand-thumbs-up me-1" style="font-size: 0.9rem;"></i> <span class="count"><%=studyview.getLikeCount()%></span>
    </button>
    <button class="btn-recommend-action d-flex align-items-center p-0 border-0 bg-transparent" data-type="STUDY" data-seq="<%=studyview.getStudySeq()%>" data-rec="1">
        <i class="bi bi-hand-thumbs-down me-1" style="font-size: 0.9rem;"></i> <span class="count"><%=studyview.getDislikeCount()%></span>
    </button>
<% } %>
</div>
<div>참여자 목록</div>
<div id="study-member-list">
<% for (StudyView s : studyviewuser) { %>
    <div>
        <img src="<%=request.getContextPath()%>/resources/upload/study/<%=Optional.ofNullable(s.getUserProfile()).orElse("default.png") %>"
             onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png';" width="50" height="50">
        <%= s.getUserNickName() %> <%= s.getUserSpeed() %>
    </div>
<% } %>
</div>
<% if (isHost) { %>
    <div class="d-grid gap-2">
        <button class="btn btn-outline-dark" style="border-radius: 30px; margin-bottom: 10px;"
                onclick="location.href='<%=request.getContextPath()%>/study/request?studySeq=<%=studyview.getStudySeq()%>'">신청자 목록</button>
    </div>
<% } else { 
    boolean isJoined = studyviewuser.stream().anyMatch(u -> u.getUserSeq().equals(userseq));
    if (!isJoined) {
        if (isFull) { %>
            <div class="d-grid gap-2">
                <button class="btn btn-secondary" style="border-radius: 30px; margin-bottom: 10px;" disabled>모집완료</button>
            </div>
        <% } else {
            Optional<StudyView> myRequest = studynotconfirmeduser.stream().filter(u -> u.getUserSeq().equals(userseq)).findFirst();
            if (myRequest.isPresent()) {
                String confirm = myRequest.get().getRequestConfirm();
                if ("R".equals(confirm)) { %>
                    <div class="d-grid gap-2">
                        <button class="btn-outline-danger btn" style="border-radius: 30px; margin-bottom: 10px;">거절됨</button>
                    </div>
                <% } else if ("N".equals(confirm)) { %>
                    <div class="d-grid gap-2">
                        <button class="btn-outline-success btn" style="border-radius: 30px; margin-bottom: 10px;">신청완료</button>
                    </div>
                <% }
            } else { %>
                <div class="d-grid gap-2">
                    <button type="button" class="btn btn-outline-dark" style="border-radius: 30px; margin-bottom: 10px;" data-bs-toggle="modal" data-bs-target="#applyModal">신청하기</button>
                </div>
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
                                    <input type="text" name="requestMsg" class="form-control">
                                    <input type="hidden" name="studySeq" value="<%=studyview.getStudySeq()%>">
                                    <input type="hidden" name="userSeq" value="<%=loginUser.getUserSeq()%>">
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                                    <button type="submit" class="btn btn-success">신청하기</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            <% }
        }
    }
} %>
</main>
<script>
    const map = new kakao.maps.Map(document.getElementById('map'), {
        center: new kakao.maps.LatLng(<%= studyview.getStudyLatitude()%>, <%= studyview.getStudyLongitude()%>),
        level: 3
    });
    new kakao.maps.Marker({
        position: new kakao.maps.LatLng(<%= studyview.getStudyLatitude()%>, <%= studyview.getStudyLongitude()%>)
    }).setMap(map);
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
