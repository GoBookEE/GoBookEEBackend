<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<main>
<div class="tab-container">
  <button class="tab-button active" disabled>
    진행 예정 <span class="tab-count"></span>
  </button>
  <button class="tab-button " onclick="location.assign('<%=request.getContextPath()%>/studygroup/listpage/end')">
    진행 완료 <span class="tab-count"></span>
  </button>
</div>
	<div class="d-inline-block" style="padding-top: 20px; padding-left: 1rem;">
	    <select id="studygroupsortby" class="form-select study-select-style">
	      <option value="latest">최신순</option>
	      <option value="recommend">추천순</option>
	    </select>
  </div>
	<div id="studyGroupListContainer"></div>
	<div id="studyGroupPageBar"></div>
</main>
<script>
	let select="latest"
	
	$(document).ready(function (){
		studygroupslist("latest");
		$("#studygroupsortby").on("change",function(){
			select = $(this).val();
			studygroupslist(select, 1);
		});
	});
	
	$(document).on("click", "#studyGroupPageBar a.go-page-link", function (e) {
		 e.preventDefault();
		 const page = $(this).data("page");
		 if (page) {
			 studygroupslist(select, page);
		 }
		});
	
	function studygroupslist(sortType, cPage = 1){
		$.ajax({
			url : "<%=request.getContextPath()%>/studygroup/studygrouplist",
			type : "GET",
			data : {sort: sortType, cPage : cPage},
			dataType : "json",
			success : function(response){
				const container = $("#studyGroupListContainer");
				const pageBarDiv = $("#studyGroupPageBar");
				container.empty();
				pageBarDiv.empty();
				
				if(response.error){
					alert(response.error);
					location.href = "<%=request.getContextPath()%>/loginpage";
					return;
				}
				
				const studygroups = response.studygroups;
				const pageBar = response.pageBar;
				
				if(!studygroups||studygroups.length == 0){
					container.append("<div>등록된 스터디가 없습니다</div>");
				}else {
					studygroups.forEach(function(b){
						const itemHtml = `
					        <div class="p-4 book-card" onclick="location.assign('<%=request.getContextPath()%>/study/view?seq=\${b.studySeq}')">
					            <div class="row book-card-row">
					            <div class="book-card-img col col-5" style="height: 180px; overflow: hidden; display: flex; align-items: center; justify-content: center;">
					            <img src="<%=request.getContextPath()%>/resources/upload/study/\${b.photoRenamedName}" 
					                 onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png';"
					                 style="height: 100%; width: auto; object-fit: contain;"
					                 alt="스터디 이미지">
					          </div>
					                <div class="book-card-content col col-7" >
					                    <div class="book-card-title">\${b.studyTitle}</div>
					                    <div class="book-card-desc">\${b.studyDate}</div>
					                    <div>
					                    	<i class="bi bi-people-fill"></i>
					                       <span>\${b.confirmedCount+1}</span>
					                       <span>/<span>
					                       <span>\${b.studyMemberLimit}</span>
					                    </div>
					                    <div id="studyAddress">\${b.studyAddress}</div>
					                    &nbsp;&nbsp;&nbsp;
					                    <span><i class="bi bi-hand-thumbs-up me-1" style="font-size: 0.9rem;"></i>
					                    <span>\${b.likeCount}</span>
					                    <span><i class="bi bi-hand-thumbs-down me-1" style="font-size: 0.9rem;"></i>
					                    <span>\${b.dislikeCount}</span>
					                </div>
					            </div>
					        </div>
						`;
						container.append(itemHtml);
					});
				}
				pageBarDiv.html(pageBar);
			},
			error: function(){
				alert("스터디  오류");
			}
		})
	}
</script>

<style>
.study-select-style {
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  background-color: #4CAF50; /* 초록색 */
  color: white;
  padding: 0.375rem 2.5rem 0.375rem 1rem; /* 오른쪽 패딩 여유 */
  border: none;
  border-radius: 999px; /* pill 형태 */
  background-image: url("data:image/svg+xml;charset=UTF-8,<svg xmlns='http://www.w3.org/2000/svg' fill='white' viewBox='0 0 16 16'><path d='M1.5 5.5l6 6 6-6'/></svg>");
  background-repeat: no-repeat;
  background-position: right 1rem center;
  background-size: 12px;
  font-weight: 500;
}

.tab-container {
  display: flex;
  justify-content: center;
  border-bottom: 2px solid #eee;
  margin-bottom: 1rem;
}

.tab-button {
  flex: 1;
  text-align: center;
  padding: 1rem 0;
  font-weight: 600;
  border: none;
  background: none;
  font-size: 1rem;
  position: relative;
  color: #666;
  cursor: pointer;
}

.tab-button.active {
  color: #000;
}

.tab-button.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 25%;
  width: 50%;
  height: 3px;
  background-color: #4CAF50;
  border-radius: 2px;
}

.tab-count {
  color: #4CAF50;
  font-weight: 700;
}
</style>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>