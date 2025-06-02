<%@ page language="java" pageEncoding="UTF-8"%>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=2d59386dd09d43d5d2ad8f433a1eb0e3&libraries=services"></script>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<<script >
	$(document).ready(function () {
	    $("header").html(`
	        <div style="height: 4rem"class="container d-flex justify-content-between align-items-center text-center small position-relative">
	            <a class="col-1" style="color:black" href="<%=request.getContextPath()%>/study/listpage">
	                <i class="bi bi-x fs-1"></i>
	            </a>
	        </div>
	    `);
	});
</script>

<main>
	<form action="<%=request.getContextPath()%>/study/upload" method="post" enctype="multipart/form-data">
	  <h2>스터디 등록 페이지</h2>
	
	  <!-- 파일 업로드 -->
<!-- 파일 선택 숨김 -->
<input type="file" name="studyImage" id="studyImage" accept="image/*" style="display: none;">

<!-- 클릭 가능한 카메라 버튼 -->
<label for="studyImage" id="cameraButton">
  <div class="camera-card">
    <i class="bi bi-camera-fill camera-icon"></i>
  </div>
</label>

<!-- 미리보기 영역 -->
<div id="previewContainer" class="mt-3"></div>

	  <br>
	
	  <!-- 텍스트 입력 -->
<div class="container">
  <div class="study-title">스터디 그룹을 만들어 주세요</div>

  <div class="study-card">
    <!-- 스터디 제목 입력 -->
    <input type="text" id="studyTitle" name="studyTitle"
           class="form-control study-input mb-4"
           placeholder="스터디 명을 입력해 주세요 (20자 이내)"
           maxlength="20" required>

    <!-- 내용 입력 -->
    <textarea id="studyContent" name="studyContent"
              class="form-control study-textarea"
              placeholder="내용을 입력해 주세요" required></textarea>
  </div>
</div>
	
	  <!-- 인원 선택 -->
<!-- Bootstrap CSS -->


<div class="d-flex align-items-start">
  <!-- 인원 선택 -->
  <div class="me-4 text-center" style="width: 120px;">
    <label for="studyMemberLimit" class="form-label fw-bold w-100">인원 입력</label>
    <select class="form-select text-center" id="studyMemberLimit" name="studyMemberLimit" required
            style="background-color: #4CAF50; color: white; border-radius: 999px; padding-right: 2rem;">
      <option value="">인원</option>
      <option value="1">1명</option>
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
  </div>

  <!-- 카테고리 선택 -->
  <div class="text-center" style="width: 120px;">
    <label for="studyCategory" class="form-label fw-bold w-100">카테고리 입력</label>
    <select class="form-select text-center" id="studyCategory" name="studyCategory" required
            style="background-color: #4CAF50; color: white; border-radius: 999px; padding-right: 2rem;">
      <option value="">카테고리</option>
      <option value="중등">중등</option>
      <option value="고등">고등</option>
      <option value="대학">대학</option>
      <option value="어학">어학</option>
      <option value="자격증">자격증</option>
      <option value="공무원">공무원</option>
      <option value="한국사">한국사</option>
      <option value="취업">취업</option>
    </select>
  </div>
</div>





	  <br><br>
	
		<!-- 📍 스터디 장소 입력 (주소 검색 API 호출 포함) -->
		<div class="mb-4">
		  <label for="studyAddress" class="form-label-bold">스터디 장소 입력</label>
		  <div class="rounded-input-wrapper" onclick="sample5_execDaumPostcode()">
		    <i class="bi bi-search"></i>
		    <input type="text" id="studyAddress" name="studyAddress" placeholder="스터디 장소를 검색해보세요" readonly>
		  </div>
		</div>
		
		<input type="hidden" name="studyLatitude" id="studyLatitude">
		<input type="hidden" name="studyLongitude" id="studyLongitude">
		<div id="map" style="width:300px; height:300px; display:none;"></div>
		
		<!-- 👤 사용자 ID -->
		<input type="hidden" name="studyUserSeq" id="studyUserSeq" value="">
		
		<!-- 📅 날짜 입력 -->
		<div class="mb-4">
		  <label for="studyDate" class="form-label-bold">날짜 입력</label>
		  <div class="rounded-input-wrapper">
		    <i class="bi bi-calendar-event"></i>
		    <input type="date" id="studyDate" name="studyDate" placeholder="시작날짜">
		  </div>
		</div>
	
		<div class="d-grid gap-2">
        <button type="submit" class="btn btn-outline-dark" style="border-radius: 30px; margin-bottom: 10px;">
            등록하기
        </button>
    	</div>
	</form>

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

	document.addEventListener("DOMContentLoaded", function () {
		  const input = document.getElementById("studyImage");
		  const preview = document.getElementById("previewContainer");
		  const cameraButton = document.getElementById("cameraButton");

		  input.addEventListener("change", function () {
		    if (input.files.length > 1) {
		      alert("사진은 1장만 선택할 수 있습니다.");
		      input.value = "";
		      return;
		    }

		    preview.innerHTML = "";
		    cameraButton.style.display = "none";

		    const file = input.files[0];
		    if (!file || !file.type.startsWith("image/")) return;

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
		      img.style.cursor = "pointer";

		      // ✅ 미리보기 이미지 클릭하면 파일 선택 다시 열기
		      img.addEventListener("click", () => input.click());

		      preview.appendChild(img);
		    };
		    reader.readAsDataURL(file);
		  });
		});



