<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ page import="com.gobookee.study.model.dto.StudyView, java.util.*"%>
<%@ page import="com.gobookee.users.model.dto.User" %>
<% 
	List<StudyView> studyview = (List<StudyView>)request.getAttribute("studyView");
	List<StudyView> studyviewuser = (List<StudyView>)request.getAttribute("studyViewUser");
%>
<script>
$("header").html(
`<div class="container d-flex justify-content-between align-items-center text-center small">
<a class="col-1" style="color:black" href="<%=request.getContextPath()%>/study/listpage">
    <i class="bi bi-x fs-1"></i>
</a>
<div class="col-1"><i class="bi bi-three-dots-vertical fs-1"></i></div>
</div>`);
</script>
<main>
	<img src="<%=request.getContextPath()%>/resources/upload/study/<%=studyview.get(0).getPhotoName() %>">
	<div>
		<%
    		String profile = studyview.get(0).getUserProfile();
    		if (profile == null || profile.trim().isEmpty()) {
		%>
		<img src="<%=request.getContextPath()%>/resources/images/default.jpg">
		<%}else{ %>
		<img src="<%=request.getContextPath()%>/resources/upload/study/<%=studyview.get(0).getUserProfile() %>">
		<%} %>
		<span><%= studyview.get(0).getUserNickName() %></span>
		<span><%= studyview.get(0).getUserSpeed() %></span>	
	</div>
	<div><%= studyview.get(0).getStudyCategory() %></div>
	<div><%= studyview.get(0).getStudyTitle() %></div>
	<div><%= studyview.get(0).getStudyContent()%></div>
	<div><%= studyview.get(0).getStudyDate()%></div>
	<div>
		<i class="bi bi-person-fill"></i>
		<%= studyview.get(0).getConfirmedCount()%>
		<span>/</span>
		<%= studyview.get(0).getStudyMemberLimit()%>
	</div>
	<div>
		스터디 장소
	</div>
<%
	User loginUser = (User) request.getSession().getAttribute("loginUser");
	Long userseq = null;
	if (loginUser != null) {
		userseq = loginUser.getUserSeq();
		Long host = studyview.get(0).getUserSeq();

		if (userseq.equals(host)) {
%>
			<i class="bi bi-hand-thumbs-up"></i>
			<%= studyview.get(0).getLikeCount() %>
			<i class="bi bi-hand-thumbs-down"></i>
			<%= studyview.get(0).getDislikeCount() %>
<%		}else{%>
			<i class="bi bi-hand-thumbs-up"></i>
			<%= studyview.get(0).getLikeCount() %>
			<i class="bi bi-hand-thumbs-down"></i>
			<%= studyview.get(0).getDislikeCount() %>
<% 		}
	}else{
%>
		<i class="bi bi-hand-thumbs-up"></i>
		<%= studyview.get(0).getLikeCount() %>
		<i class="bi bi-hand-thumbs-down"></i>
		<%= studyview.get(0).getDislikeCount() %>
<%	} %>
	<div></div>
	<div>참여자 목록</div>
	<div id="study-member-list">
		<% for(StudyView s : studyviewuser){ %>
			<div>
			<%
				String users = studyviewuser.get(0).getUserProfile();
    			if (profile == null || profile.trim().isEmpty()) {
				%>
					<img src="<%=request.getContextPath()%>/resources/images/default.jpg">
				<%}else{ %>
					<img src="<%=request.getContextPath()%>/resources/upload/study/<%=studyviewuser.get(0).getUserProfile() %>">
				<%} %>
				<%= s.getUserNickName() %>
				<%= s.getUserSpeed() %>
				
			</div>
			
		<% } %>
		
	</div>
</main>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>