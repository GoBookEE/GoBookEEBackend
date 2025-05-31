<%@ page import="com.gobookee.common.CommonPathTemplate"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
    <%@ page
	import="com.gobookee.users.model.dto.User" %>
<%@ include file="/WEB-INF/views/mypage/mypageHeader.jsp"%>
<%
	User loginUser = (User) session.getAttribute("loginUser");
%>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
	
	<style>
	
		main {
			background-color : #FAFAFA;
		}
		
		.userProfile {
		    border-radius: 30px;
		    width: 560px;
		    height: 270px;
		    background-color: #FFFFFF;
		    justify-self: center;
		    box-shadow: 1px 4px 12px rgba(0, 0, 0, 0.2);
		    margin-top : 50px;
		}
		
		.profile-1 {
			display: flex;
			align-items: center;
			padding: 15px 40px;
			justify-content: space-between;
		}
		
		.profile-2 {
			padding: 15px 30px;
		}
		
		.profile-2 p {
			font-size:14px;
			font-weight:400;
		}
		
		.profile-img {
		 	display:flex;
			margin-top:10px;
			align-items: center;
		}
		
		.profile-img img{
			border-radius:100px;
		}
		
		.profile-img p {
			margin-left:20px;
		}
		
		.mypage-menulist {
			display:flex;
			flex-direction: column; 
			align-items: center;
			gap: 20px;
			margin-top: 40px;
		}
		
		.menulist {
			display: flex;
			flex-direction: row; 
			gap: 45px; 
			margin-top: 30px;
		}
		
		.box {
			width: 250px;
			height: 250px;
			background-color: #FFFFFF;
			box-shadow: 1px 4px 12px rgba(0, 0, 0, 0.2);
			border-radius: 16px;
		}
		
		.menulist:nth-child(2){
			margin-bottom: 70px;
		}
		
		.icon {
			display:flex;
			justify-self: center;
			margin-top:70px;
			font-size:40px;
			color:#50A65D;
			margin-right: 7px;
		}
		
		.box h4 {
			text-align: center;
			font-size:20px;
			font-weight:700;
			letter-spacing: -0.7px;
		}
		
		.profile-img span {
			font-size:20px;
			font-weight:900;
			color:#50A65D;
			margin-right:5px;
		}
		
		.profile-img p {
			font-size:16px;
			font-weight:400;
		}
		
		.progress {
			border-radius: 50px;
			width:500px;
			height:27px;
			justify-self: center;
		}
		
	</style>
	
<main>
	<section class="userProfile">
		<div>
			<div class="profile-1">
			<div class="profile-img">
				<img alt="" src="" width=80 height=80>
				<p><span><%= loginUser.getUserNickName() %></span>님 안녕하세요!</p>
			</div>
				<button type="button" class="btn btn-dark"
        		style="width: 91px; height: 34px; font-size:14px; border-radius: 50px;"><p>프로필 수정</p></button>
			</div>
			<div class="profile-2">
				<h3>고북이 속도</h3>
				<p>현재 <%=loginUser.getUserSpeed()%>km/s로 달리는 중</p>
					<div class="progress" style="height: 25px; width: 500px;">
  						<div class="progress-bar bg-success" role="progressbar"
       					style="width: <%=loginUser.getUserSpeed()%>%;" aria-valuenow="<%=loginUser.getUserSpeed()%>"
       					aria-valuemin="0" aria-valuemax="100">
    						<%=loginUser.getUserSpeed()%>%
  						</div>
					</div>
			</div>
		</div>
	</section>
	<section class="mypage-menulist">
		<div class="menulist">
			<div class="box">
				<i class="bi bi-bookmark-fill icon"></i>
				<h4>찜 목록</h4>
				
			</div>
			<div class="box">
				<i class="bi bi-book-fill icon" onclick="location.assign(`<%=request.getContextPath()%>/mypage/myboard`)"></i>
				<h4>내가 쓴 글 목록</h4>
				<p></p>
			</div>
		</div>
		<div class="menulist">
			<div class="box">
				<i class="bi bi-hand-thumbs-up-fill icon" onclick="location.assign(`<%=request.getContextPath()%>/mypage/recboard`)"></i>
				<h4>내가 추천한 글</h4>
				
			</div>
			<div class="box">
				<i class="bi bi-people-fill icon"></i>
				<h4>내 스터디</h4>
				
			</div>
		</div>
	</section>
</main>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>
