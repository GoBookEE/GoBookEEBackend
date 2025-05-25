<%@ page import="com.gobookee.place.model.dto.PlaceViewResponse" %>
<%@ page import="com.gobookee.common.CommonPathTemplate" %>
<%@ page import="com.gobookee.common.enums.FileType" %>
<%@ page pageEncoding="utf-8" %>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=2d59386dd09d43d5d2ad8f433a1eb0e3&libraries=services"></script>
<!-- 부트스트랩 CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>

<!-- FullCalendar CSS -->
<link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.css" rel="stylesheet"/>
<style>
    #calendar {
        max-width: 100%;
        margin: 0 auto;
    }
</style>
<%
    PlaceViewResponse place = (PlaceViewResponse) request.getAttribute("place");
%>
<main>
    <h2>장소 상세 페이지</h2>
    <%
        if (place != null) {
    %>
    <div>
        주소 : <%=place.getPlaceAddress()%>
    </div>
    <div>
        제목 : <%=place.getPlaceTitle()%>
    </div>
    <div>
        내용 : <%=place.getPlaceContents()%>
    </div>
    <div>
        위도 : <%=place.getPlaceLatitude()%>
    </div>
    <div>
        경도 : <%=place.getPlaceLongitude()%>
    </div>
    <div>
        <div id="map" style="width:100%;height:400px;"></div>
    </div>
    <div>
        유저닉네임 : <%=place.getUserNickname()%>
    </div>
    <div>
        유저속도 : <%=place.getUserSpeed()%>
    </div>
    <div>
        추천 : <%=place.getPlaceRecCount()%>
    </div>
    <div>
        비추천 : <%=place.getPlaceNonRecCount()%>
    </div>
    <div>
        <%
            if (place.getPhotoNames() != null && !place.getPhotoNames().isEmpty()) {
                for (String photoName : place.getPhotoNames()) {
        %>
        <script>
        </script>
        <div>
            <img src="<%=CommonPathTemplate.getUploadPath(request,FileType.PLACE,photoName)%>"
                 alt="">
        </div>
        <%
                }
            }
        %>
    </div>
    <%
        }
    %>


    <!-- 예약하기 버튼 -->
    <button
            type="button"
            class="btn btn-primary"
            data-bs-toggle="modal"
            data-bs-target="#reserveModal">
        예약하기
    </button>

    <!-- 모달 -->
    <div class="modal fade" id="reserveModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">

                <!-- 헤더 -->
                <div class="modal-header">
                    <h5 class="modal-title" id="modalTitle">스터디 그룹 선택</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- 바디 -->
                <div class="modal-body">
                    <!-- Step 1: 그룹 선택 -->
                    <div id="step-group-select">
                        <ul id="groupList" class="list-group"></ul>
                    </div>

                    <!-- Step 2: 캘린더 -->
                    <div id="step-calendar" class="d-none">
                        <div id="calendar"></div>
                        <p id="reservationCountMsg" class="mt-2"></p>
                        <button id="toReserveForm" class="btn btn-primary mt-3" disabled>예약 계속하기</button>
                        <button id="backToGroupSelect" class="btn btn-secondary mt-3 ms-2">그룹 선택으로 돌아가기</button>
                    </div>

                    <!-- Step 3: 예약 입력 -->
                    <div id="step-reserve-form" class="d-none">
                        <form id="reserveForm" method="POST" action="<%=request.getContextPath()%>/schedule/insert">
                            <input type="hidden" name="groupId" id="formGroupId">
                            <input type="hidden" name="placeSeq" id="formPlaceSeq">
                            <input type="hidden" name="date" id="formDate">
                            <p><strong id="selectedGroupName"></strong> 그룹으로 예약합니다.</p>
                            <p>예약 날짜: <span id="selectedDateDisplay"></span></p>
                            <button type="submit" class="btn btn-success w-100">예약하기</button>
                            <button type="button" id="backToCalendar" class="btn btn-secondary mt-2 w-100">날짜 선택으로
                                돌아가기
                            </button>
                        </form>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <!-- 부트스트랩 JS + Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- FullCalendar JS -->
    <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>

    <script>
        // 전역 상태 변수
        let selectedGroup = null;
        let selectedDateForReserve = null;
        let calendar = null;
        const placeSeq = 42; // 예약할 게시글 ID (실제론 상세페이지에서 받아와야 함)

        document.getElementById('reserveModal').addEventListener('show.bs.modal', async () => {
            resetModalToGroupSelect();

            try {
                const groups = await fetchMyStudyGroupsFromServer(); // [{studySeq, studyTitle}, ...]

                const groupList = document.getElementById('groupList');
                groupList.innerHTML = '';

                groups.forEach(group => {
                    const li = document.createElement('li');
                    li.className = 'list-group-item list-group-item-action';
                    li.textContent = group.studyTitle;
                    li.style.cursor = 'pointer';

                    // 클릭 시 선택
                    li.addEventListener('click', () => onGroupSelected({
                        id: group.studySeq,
                        name: group.studyTitle
                    }));

                    groupList.appendChild(li);
                });
            } catch (err) {
                alert('그룹 목록 불러오기 실패');
                console.error(err);
            }
        });

        // 그룹 선택 처리 함수
        function onGroupSelected(group) {
            selectedGroup = group;
            switchToCalendarStep();
        }

        // 1단계 UI 초기화
        function resetModalToGroupSelect() {
            selectedGroup = null;
            selectedDateForReserve = null;

            document.getElementById('modalTitle').textContent = '스터디 그룹 선택';
            document.getElementById('step-group-select').classList.remove('d-none');
            document.getElementById('step-calendar').classList.add('d-none');
            document.getElementById('step-reserve-form').classList.add('d-none');
        }

        // 그룹 선택 후 캘린더 단계로 전환
        function switchToCalendarStep() {
            document.getElementById('modalTitle').textContent = '예약 날짜 선택';
            document.getElementById('step-group-select').classList.add('d-none');
            document.getElementById('step-calendar').classList.remove('d-none');
            document.getElementById('step-reserve-form').classList.add('d-none');

            // 캘린더 초기화 혹은 재설정
            if (calendar) {
                calendar.destroy();
            }

            const calendarEl = document.getElementById('calendar');
            calendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'dayGridMonth',
                selectable: true,
                dateClick: async function (info) {
                    selectedDateForReserve = info.dateStr;
                    document.getElementById('reservationCountMsg').textContent = '예약 현황 불러오는 중...';
                    document.getElementById('toReserveForm').disabled = true;

                    try {
                        // 실제 API로 바꾸세요
                        const data = await fetchReservationCount(selectedDateForReserve, placeSeq);

                        // 날짜 문자열 YYYY-MM-DD → 몇월/몇일 변환
                        const dateObj = new Date(selectedDateForReserve);
                        const month = dateObj.getMonth() + 1; // 0부터 시작해서 +1
                        const day = dateObj.getDate();

                        document.getElementById('reservationCountMsg').textContent =
                            `${month}월 ${day}일에는 ${data.count}팀이 예약했습니다.`;
                        document.getElementById('toReserveForm').disabled = false;
                    } catch (err) {
                        document.getElementById('reservationCountMsg').textContent = '예약 현황 불러오기 실패';
                        console.error(err);
                    }
                }
            });
            calendar.render();

            // 초기 메시지 초기화
            document.getElementById('reservationCountMsg').textContent = '날짜를 선택하세요.';
            document.getElementById('toReserveForm').disabled = true;
        }

        // 캘린더 -> 예약 입력폼 전환 시 날짜 표시 업데이트
        document.getElementById('toReserveForm').addEventListener('click', () => {
            if (!selectedDateForReserve || !selectedGroup) {
                alert('날짜와 그룹을 선택해주세요.');
                return;
            }
            document.getElementById('modalTitle').textContent = '예약 정보 입력';
            document.getElementById('step-group-select').classList.add('d-none');
            document.getElementById('step-calendar').classList.add('d-none');
            document.getElementById('step-reserve-form').classList.remove('d-none');

            document.getElementById('selectedGroupName').textContent = selectedGroup.name;
            document.getElementById('selectedDateDisplay').textContent = selectedDateForReserve;

            // <form> 안에 값 세팅
            document.getElementById('formGroupId').value = selectedGroup.id;
            document.getElementById('formPlaceSeq').value = placeSeq;
            document.getElementById('formDate').value = selectedDateForReserve;
        });

        // 뒤로 가기 버튼 (캘린더 -> 그룹 선택)
        document.getElementById('backToGroupSelect').addEventListener('click', () => {
            resetModalToGroupSelect();
        });

        // 뒤로 가기 버튼 (예약폼 -> 캘린더)
        document.getElementById('backToCalendar').addEventListener('click', () => {
            switchToCalendarStep();
        });

        async function fetchMyStudyGroupsFromServer() {
            const response = await fetch('<%=request.getContextPath()%>/study/searchstudy');
            console.log(response);
            if (!response.ok) {
                throw new Error('스터디 그룹 목록 조회 실패');
            }
            return await response.json(); // List<SearchStudyResponse>
        }

        async function fetchReservationCount(date, placeSeq) {
            const query = new URLSearchParams({date, placeSeq}).toString();
            const response = await fetch(`<%=request.getContextPath()%>/study/searchstudycount?${query}`);

            if (!response.ok) {
                throw new Error('예약 수 조회 실패');
            }
            return await response.json(); // { count: number }
        }

        async function mockPostReserve(data) {
            return new Promise((resolve, reject) => {
                setTimeout(() => {
                    if (data.groupId && data.placeSeq && data.date) {
                        resolve({success: true});
                    } else {
                        reject(new Error('잘못된 데이터'));
                    }
                }, 700);
            });
        }
    </script>
</main>
<script>
    // 위도와 경도 값
    var latitude =
    <%=place.getPlaceLatitude()%>
    var longitude =
    <%=place.getPlaceLongitude()%>

    // 지도 옵션 설정
    var mapContainer = document.getElementById('map'); // 지도를 표시할 div
    var mapOption = {
        center: new kakao.maps.LatLng(latitude, longitude), // 중심좌표
        level: 3 // 확대 수준 (1~14, 작을수록 확대됨)
    };

    // 지도 생성
    var map = new kakao.maps.Map(mapContainer, mapOption);

    // 마커 생성
    var markerPosition = new kakao.maps.LatLng(latitude, longitude);
    var marker = new kakao.maps.Marker({
        position: markerPosition
    });
    marker.setMap(map);
</script>
<%@include file="/WEB-INF/views/common/footer.jsp" %>