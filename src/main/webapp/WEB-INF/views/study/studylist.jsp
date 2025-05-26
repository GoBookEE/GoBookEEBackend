<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.gobookee.study.model.dto.StudyList,java.util.*" %>
<%
	List<StudyList> studies=(List<StudyList>)request.getAttribute("studylist");
%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<br><br><br>
	<style>
		.studylistcontainer{
			display: flex;
			margin-bottom: 5px;
		}
	</style>
	<div>
		<select id="studysortby">
			<option value="latest">최신순</option>
			<option value="recommend">추천순</option>
		</select>
		<div id="studyListContainer"></div>
		<div id="studyPageBar"></div>
	<script>
		let select="latest"
	
		$(document).ready(function (){
			studieslist("latest");
			$("#studysortby").on("change",function(){
				select = $(this).val();
				studieslist(select, 1);
			});
		});
		
		$(document).on("click", "#studyPageBar a.go-page-link", function (e) {
			 e.preventDefault();
			 const page = $(this).data("page");
			 if (page) {
			     studieslist(select, page);
			 }
			});
		
		function studieslist(sortType, cPage = 1){
			$.ajax({
				url : "<%=request.getContextPath()%>/study/studylist",
				type : "GET",
				data : {sort: sortType, cPage : cPage},
				dataType : "json",
				success : function(response){
					const container = $("#studyListContainer");
					const pageBarDiv = $("#studyPageBar");
					container.empty();
					pageBarDiv.empty();
					const studies = response.studies;
					const pageBar = response.pageBar;
					
					if(!studies||studies.length == 0){
						container.append("<div>등록된 스터디가 없습니다</div>");
					}else {
						studies.forEach(function(b){
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
	</div>	
<%--<div id="studyPageBar">
	<%=request.getAttribute("studyPageBar") %>
	</div> --%>

	
<%@ include file="/WEB-INF/views/common/footer.jsp"%>