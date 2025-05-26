<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<section>
<br><br><br>
	<select id="studygroupsortby">
			<option value="latest">최신순</option>
			<option value="recommend">추천순</option>
	</select>
	<button onclick="location.assign('<%=request.getContextPath()%>/studygroup/listpage/active')">진행예정</button>
	<button>완료됨</button><!-- 버튼 비활성화 -->
	<div id="studyGroupListContainer"></div>
	<div id="studyPageBar"></div>
</section>
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
			url : "<%=request.getContextPath()%>/studygroup/studygroupendlist",
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
						const itemHtml =`
							<div class="list-group-item list-group-item-action d-flex gap-3 py-4" 
		                    onclick="location.assign('<%=request.getContextPath()%>/study/studyinfo?seq=\${b.studySeq}')">
							
								<%-- <img src='<%=CommonPathTemplate.getUploadPath(request,FileType.STUDY,studyName)%>'> --%>
								<div class="d-flex flex-column">
									<strong class="mb-1">\${b.studyTitle}</strong>
									<small>\${b.likeCount}</small>
								</div>
							</div>`;
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
<%@ include file="/WEB-INF/views/common/footer.jsp"%>