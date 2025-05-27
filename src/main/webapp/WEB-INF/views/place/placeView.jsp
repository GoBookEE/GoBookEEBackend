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
    <h2><%= place.getPlaceTitle() %> 예약</h2>
    <div class="mb-3">
        <div>주소: <%=place.getPlaceAddress()%>
        </div>
        <div>제목: <%=place.getPlaceTitle()%>
        </div>
        <div>내용: <%=place.getPlaceContents()%>
        </div>
        <div>위도: <%=place.getPlaceLatitude()%>
        </div>
        <div>경도: <%=place.getPlaceLongitude()%>
        </div>
        <div id="map" style="width:50%; height:400px;"></div>
        <div>유저 닉네임: <%=place.getUserNickname()%>
        </div>
        <div>유저 속도: <%=place.getUserSpeed()%>
        </div>
        <div>추천: <%=place.getPlaceRecCount()%> / 비추천: <%=place.getPlaceNonRecCount()%>
        </div>
        <% if (place.getPhotoNames() != null && !place.getPhotoNames().isEmpty()) {
            for (String photoName : place.getPhotoNames()) { %>
        <div><img src="<%=CommonPathTemplate.getUploadPath(request, FileType.PLACE, photoName)%>" alt=""/></div>
        <% }
        } %>
    </div>
    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#reservationModal">예약하기</button>

    <!-- 예약 모달 -->
    <div class="modal fade" id="reservationModal" tabindex="-1">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">예약 정보</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <% if (isOwner) { %>
                    <!-- 사장용 -->
                    <div id="ownerCalendar"></div>
                    <h6 class="mt-3" id="selectedDateTitle">예약 현황</h6>
                    <table class="table">
                        <thead>
                        <tr>
                            <th>스터디명</th>
                            <th>인원</th>
                            <th>전화번호</th>
                            <th>확정</th>
                        </tr>
                        </thead>
                        <tbody id="reservationTableBody"></tbody>
                    </table>
                    <% } else { %>
                    <!-- 일반 유저용 -->
                    <div id="step-group">
                        <h6>스터디 그룹 선택</h6>
                        <ul id="groupList" class="list-group"></ul>
                    </div>
                    <div id="step-calendar" style="display:none">
                        <h6>예약 날짜 선택</h6>
                        <div id="userCalendar"></div>
                        <button class="btn btn-link" onclick="goBackToGroup()">이전</button>
                    </div>
                    <div id="step-confirm" style="display:none">
                        <form method="post" action="<%=request.getContextPath()%>/schedule/insert">
                            <input type="hidden" name="placeSeq" value="<%=place.getPlaceSeq()%>">
                            <input type="hidden" id="formStudySeq" name="studySeq">
                            <input type="hidden" id="formDate" name="date">
                            <div id="confirmInfo" class="my-2"></div>
                            <button type="submit" class="btn btn-primary">예약하기</button>
                        </form>
                        <button class="btn btn-link" onclick="goBackToDate()">이전</button>
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
                const confirmBtn = resv.requestConfirm === 'Y' ? 'O'
                    : resv.requestConfirm === 'N' ? 'X'
                        : `<button class="btn btn-sm btn-success">O</button> <button class="btn btn-sm btn-danger">X</button>`;

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