</script>
<style>
/* ✅ select 전용 스타일 */
.study-select-style {
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  background-color: #4CAF50;
  color: white;
  padding: 0.375rem 2.5rem 0.375rem 1rem;
  border: none;
  border-radius: 999px;
  background-image: url("data:image/svg+xml;charset=UTF-8,<svg xmlns='http://www.w3.org/2000/svg' fill='white' viewBox='0 0 16 16'><path d='M1.5 5.5l6 6 6-6'/></svg>");
  background-repeat: no-repeat;
  background-position: right 1rem center;
  background-size: 12px;
  font-weight: 500;
}

/* ✅ 텍스트 카드 스타일 */
.study-card {
  background-color: #fff;
  border-radius: 20px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.05);
  padding: 2rem;
  max-width: 640px;
  margin: 2rem auto;
}

.study-title {
  font-weight: 600;
  font-size: 1.25rem;
  margin-bottom: 1.5rem;
}

.study-input {
  border: none;
  border-bottom: 1px solid #ddd;
  border-radius: 0;
  padding: 0.5rem 0;
  font-size: 1rem;
  color: #333;
  box-shadow: none;
}

.study-input::placeholder {
  color: #bbb;
}

.study-input:focus {
  border-color: #aaa;
  box-shadow: none;
}

.study-textarea {
  border: none;
  resize: none;
  padding: 0.75rem 0 0 0;
  font-size: 1rem;
  height: 180px;
  color: #333;
  box-shadow: none;
}

.study-textarea::placeholder {
  color: #bbb;
}

.study-textarea:focus {
  outline: none;
  box-shadow: none;
}

/* ✅ 카메라 카드 스타일 */
.camera-card {
  width: 100px;
  height: 100px;
  background-color: #fff;
  border-radius: 20px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: box-shadow 0.2s ease;
}

.camera-card:hover {
  box-shadow: 0 10px 24px rgba(0, 0, 0, 0.12);
}

.camera-icon {
  font-size: 2rem;
  color: #4CAF50;
}

/* ✅ 핵심: 기본 input 파일 선택 버튼 숨기기 */
#studyImage {
  display: none;
}


/*  */

  .form-label-bold {
    font-weight: bold;
    margin-bottom: 0.5rem;
  }

  .rounded-input-wrapper {
    display: flex;
    align-items: center;
    background-color: #fff;
    border-radius: 20px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    padding: 0.75rem 1rem;
    margin-bottom: 1.5rem;
  }

  .rounded-input-wrapper i {
    font-size: 1.2rem;
    color: #ccc;
    margin-right: 0.5rem;
  }

  .rounded-input-wrapper input {
    border: none;
    outline: none;
    flex: 1;
    font-size: 1rem;
    color: #333;
    background: transparent;
  }

  .rounded-input-wrapper input::placeholder {
    color: #bbb;
  }
  
  .submit-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #1e1e1e;
    color: #fff;
    border: none;
    border-radius: 999px;
    padding: 0.3rem 1.5rem; /* ✅ 아주 얇게 */
    font-size: 1rem;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.2s ease;
    width: 100%;
    max-width: 600px;
    margin: 2rem auto 0;
  }

  .submit-btn i {
    margin-right: 0.5rem;
  }

  .submit-btn:hover {
    background-color: #333;
  }
</style>

</html>