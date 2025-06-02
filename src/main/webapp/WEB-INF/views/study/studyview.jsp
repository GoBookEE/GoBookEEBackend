<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.gobookee.study.model.dto.StudyView, java.util.*" %>
<%@ page import="com.gobookee.users.model.dto.User" %>
<%@ page import="com.gobookee.common.enums.FileType" %>
<%@ page import="com.gobookee.common.CommonPathTemplate" %>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=2d59386dd09d43d5d2ad8f433a1eb0e3&libraries=services"></script>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%
    StudyView studyview = (StudyView) request.getAttribute("studyView");
    List<StudyView> studyviewuser = (List<StudyView>) request.getAttribute("studyViewUser");
    List<StudyView> studynotconfirmeduser = (List<StudyView>) request.getAttribute("studyNotConfirmedUser");
    Long userseq = loginUser.getUserSeq();
    Long host = studyview.getUserSeq();
    boolean isHost = userseq.equals(host);
    boolean isFull = studyview.getConfirmedCount() + 1 >= studyview.getStudyMemberLimit();
%>
<style>
    .center-button {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 40px;
        padding: 0;
        width: 100%;
    }

    /* 드롭다운 메뉴를 정확히 정렬하기 위한 li 스타일 */
    .dropdown-menu > li {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 50px; /* 메뉴 항목 높이 고정 */
        padding: 0; /* 상하 여백 제거 */
    }
</style>

<%
    String dropdownHtml;

    if (isHost) {
        dropdownHtml =
                "<form class='dropdown-menu dropdown-menu-end' action='" + request.getContextPath() + "/study/delete' method='post' onsubmit=\"return confirm('정말 삭제하시겠습니까?');\" style='width: 100%;'>" +
                        "<input type='hidden' name='studySeq' value='" + studyview.getStudySeq() + "'/>" +
                        "<button type='submit' class='dropdown-item text-danger center-button'>게시물 삭제</button>" +
                        "</form>";
    } else {
        dropdownHtml =
                "<li class='dropdown-menu dropdown-menu-end'>" +
                        "<button class='dropdown-item text-warning center-button' onclick='reportPost(" + studyview.getStudySeq() + ", \"STUDY\")'>게시물 신고</button>" +
                        "</li>";
    }
%>

<script>
    $(document).ready(function () {
        $("header").html(`
            <div style="height: 4rem"class="container d-flex justify-content-between align-items-center text-center small position-relative">
                <a class="col-1" style="color:black" href="<%=request.getContextPath()%>/study/listpage">
                    <i class="bi bi-x fs-1"></i>
                </a>

                <div class="dropdown col-1">
                    <button class="btn btn-link text-dark p-0" id="studyMoreMenu"
                            data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-three-dots-vertical fs-0.5"></i>
                    </button>

                        <%= dropdownHtml %>
                </div>
            </div>
        `);
    });
