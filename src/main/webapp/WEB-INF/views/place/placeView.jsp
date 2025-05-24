<%@ page import="com.gobookee.place.model.dto.PlaceViewResponse" %>
<%@ page import="com.gobookee.common.CommonPathTemplate" %>
<%@ page import="com.gobookee.common.enums.FileType" %>
<%@ page pageEncoding="utf-8" %>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=2d59386dd09d43d5d2ad8f433a1eb0e3&libraries=services"></script>
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
        지도 :
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
            console.log(<%=place.getPhotoNames()%>);
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