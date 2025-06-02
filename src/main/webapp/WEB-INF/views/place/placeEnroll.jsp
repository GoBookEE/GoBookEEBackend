<%@ page import="com.gobookee.users.model.dto.User" %>
<%@ page pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=2d59386dd09d43d5d2ad8f433a1eb0e3&libraries=services"></script>
<style>
    header, footer {
        display: none !important;
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
        <h5 class="fw-bold">장소 등록하기</h5>
        <div></div>
    </div>

    <div class="form-box">
        <div class="mb-3 text-center">
            <label for="placeImage" class="form-label">
                <i class="bi bi-camera fs-1 text-success"></i><br>
                <span id="photoCount" class="text-muted small">0/10</span>
            </label>
            <input class="form-control d-none" type="file" id="placeImage" name="placeImage" multiple accept="image/*">
            <div id="previewContainer" class="d-flex mt-2"></div>
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">장소를 등록해 주세요</label>
            <input type="text" id="placeTitle" class="form-control mb-2" placeholder="장소 이름을 입력해 주세요 (20자 이내)"
                   maxlength="20">
            <textarea id="placeContent" class="form-control" rows="5" placeholder="내용을 입력해 주세요"></textarea>
        </div>

        <div class="mb-3">
            <label class="form-label fw-semibold">주소 입력</label>
            <input type="text" id="placeAddress" class="form-control mb-2" placeholder="주소를 검색해보세요" readonly>
            <button type="button" class="btn btn-outline-secondary w-100" onclick="sample5_execDaumPostcode()">주소 검색
            </button>
            <div id="map" class="map-container mt-2"></div>
        </div>

        <input type="hidden" id="placeUserSeq" value="<%=loginUser!=null?loginUser.getUserSeq():""%>">
        <button class="btn btn-dark w-100 mt-3" onclick="saveImageFiles()">
            <i class="bi bi-pencil me-1"></i> 등록하기
        </button>
    </div>
</main>

<script>
    const mapContainer = document.getElementById('map');
    let map, marker, coords, result;
    const geocoder = new kakao.maps.services.Geocoder();

    function sample5_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function (data) {
                const addr = data.address;
                document.getElementById("placeAddress").value = addr;
                geocoder.addressSearch(addr, function (results, status) {
                    if (status === kakao.maps.services.Status.OK) {
                        result = results[0];
                        coords = new kakao.maps.LatLng(result.y, result.x);
                        mapContainer.style.display = "block";
                        map = new kakao.maps.Map(mapContainer, {
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
            }
        }).open();
    }

    function saveImageFiles() {
        if (!$('#placeAddress').val()) {
            alert("주소를 입력해주세요.");
            return;
        }

        if (!coords) {
            alert("주소 검색을 통해 지도를 확인해주세요.");
            return;
        }

        const formData = new FormData();
        const files = $('#placeImage')[0].files;

        for (let i = 0; i < files.length; i++) {
            formData.append("placeFile" + i, files[i]);
        }
        formData.append("placeTitle", $('#placeTitle').val());
        formData.append("placeContent", $('#placeContent').val());

        formData.append("placeAddress", $('#placeAddress').val());
        if (coords) {
            formData.append("placeLatitude", coords.getLat());
            formData.append("placeLongitude", coords.getLng());
        }
        formData.append("placeUserSeq", $('#placeUserSeq').val());

        $.ajax({
            url: "<%=request.getContextPath()%>/place/insert",
            type: "post",
            processData: false,
            contentType: false,
            data: formData,
            success: data => {
                const parseData = JSON.parse(data);
                alert(parseData ? "장소 등록에 성공했습니다!" : "장소 등록에 실패했습니다!");
                location.replace("<%=request.getContextPath()%>/place/listpage");
            },
            error: () => {
                alert("오류발생! 장소 등록에 실패했습니다!");
                location.replace("<%=request.getContextPath()%>/place/listpage");
            }
        });
    }

    $('#placeImage').change(e => {
        const files = e.target.files;
        $('#previewContainer').html("");

        // 👉 이미지 수 업데이트
        $('#photoCount').text(`\${files.length}/10`);

        $.each(files, (i, file) => {
            const fileReader = new FileReader();
            fileReader.readAsDataURL(file);
            fileReader.onload = e => {
                const path = e.target.result;
                const $img = $('<img>').attr({
                    'src': path,
                    'width': '200',
                    'height': '200'
                });
                $('#previewContainer').append($img);
            };
        });
    });
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>