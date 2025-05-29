<%@ page language="java" pageEncoding="UTF-8"%>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=2d59386dd09d43d5d2ad8f433a1eb0e3&libraries=services"></script>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<main>
	<form action="<%=request.getContextPath()%>/study/upload" method="post" enctype="multipart/form-data">
	  <h2>스터디 등록 페이지</h2>
	
	  <!-- 파일 업로드 -->
	  <input type="file" name="studyImage" id="studyImage" accept="image/*" style="display:none;">
	  <label for="studyImage"><i class="bi bi-camera-fill"></i></label>
	  <div id="previewContainer"></div>
	  <br>
	
	  <!-- 텍스트 입력 -->
	  <input type="text" name="studyTitle" id="studyTitle" placeholder="스터디명을 입력해주세요" required><br>
	  <textarea name="studyContent" id="studyContent" placeholder="내용을 입력해주세요" required></textarea><br>
	
	  <!-- 인원 선택 -->
	  <label for="studyMemberLimit">참여 인원 수 선택</label><br>
	  <select id="studyMemberLimit" name="studyMemberLimit" required>
	    <option value="">인원을 선택하세요</option>
	    <option value="2">2명</option>
	    <option value="3">3명</option>
	    <option value="4">4명</option>
	    <option value="5">5명</option>
	    <option value="6">6명</option>
	    <option value="7">7명</option>
	    <option value="8">8명</option>
	    <option value="9">9명</option>
	    <option value="10">10명</option>
	    <option value="11">11명</option>
	    <option value="12">12명</option>
	  </select>
	  <br><br>
	  <!-- 카테고리 선택 -->
	  <label for="studyCategory">카테고리</label><br>
	  <select id="studyCategory" name="studyCategory" required>
	    <option value="">카테고리를 선택하세요</option>
	    <option value="중등">중등</option>
	    <option value="고등">고등</option>
	    <option value="대학">대학</option>
	    <option value="어학">어학</option>
	    <option value="자격증">자격증</option>
	    <option value="공무원">공무원</option>
	    <option value="한국사">한국사</option>
	    <option value="취업">취업</option>
	  </select>
	  <br><br>
	
	  <!-- 주소 및 좌표 -->
	  <input type="button" onclick="sample5_execDaumPostcode()" value="주소 검색"><br>
	  <input type="text" name="studyAddress" id="studyAddress" placeholder="주소" readonly>주소나 날짜를 입력하시면 장소연동을 할 수 없습니다.
	  <br><br>
	  <input type="hidden" name="studyLatitude" id="studyLatitude">
	  <input type="hidden" name="studyLongitude" id="studyLongitude">
	  <div id="map" style="width:300px; height:300px; display:none;"></div>
	
	  <!-- 사용자 ID -->
	  <input type="hidden" name="studyUserSeq" id="studyUserSeq" value="">
	  <br>
	  <!-- 날짜 선택 -->
	  <input type="date" name="studyDate" id="studyDate"><br><br>
	
	  <button type="submit">작성 완료</button>
	</form>

</main>
<script>
  document.getElementById("studyImage").addEventListener("change", function () {
    const preview = document.getElementById("previewContainer");
    preview.innerHTML = "";

    const files = this.files;

    if (files.length > 3) {
      alert("사진은 최대 3장까지만 업로드할 수 있습니다.");
      this.value = ""; // 파일 선택 초기화
      return;
    }

    for (let i = 0; i < files.length; i++) {
      const file = files[i];

      if (!file.type.startsWith("image/")) continue;

      const reader = new FileReader();
      reader.onload = function (e) {
        const img = document.createElement("img");
        img.src = e.target.result;
        img.style.width = "100px";
        img.style.height = "100px";
        img.style.objectFit = "cover";
        img.style.margin = "5px";
        img.style.border = "1px solid #ccc";
        img.style.borderRadius = "5px";
        preview.appendChild(img);
      };
      reader.readAsDataURL(file);
    }
  });
</script>


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
	                document.getElementById("studyAddress").value = addr;
	
	                geocoder.addressSearch(addr, function (results, status) {
	                    if (status === kakao.maps.services.Status.OK) {
	                        result = results[0];
	                        coords = new kakao.maps.LatLng(result.y, result.x);
							
	                        document.getElementById("studyLatitude").value = coords.getLat();
	                        document.getElementById("studyLongitude").value = coords.getLng();
	                        
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

</script>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>