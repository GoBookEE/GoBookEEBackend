<%@ page import="com.gobookee.users.model.dto.User, java.util.List" %>
<%@ page import="com.gobookee.common.enums.FileType" %>
<%@ page import="com.gobookee.common.CommonPathTemplate" %>
<%@ page import="com.gobookee.place.model.dto.PlaceViewResponse" %>
<%@ page pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=2d59386dd09d43d5d2ad8f433a1eb0e3&libraries=services"></script>
<%
    User loginUser = (User) request.getSession().getAttribute("loginUser");
    PlaceViewResponse place = (PlaceViewResponse) request.getAttribute("place");
%>
<style>
    body {
        padding-bottom: 80px;
    }

    .form-container {
        max-width: 500px;
        margin: auto;
        padding: 30px 20px;
    }

    .form-box {
        border-radius: 20px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        background-color: #fff;
        padding: 25px;
    }

    .preview-image {
        width: 60px;
        height: 60px;
        object-fit: cover;
        margin-right: 5px;
    }

    .map-container {
        display: none;
        width: 100%;
        height: 250px;
        border-radius: 12px;
        margin-top: 10px;
    }
</style>

<main class="form-container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <button class="btn btn-link text-dark fs-4" onclick="history.back()">
            <i class="bi bi-x-lg"></i>
        </button>
        <h5 class="fw-bold">장소 수정하기</h5>
        <div></div>
    </div>

    <div class="form-box">
        <div class="mb-3 text-center">
            <label for="placeImage" class="form-label">
                <i class="bi bi-camera fs-1 text-success"></i><br>
                <span id="photoCount"
                      class="text-muted small"><%= place.getPhotoNames() != null ? place.getPhotoNames().size() : 0 %>/10</span>
            </label>
            <input class="form-control d-none" type="file" id="placeImage" name="placeImage" multiple accept="image/*">
            <div id="previewContainer" class="d-flex mt-2">
                <% if (place.getPhotoNames() != null) {
                    for (String photoName : place.getPhotoNames()) { %>
                <img src="<%=CommonPathTemplate.getUploadPath(request, FileType.PLACE, photoName)%>"
                     class="preview-image">
                <% }
                } %>
            </div>
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">장소를 수정해 주세요</label>
            <input type="text" id="placeTitle" class="form-control mb-2" value="<%=place.getPlaceTitle()%>"
                   maxlength="20">
            <textarea id="placeContent" class="form-control" rows="5"><%=place.getPlaceContents()%></textarea>
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">주소 입력</label>
            <input type="text" id="placeAddress" class="form-control mb-2" value="<%=place.getPlaceAddress()%>"
                   readonly>
            <button type="button" class="btn btn-outline-secondary w-100" onclick="sample5_execDaumPostcode()">주소 검색
            </button>
            <div id="map" class="map-container mt-2"></div>
        </div>

        <input type="hidden" id="placeLatitude" value="<%=place.getPlaceLatitude()%>">
        <input type="hidden" id="placeLongitude" value="<%=place.getPlaceLongitude()%>">
        <input type="hidden" id="placeSeq" value="<%=place.getPlaceSeq()%>">
        <input type="hidden" id="placeUserSeq" value="<%=loginUser != null ? loginUser.getUserSeq() : ""%>">

        <button class="btn btn-dark w-100 mt-3" onclick="updatePlace()">
            <i class="bi bi-pencil me-1"></i> 수정하기
        </button>
    </div>
</main>

<script>
    const initialLat = <%= place.getPlaceLatitude() %>;
    const initialLng = <%= place.getPlaceLongitude() %>;
    $(document).ready(function () {
        // 지도 초기 렌더링
        if (initialLat && initialLng) {
            coords = new kakao.maps.LatLng(initialLat, initialLng);
            $('#map').show();
            map = new kakao.maps.Map(document.getElementById('map'), {
                center: coords,
                level: 5
            });
            marker = new kakao.maps.Marker({
                position: coords,
                map: map
            });
            map.setCenter(coords);
        }
    });

    const mapContainer = document.getElementById('map');
    let map, marker, coords;
    const geocoder = new kakao.maps.services.Geocoder();

    function sample5_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function (data) {
                const addr = data.address;
                document.getElementById("placeAddress").value = addr;
                geocoder.addressSearch(addr, function (results, status) {
                    if (status === kakao.maps.services.Status.OK) {
                        coords = new kakao.maps.LatLng(results[0].y, results[0].x);
                        mapContainer.style.display = "block";
                        map = new kakao.maps.Map(mapContainer, {center: coords, level: 5});
                        marker = new kakao.maps.Marker({position: coords, map: map});
                        map.setCenter(coords);
                    }
                });
            }
        }).open();
    }

    function updatePlace() {
        const formData = new FormData();
        const files = $('#placeImage')[0].files;

        for (let i = 0; i < files.length; i++) {
            formData.append("placeFile" + i, files[i]);
        }
        formData.append("placeSeq", $('#placeSeq').val());
        formData.append("placeTitle", $('#placeTitle').val());
        formData.append("placeContent", $('#placeContent').val());
        formData.append("placeAddress", $('#placeAddress').val());
        formData.append("placeLatitude", coords ? coords.getLat() : $('#placeLatitude').val());
        formData.append("placeLongitude", coords ? coords.getLng() : $('#placeLongitude').val());
        formData.append("placeUserSeq", $('#placeUserSeq').val());

        $.ajax({
            url: "<%=request.getContextPath()%>/place/update",
            type: "post",
            processData: false,
            contentType: false,
            data: formData,
            success: data => {
                const result = JSON.parse(data);
                alert(result ? "장소 수정 성공!" : "장소 수정 실패!");
                location.replace("<%=request.getContextPath()%>/place/view?placeSeq=<%=place.getPlaceSeq()%>");
            },
            error: () => {
                alert("오류 발생! 장소 수정 실패!");
                location.replace("<%=request.getContextPath()%>/place/view?placeSeq=<%=place.getPlaceSeq()%>");
            }
        });
    }

    $('#placeImage').change(e => {
        const files = e.target.files;
        $('#previewContainer').html("");
        $('#photoCount').text(`${files.length}/10`);

        $.each(files, (i, file) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = e => {
                const $img = $('<img>').attr('src', e.target.result).addClass('preview-image');
                $('#previewContainer').append($img);
            }
        });
    });
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>