</script>
<main>
    <div class="text-center" style="width: 100%; max-width: 800px; margin: 0 auto;">
        <img src="<%=request.getContextPath()%>/resources/upload/study/<%=studyview.getPhotoName() %>"
             onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png';"
             style="width: 100%; height: auto; object-fit: cover; border-radius: 12px;"
             alt="스터디 이미지">
    </div>
    <div class="d-flex align-items-center">

        <div class="d-flex">
            <img
                    src="<%=request.getContextPath()%>/resources/upload/user/<%=studyview.getUserProfile()%>"
                    class="rounded-circle me-2" alt="user" width="50" height="50"
                    onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png'">

            <div>
                <div class="fw-semibold"><%=studyview.getUserNickName()%>
                </div>
                <div class="progress mt-1" style="height: 8px; width: 150px;">
                    <div class="progress-bar bg-success"
                         style="width: <%=studyview.getUserSpeed()%>%"></div>
                </div>
                <div style="font-size: 0.8rem;"><%=studyview.getUserSpeed()%>km/h로 달리는 중!</div>
            </div>
        </div>
    </div>
    <div><span style="color: #999999"><%= studyview.getStudyCategory() %></span></div>
    <div><strong><%= studyview.getStudyTitle() %>
    </strong></div>
    <div style="word-wrap: break-word;"><%= studyview.getStudyContent()%>
    </div>
    <div>
	    <i class="bi bi-calendar-date" style="color: #50A65D"></i>
	    <%= (studyview.getStudyDate() == null || studyview.getStudyDate().toString().trim().isEmpty()) 
	        ? " 날짜 미등록" 
	        : studyview.getStudyDate().toString() %>
	</div>
    </div>
    <div><i class="bi bi-people-fill" style="color: #50A65D"></i> <%= studyview.getConfirmedCount() + 1%>
        /<%= studyview.getStudyMemberLimit()%>
    </div>
    <% if (studyview.getStudyAddress() == null || studyview.getStudyAddress().trim().isEmpty()) { %>
    <div><span>스터디 장소 미등록</span></div>
	<% } else { %>
	    <div><span>스터디 장소</span> <span><%= studyview.getStudyAddress() %></span></div>
	    <div id="map" style="width:100%; height:300px;"></div>
	<% } %>
    <div style="display: flex;">
        <% if (isHost) { %>
        <button class="d-flex align-items-center p-0 border-0 bg-transparent" data-type="STUDY"
                data-seq="<%=studyview.getStudySeq()%>" data-rec="0">
            <i class="bi bi-hand-thumbs-up me-1" style="font-size: 0.9rem;"></i> <span
                class="count"><%=studyview.getLikeCount()%></span>
        </button>
        <button class="d-flex align-items-center p-0 border-0 bg-transparent" data-type="STUDY"
                data-seq="<%=studyview.getStudySeq()%>" data-rec="1">
            <i class="bi bi-hand-thumbs-down me-1" style="font-size: 0.9rem;"></i> <span
                class="count"><%=studyview.getDislikeCount()%></span>
        </button>
        <% } else { %>
        <button class="btn-recommend-action d-flex align-items-center p-0 border-0 bg-transparent" data-type="STUDY"
                data-seq="<%=studyview.getStudySeq()%>" data-rec="0">
            <i class="bi bi-hand-thumbs-up me-1" style="font-size: 0.9rem;"></i> <span
                class="count"><%=studyview.getLikeCount()%></span>
        </button>
        <button class="btn-recommend-action d-flex align-items-center p-0 border-0 bg-transparent" data-type="STUDY"
                data-seq="<%=studyview.getStudySeq()%>" data-rec="1">
            <i class="bi bi-hand-thumbs-down me-1" style="font-size: 0.9rem;"></i> <span
                class="count"><%=studyview.getDislikeCount()%></span>
        </button>
        <% } %>
    </div>
    <div>참여자 목록</div>
    <div id="study-member-list">
        <% for (StudyView s : studyviewuser) { %>
        <div>
            <img src="<%=request.getContextPath()%>/resources/upload/user/<%=s.getUserProfile()%>"
                 onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png';" width="50"
                 height="50">
            <div class="fw-semibold"><%=s.getUserNickName()%>
            </div>
            <div class="progress mt-1" style="height: 8px; width: 150px;">
                <div class="progress-bar bg-success"
                     style="width: <%=s.getUserSpeed()%>%"></div>
            </div>
            <div style="font-size: 0.8rem;"><%=s.getUserSpeed()%>km/h로 달리는 중!</div>
        </div>
        <% } %>
    </div>
    <% if (isHost) { %>
    <div class="d-grid gap-2">
        <button class="btn btn-outline-dark" style="border-radius: 30px; margin-bottom: 10px;"
                onclick="location.href='<%=request.getContextPath()%>/study/request?studySeq=<%=studyview.getStudySeq()%>'">
            신청자 목록
        </button>
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
        <button type="button" class="btn btn-outline-dark" style="border-radius: 30px; margin-bottom: 10px;"
                data-bs-toggle="modal" data-bs-target="#applyModal">신청하기
        </button>
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
    
    function reportPost(reviewSeq, boardType) {
        const reason = prompt("신고 사유를 입력해주세요.");
        if (reason === null || reason.trim() === "") {
            alert("신고 사유가 필요합니다.");
            return;
        }

        $.ajax({
            url: "<%=request.getContextPath()%>/reports/insert",
            method: "POST",
            data: {
                boardSeq: reviewSeq,
                boardType: boardType,
                reason: reason
            },
            success: function (res) {
                if (res.success) {
                    alert("신고가 접수되었습니다.");
                } else {
                    alert(res.message || "이미 신고하셨습니다.");
                }
            },
            error: function () {
                alert("신고 처리 중 오류 발생.");
            }
        });
    }
</script>
</html>