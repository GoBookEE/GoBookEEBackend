<%@ page import="com.gobookee.place.model.dto.PlaceViewResponse" %>
<%@ page import="com.gobookee.users.model.dto.User" %>
<%@ page import="com.gobookee.common.CommonPathTemplate" %>
<%@ page import="com.gobookee.common.enums.FileType" %>
<%@ page pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=2d59386dd09d43d5d2ad8f433a1eb0e3&libraries=services"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<style>
    header, footer {
        display: none !important;
    }

    .container {
        padding-top: 20px !important;
        padding-bottom: 10px !important;
        margin: 0 auto !important;
    }

    .confirm-group {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .confirm-btn {
        border: none;
        padding: 6px 12px;
        border-radius: 50%;
        font-weight: bold;
        color: white;
        cursor: pointer;
        background-color: #d6f0dd;
        transition: background-color 0.3s;
        margin: 0 8px;
        box-shadow: none !important;
        outline: none !important;
    }

    .confirm-btn:hover {
        background-color: #a5d6b5;
    }

    .confirm-btn.active {
        background-color: #50A65D;
    }

    .confirm-btn.active:hover {
        background-color: #449953;
    }
</style>
<%
    PlaceViewResponse place = (PlaceViewResponse) request.getAttribute("place");
    boolean isOwner = loginUser.getUserSeq().equals(place.getUserSeq());
%>

<main class="container">
    <!-- ✅ 콘텐츠 너비에 맞춰 wrapping -->
    <div class="container" style="max-width: 600px;">
        <div class="d-flex justify-content-between align-items-center border-bottom mb-3"
             style="position: fixed; top:0; left:30%; right: 30%; z-index: 1030; background-color: #fff;">
            <button class="btn btn-link text-dark text-decoration-none"
                    onclick="history.back()">
                <i class="bi bi-arrow-left"></i>
            </button>

            <div class="dropdown">
                <button class="btn btn-link text-dark" id="moreMenu"
                        data-bs-toggle="dropdown" aria-expanded="false">
                    <i class="bi bi-three-dots-vertical"></i>
                </button>
                <form id="deleteForm" action="<%=request.getContextPath()%>/place/delete" method="post"
                      style="display:none;">
                    <input type="hidden" name="placeSeq" id="deletePlaceSeq">
                </form>
                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="moreMenu">
                    <%
                        if (loginUser != null && loginUser.getUserSeq().equals(place.getUserSeq())) {
                    %>
                    <li>
                        <a class="dropdown-item"
                           href="<%=request.getContextPath()%>/place/updatepage?placeSeq=<%=place.getPlaceSeq()%>">게시물
                            수정</a>
                    </li>
                    <li>
                        <a class="dropdown-item text-danger" href="#"
                           onclick="return confirmDeleteReview(<%=place.getPlaceSeq()%>);">게시물 삭제</a>
                    </li>
                    <%
                    } else {
                    %>
                    <li>
                        <button class="dropdown-item text-danger"
                                onclick="reportPost(<%=place.getPlaceSeq()%>,'PLACE')">장소글 신고
                        </button>
                    </li>
                    <%
                        }
                    %>
                </ul>
            </div>
        </div>
    </div>

    <div class="container" style="max-width: 600px;">
        <!-- 🖼️ 이미지 Carousel -->
        <div id="placeImageCarousel" class="carousel slide mb-3" data-bs-ride="carousel">
            <div class="carousel-inner rounded">
                <% for (int i = 0; i < place.getPhotoNames().size(); i++) { %>
                <div class="carousel-item <%= i == 0 ? "active" : "" %>">
                    <img src="<%=CommonPathTemplate.getUploadPath(request,FileType.PLACE,place.getPhotoNames().get(i))%>"
                         class="d-block w-100" alt="이미지<%=i+1%>"
                         onerror="this.onerror=null; this.src='<%=request.getContextPath()%>/resources/images/default.png'; this.classList.remove('w-100'); this.style.width='100px'; this.style.height='100px';">
                </div>
                <% } %>
            </div>
            <button class="carousel-control-prev" type="button" data-bs-target="#placeImageCarousel"
                    data-bs-slide="prev">
                <span class="carousel-control-prev-icon"></span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#placeImageCarousel"
                    data-bs-slide="next">
                <span class="carousel-control-next-icon"></span>
            </button>
        </div>

        <!-- 👤 작성자 정보 -->
        <div class="d-flex align-items-center mb-3">
            <img src="<%=request.getContextPath()%>/resources/upload/user/<%=place.getUserProfileImage()%>"
                 class="rounded-circle me-3"
                 width="50" height="50" alt="프로필"
                 onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png'">
            <div>
                <div class="fw-bold"><%=place.getUserNickname()%>
                </div>
                <div class="progress mt-1" style="height: 8px; width: 150px;">
                    <div class="progress-bar bg-success" style="width: <%=place.getUserSpeed()%>%"></div>
                </div>
                <div style="font-size: 0.8rem;"><%=place.getUserSpeed()%>km/h로 달리는 중!</div>
            </div>
        </div>

        <!-- 📍 장소 제목 및 내용 -->
        <h5 class="fw-bold"><%=place.getPlaceTitle()%>
        </h5>
        <p class="text-muted"><%=place.getPlaceContents()%>
        </p>

        <!-- 🧭 주소 -->
        <div class="mb-2">
            <strong>주소:</strong> <%=place.getPlaceAddress()%>
        </div>

        <!-- 🗺️ 카카오맵 -->
        <div id="map" style="width:100%; height:300px;" class="rounded mb-3"></div>

        <!-- 👍👎 추천/비추천 -->
        <div class="d-flex justify-content-start align-items-center gap-4 mt-3">
            <%
                if (loginUser != null) {
            %>
            <button
                    class="btn-recommend-action btn-sm d-flex align-items-center p-0 border-0 bg-transparent"
                    data-type="PLACE" data-seq="<%=place.getPlaceSeq()%>"
                    data-rec="0">
                <i class="bi bi-hand-thumbs-up me-1"
                   style="font-size: 0.9rem;"></i> <span class="count"><%=place.getPlaceRecCount()%></span>
            </button>

            <!-- 비추천 버튼 -->
            <button
                    class="btn-recommend-action btn-sm d-flex align-items-center p-0 border-0 bg-transparent"
                    data-type="PLACE" data-seq="<%=place.getPlaceSeq()%>"
                    data-rec="1">
                <i class="bi bi-hand-thumbs-down me-1"
                   style="font-size: 0.9rem;"></i> <span class="count"><%=place.getPlaceNonRecCount()%></span>
            </button>
            <%
                }
            %>
        </div>
    </div>

    <div class="text-center">
        <button class="btn btn-dark" data-bs-toggle="modal" data-bs-target="#reservationModal" style="width: 200px">예약
        </button>
    </div>

    <div class="modal fade" id="reservationModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content rounded-4">
                <div class="modal-header border-0">
                    <h5 class="modal-title">장소 예약하기</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div id="step-calendar">
                        <div id="calendarInlineContainer" class="mb-4"></div>
                        <h6 class="fw-bold" id="selectedDateTitle">선택된 날짜 없음</h6>
                        <div id="reservationCount" class="mb-3 text-muted small"></div>
                        <% if (!isOwner) { %>
                        <button id="goToStudySelect" class="btn btn-success w-100 mb-3">예약 진행하기</button>
                        <% } %>
                        <% if (isOwner) { %>
                        <table class="table table-bordered text-center mt-3">
                            <thead class="table-light">
                            <tr>
                                <th>스터디명</th>
                                <th>인원</th>
                                <th>전화번호</th>
                                <th>확정</th>
                            </tr>
                            </thead>
                            <tbody id="reservationTableBody"></tbody>
                        </table>
                        <% } %>
                    </div>

                    <% if (!isOwner) { %>
                    <div id="step-study-select" style="display:none;">
                        <h6 class="fw-bold mb-3">나의 스터디 그룹 선택</h6>
                        <ul id="groupList" class="list-group mb-3"></ul>
                        <button class="btn btn-secondary w-100" onclick="goBackToCalendar()">이전</button>
                    </div>

                    <div id="step-confirm" style="display:none">
                        <h6 class="fw-bold mb-3">예약 확인</h6>
                        <form method="post" action="<%=request.getContextPath()%>/schedule/insert">
                            <input type="hidden" name="placeSeq" value="<%=place.getPlaceSeq()%>">
                            <input type="hidden" id="formStudySeq" name="studySeq">
                            <input type="hidden" id="formDate" name="date">
                            <div id="confirmInfo" class="border rounded p-3 mb-3 bg-light text-center"></div>
                            <button type="submit" class="btn btn-success w-100">예약하기</button>
                        </form>
                        <button class="btn btn-outline-dark w-100 mt-2" onclick="goBackToStudySelect()">스터디 선택으로 돌아가기
                        </button>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
    const map = new kakao.maps.Map(document.getElementById('map'), {
        center: new kakao.maps.LatLng(<%=place.getPlaceLatitude()%>, <%=place.getPlaceLongitude()%>),
        level: 3
    });
    new kakao.maps.Marker({
        position: new kakao.maps.LatLng(<%=place.getPlaceLatitude()%>, <%=place.getPlaceLongitude()%>)
    }).setMap(map);

    const userSeq = <%= loginUser.getUserSeq() %>;
    const placeSeq = <%= place.getPlaceSeq() %>;
    const isOwner = <%= loginUser.getUserSeq().equals(place.getUserSeq()) %>;
    let selectedDate = null;
    let selectedGroupId = null;
    let calendarInstance = null;

    $('#reservationModal').on('shown.bs.modal', function () {
        initCalendar();
        $('#step-calendar').show();
        if (!isOwner) {
            $('#step-study-select, #step-confirm').hide();
        }
    });

    function initCalendar() {
        const calendarEl = document.createElement('div');
        document.getElementById('calendarInlineContainer').innerHTML = '';
        document.getElementById('calendarInlineContainer').appendChild(calendarEl);

        calendarInstance = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            selectable: true,
            validRange: {
                start: new Date().toISOString().split('T')[0]  // 오늘 날짜부터 가능
            },
            dateClick: function (info) {
                selectedDate = info.dateStr;
                document.getElementById('selectedDateTitle').innerText = `\${info.dateStr} 예약 현황`;
                if (document.getElementById('formDate')) {
                    document.getElementById('formDate').value = selectedDate;
                }
                loadReservations(selectedDate);
            }
        });
        calendarInstance.render();
    }

    $('#goToStudySelect').on('click', function () {
        if (selectedDate) {
            loadStudyGroups();
            $('#step-calendar').hide();
            $('#step-study-select').show();
        } else {
            alert('먼저 날짜를 선택해주세요.');
        }
    });

    function loadReservations(date) {
        $.get(`<%=request.getContextPath()%>/schedule/searchreservation?date=\${date}&placeSeq=\${placeSeq}`, function (list) {
            const $tbody = $('#reservationTableBody').empty();
            const $count = $('#reservationCount').empty();

            if (!Array.isArray(list) || list.length === 0) {
                if (isOwner) {
                    $tbody.append('<tr><td colspan="4">예약 없음</td></tr>');
                }
                $count.text("예약된 팀 없음");
                return;
            }

            $count.text(`현재 예약된 팀 수: \${list.length}`);

            if (!isOwner) return; // ✅ 일반 사용자는 목록 표시 안 함

            list.forEach(resv => {
                let confirmBtn = '';
                if (resv.requestConfirm === 'Y') {
                    confirmBtn = '<span class="confirm-group"><span class="confirm-btn active">O</span><span class="confirm-btn">X</span></span>';
                } else if (resv.requestConfirm === 'R') {
                    confirmBtn = '<span class="confirm-group"><span class="confirm-btn">O</span><span class="confirm-btn active">X</span></span>';
                } else {
                    confirmBtn = `
                    <div class="confirm-group">
                        <button type="button" class="confirm-btn" onclick="confirmReservation(selectedDate, \${resv.studySeq}, \${resv.scheduleSeq}, 'Y', \${placeSeq})">O</button>
                        <button type="button" class="confirm-btn" onclick="confirmReservation(selectedDate, \${resv.studySeq}, \${resv.scheduleSeq}, 'R', \${placeSeq})">X</button>
                    </div>
                `;
                }
                $tbody.append(`
                <tr>
                    <td>\${resv.studyTitle}</td>
                    <td>\${resv.studyCurrCount}/\${resv.studyMemberLimit}</td>
                    <td>\${resv.studyContact || '-'}</td>
                    <td>\${confirmBtn}</td>
                </tr>
            `);
            });
        });
    }

    function confirmReservation(date, studySeq, scheduleSeq, status, placeSeq) {
        console.log(date, studySeq, scheduleSeq, status, placeSeq);
        $.ajax({
            url: '<%=request.getContextPath()%>/schedule/confirm',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                date: date,
                studySeq: studySeq,
                scheduleSeq: scheduleSeq,
                status: status,
                placeSeq: placeSeq
            }),
            success: function (response) {
                if (response === true) {
                    alert("예약 확정에 성공했습니다.")
                    loadReservations(selectedDate);
                } else {
                    alert('예약 확정에 실패했습니다.');
                }
            },
            error: function () {
                alert('확정 처리 실패');
            }
        });
    }

    function loadStudyGroups() {
        $.get('<%=request.getContextPath()%>/study/searchstudy', function (list) {
            const $ul = $('#groupList').empty();
            list.forEach(study => {
                $ul.append(`<li class="list-group-item group-item" data-id="\${study.studySeq}">\${study.studyTitle}</li>`);
            });
        });
    }

    $(document).on('click', '.group-item', function () {
        selectedGroupId = $(this).data('id');

        // 중복 예약 확인 Ajax
        $.get(`<%=request.getContextPath()%>/schedule/searchreservation?date=\${selectedDate}&placeSeq=\${placeSeq}`, function (list) {
            const isDuplicate = list.some(item => item.studySeq === selectedGroupId);
            if (isDuplicate) {
                alert("이미 해당 스터디로 이 날짜에 장소를 예약하셨습니다.");
                return;
            }

            // 중복 아님 → 예약 확인단계로 이동
            $('#formStudySeq').val(selectedGroupId);
            $('#confirmInfo').html(`<p>\${selectedDate}에<br><strong>\${$(this).text()}</strong> 그룹으로 예약합니다.</p>`);
            $('#step-study-select').hide();
            $('#step-confirm').show();
        });
    });

    function goBackToCalendar() {
        $('#step-study-select').hide();
        $('#step-calendar').show();
        if (calendarInstance) {
            calendarInstance.render();
        }
    }

    function goBackToStudySelect() {
        $('#step-confirm').hide();
        $('#step-study-select').show();
    }

    function confirmDeleteReview(seq) {
        if (confirm("정말 이 게시물을 삭제하시겠습니까?")) {
            document.getElementById("deletePlaceSeq").value = seq;
            document.getElementById("deleteForm").submit();
        }
        return false; // 기본 링크 동작 방지
    }


    //신고
    function reportPost(placeSeq, boardType) {
        const reason = prompt("신고 사유를 입력해주세요.");
        if (reason === null || reason.trim() === "") {
            alert("신고 사유가 필요합니다.");
            return;
        }

        $.ajax({
            url: "<%=request.getContextPath()%>/reports/insert",
            method: "POST",
            data: {
                boardSeq: placeSeq,
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


    //추천, 비추천
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
                recType: recType,
                boardType: targetType
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