<%@ page import="com.gobookee.place.model.dto.PlaceViewResponse" %>
<%@ page import="com.gobookee.common.CommonPathTemplate" %>
<%@ page import="com.gobookee.common.enums.FileType" %>
<%@ page import="com.gobookee.users.model.dto.User" %>
<%@ page pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=2d59386dd09d43d5d2ad8f433a1eb0e3&libraries=services"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>

<%
    PlaceViewResponse place = (PlaceViewResponse) request.getAttribute("place");
    boolean isOwner = (boolean) request.getAttribute("isOwner");
    User loginMember = (User) session.getAttribute("loginUser");
%>

<main class="container my-4">
    <h2 class="fw-bold mb-4 text-center"><%= place.getPlaceTitle() %> 예약</h2>

    <!-- 지도 및 정보 -->
    <div class="row mb-5">
        <div class="col-md-6">
            <div id="map" style="width:100%; height:400px; border-radius: 10px;"></div>
        </div>
        <div class="col-md-6">
            <p><strong>주소:</strong> <%=place.getPlaceAddress()%>
            </p>
            <p><strong>내용:</strong> <%=place.getPlaceContents()%>
            </p>
            <p><strong>위도/경도:</strong> <%=place.getPlaceLatitude()%> / <%=place.getPlaceLongitude()%>
            </p>
            <p><strong>작성자:</strong> <%=place.getUserNickname()%> (속도: <%=place.getUserSpeed()%>)</p>
            <p><strong>추천/비추천:</strong> <%=place.getPlaceRecCount()%> / <%=place.getPlaceNonRecCount()%>
            </p>
            <% if (place.getPhotoNames() != null && !place.getPhotoNames().isEmpty()) {
                for (String photoName : place.getPhotoNames()) { %>
            <img src="<%=CommonPathTemplate.getUploadPath(request, FileType.PLACE, photoName)%>" alt=""
                 class="img-fluid rounded mb-2"/>
            <% }
            } %>
        </div>
    </div>

    <!-- 예약 버튼 -->
    <div class="text-center">
        <button class="btn btn-dark px-5 py-2" data-bs-toggle="modal" data-bs-target="#reservationModal">예약하기</button>
    </div>

    <!-- 예약 모달 (세로 크기 조절: modal-xl → modal-lg, 높이 줄이기) -->
    <div class="modal fade" id="reservationModal" tabindex="-1">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content p-4">
                <div class="modal-header border-0 pb-0">
                    <h4 class="modal-title fw-bold">장소 예약</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body pt-0">
                    <% if (isOwner) { %>
                    <!-- 사장용 캘린더 & 예약 리스트 -->
                    <div id="ownerCalendar" class="mb-4"></div>
                    <h5 class="mb-3" id="selectedDateTitle">예약 현황</h5>
                    <div class="table-responsive">
                        <table class="table table-bordered text-center">
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
                    </div>
                    <% } else { %>
                    <!-- 일반 유저: 그룹 선택 → 날짜 → 예약 확인 -->
                    <div id="step-group">
                        <h6 class="mb-3 fw-bold">스터디 그룹 선택</h6>
                        <ul id="groupList" class="list-group"></ul>
                    </div>
                    <div id="step-calendar" style="display:none">
                        <h6 class="fw-bold mb-3">예약 날짜 선택</h6>
                        <div id="userCalendar" class="mb-3"></div>
                        <button class="btn btn-secondary" onclick="goBackToGroup()">이전</button>
                    </div>
                    <div id="step-confirm" style="display:none">
                        <h6 class="fw-bold mb-3">예약 확인</h6>
                        <form method="post" action="<%=request.getContextPath()%>/schedule/insert">
                            <input type="hidden" name="placeSeq" value="<%=place.getPlaceSeq()%>">
                            <input type="hidden" id="formStudySeq" name="studySeq">
                            <input type="hidden" id="formDate" name="date">
                            <div id="confirmInfo" class="border rounded p-3 mb-3 bg-light"></div>
                            <button type="submit" class="btn btn-success w-100">예약하기</button>
                        </form>
                        <button class="btn btn-outline-dark mt-2 w-100" onclick="goBackToDate()">날짜 선택으로 돌아가기</button>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
    const latitude = <%= place.getPlaceLatitude() %>;
    const longitude = <%= place.getPlaceLongitude() %>;
    const mapContainer = document.getElementById('map');
    const mapOption = {
        center: new kakao.maps.LatLng(latitude, longitude),
        level: 3
    };
    const map = new kakao.maps.Map(mapContainer, mapOption);
    const marker = new kakao.maps.Marker({position: new kakao.maps.LatLng(latitude, longitude)});
    marker.setMap(map);

    const isOwner = <%= isOwner %>;
    const placeSeq = <%= place.getPlaceSeq() %>;
    const userSeq = <%= loginMember.getUserSeq() %>;
    let calendar, selectedGroupId = null;

    $('#reservationModal').on('shown.bs.modal', function () {
        if (isOwner) initOwnerCalendar();
        else {
            $('#step-group').show();
            $('#step-calendar, #step-confirm').hide();
            loadStudyGroups();
        }
    });

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
        $('#formStudySeq').val(selectedGroupId);
        $('#step-group').hide();
        $('#step-calendar').show();
        initUserCalendar();
    });

    function goBackToGroup() {
        $('#step-calendar').hide();
        $('#step-group').show();
    }

    function goBackToDate() {
        $('#step-confirm').hide();
        $('#step-calendar').show();
        if (calendar) calendar.updateSize();
    }

    function initUserCalendar() {
        if (calendar) calendar.destroy();
        calendar = new FullCalendar.Calendar(document.getElementById('userCalendar'), {
            initialView: 'dayGridMonth',
            selectable: true,
            dateClick(info) {
                $('#formDate').val(info.dateStr);
                $('#confirmInfo').html(`<p>예약 날짜: \${info.dateStr}</p>`);
                $('#step-calendar').hide();
                $('#step-confirm').show();
            }
        });
        calendar.render();
    }

    function initOwnerCalendar() {
        if (calendar) calendar.destroy();
        calendar = new FullCalendar.Calendar(document.getElementById('ownerCalendar'), {
            initialView: 'dayGridMonth',
            selectable: true,
            dateClick(info) {
                loadReservations(info.dateStr);
                $('#selectedDateTitle').text(`\${info.dateStr} 예약 현황`);
            }
        });
        calendar.render();
    }

    function loadReservations(date) {
        $.get(`<%=request.getContextPath()%>/schedule/searchreservation?date=\${date}&placeSeq=\${placeSeq}`, function (list) {
            console.log(list);
            const $tbody = $('#reservationTableBody').empty();

            if (!Array.isArray(list) || list.length === 0) {
                $tbody.append('<tr><td colspan="4" class="text-center">예약 없음</td></tr>');
                return;
            }

            for (let i = 0; i < list.length; i++) {
                const resv = list[i];
                console.log(resv);

                let confirmBtn = '';
                if (resv.requestConfirm === 'Y') {
                    confirmBtn = '<span class="badge bg-success">O</span>';
                } else if (resv.requestConfirm === 'N') {
                    confirmBtn = `
                    <div class="d-flex justify-content-center gap-1">
                        <button type="button" class="btn btn-outline-success btn-sm px-3 py-1 fw-bold">O</button>
                        <button type="button" class="btn btn-outline-danger btn-sm px-3 py-1 fw-bold">X</button>
                    </div>
                `;
                } else if (resv.requestConfirm === 'R') {
                    confirmBtn = '<span class="badge bg-danger">X</span>';
                }

                $tbody.append(`
                    <tr>
                        <td>\${resv.studyTitle}</td>
                        <td>\${resv.studyCurrCount}/\${resv.studyMemberLimit}</td>
                        <td>\${resv.studyContact || '-'}</td>
                        <td>\${confirmBtn}</td>
                    </tr>
                `);
            }
        });
    }
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>