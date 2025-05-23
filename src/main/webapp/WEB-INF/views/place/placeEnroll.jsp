<%@ page pageEncoding="utf-8" %>
<%--  카카오 주소 api  --%>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=2d59386dd09d43d5d2ad8f433a1eb0e3&libraries=services"></script>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<main>
    <h2>매장 등록 페이지</h2>
    <input type="text" placeholder="매장명" id="placeTitle" name="placeTitle" required> <br>
    <textarea name="placeContent" id="placeContent" required>내용</textarea> <br>
    <input type="file" id="placeImage" name="placeImage" multiple accept="image/*"> <br>
    <%--    사진 업로드 시 div영역에 preview 이미지 띄워주기!!     --%>
    <div id="previewContainer"></div>
    <br>

    <input type="text" id="placeAddress" placeholder="주소"> <br>
    <input type="button" onclick="sample5_execDaumPostcode()" value="주소 검색"><br>
    <div id="map" style="width:300px;height:300px;margin-top:10px;display:none"></div>

    <%--  작성자는 input hidden으로 아이디 넘겨주기  --%>
    <%--                <input type="hidden" id="placeUserSeq" value="<%=loginMember.getUserId()%>">--%>
    <input type="hidden" id="placeUserSeq" value="1">
    <button onclick="saveImageFiles();">매장 등록</button>
</main>
<script>
    //카카오 주소 및 지도 api 관련 코드
    const mapContainer = document.getElementById('map');
    let map;
    let marker;
    let coords, result;
    const geocoder = new kakao.maps.services.Geocoder();

    function sample5_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function (data) {
                try {
                    var addr = data.address;
                    document.getElementById("placeAddress").value = addr;

                    geocoder.addressSearch(addr, function (results, status) {
                        if (status === kakao.maps.services.Status.OK) {
                            result = results[0];
                            coords = new kakao.maps.LatLng(result.y, result.x);

                            // 지도를 보여주기 전에 display 해제
                            mapContainer.style.display = "block";

                            // 지도 객체 재생성 (권장)
                            map = new kakao.maps.Map(mapContainer, {
                                center: coords,
                                level: 5
                            });

                            marker = new kakao.maps.Marker({
                                position: coords,
                                map: map
                            });

                            map.setCenter(coords);
                        } else {
                            console.warn("주소를 좌표로 변환하지 못했습니다.");
                        }
                    });
                } catch (e) {
                    console.error("주소 검색 후 오류 발생:", e);
                }
            }
        }).open();
    }


    const saveImageFiles = () => {
        const formData = new FormData();
        const files = $('#placeImage')[0].files;

        for (let i = 0; i < files.length; i++) {
            formData.append("placeFile" + i, files[i]);
        }

        formData.append("placeTitle", $('#placeTitle').val());
        formData.append("placeContent", $('#placeContent').val());
        formData.append("placeAddress", $('#placeAddress').val());
        if (coords) {
            formData.append("placeLatitude", coords.getLat())
            formData.append("placeLongitude", coords.getLng())
        }
        formData.append("placeUserSeq", $('#placeUserSeq').val())

        $.ajax({
            url: "<%=request.getContextPath()%>/place/insert",
            type: "post",
            processData: false,
            contentType: false,
            data: formData,
            success: data => {
                let parseData = JSON.parse(data);
                if (parseData == true) {
                    alert("장소 등록에 성공했습니다!");
                    location.replace("<%=request.getContextPath()%>/place/listpage")
                } else {
                    alert("장소 등록에 실패했습니다!");
                    location.replace("<%=request.getContextPath()%>/place/listpage")
                }
            },
            error: () => {
                alert("오류발생! 장소 등록에 실패했습니다!");
                location.replace("<%=request.getContextPath()%>/place/listpage")
            }
        });
    }

    $('#placeImage').change(e => {
        $('#previewContainer').html("");
        $.each(e.target.files, (i, file) => {
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
<%@include file="/WEB-INF/views/common/footer.jsp" %>