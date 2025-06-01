<%@ page import="com.gobookee.common.CommonPathTemplate"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
		import="com.gobookee.users.model.dto.User,com.gobookee.place.service.PlaceService" %>
<%@ include file="/WEB-INF/views/mypage/mypageHeader.jsp"%>
<%
	User loginUser = (User) session.getAttribute("loginUser");
	Long userSpeed = loginUser.getUserSpeed();
	String speedIcon = "👟";
	double logoLeftPercent = 0.0; // 기본 위치 (%)
	int progressPercent = 0;

	String userProfile = loginUser.getUserProfile();
	String profileImagePath;

	if (userProfile == null || userProfile.trim().isEmpty()) {
		profileImagePath = request.getContextPath() + "/resources/images/default.png";  // 기본 이미지 경로
	} else {
		profileImagePath = request.getContextPath() +"/resources/upload/user/" + userProfile;  // 실제 업로드 경로
	}


	if (userSpeed != null && userSpeed > 0) {
		if (userSpeed >= 1000) {
			speedIcon = "🚀";
		} else if (userSpeed >= 500) {
			speedIcon = "✈️";
		} else if (userSpeed >= 300) {
			speedIcon = "🚅";
		} else if (userSpeed >= 50) {
			speedIcon = "🚗";
		} else if (userSpeed >= 10) {
			speedIcon = "🚲";
		} else {
			speedIcon = "👟";
		}

		// 게이지 비율과 아이콘 위치 계산
		double ratio = Math.min(userSpeed, 1000) / 1000.0;
		progressPercent = (int)(ratio * 100);
		logoLeftPercent = ratio * 100;

	} else {
		userSpeed = 0L;
		progressPercent = 0;
		speedIcon = "😭";
		logoLeftPercent = 0; // 왼쪽 끝
	}

	double adjustedLeftPercent = logoLeftPercent - 3;

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
		margin-top: -14px;
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
		margin-top: 20px;
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
		margin-top: 20px;
	}

	.progress {
		border-radius: 50px;
		width:500px;
		height:27px;
		justify-self: center;
	}


	.profile-2 h3{
		font-size:18px;
		font-weight:600;
	}

	.progress, .progress-stacked {
		--bs-progress-bg: #C1DFC6;

	}

	.bg-success {
		border-radius: 30px;
		background: linear-gradient(90deg, #3D6C44 0%, #50A65D 100%); !important;
	}

	.number {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		height: 14px;
	}

	.speed-icon {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		margin-top: 5px;
	}

	.speed-icon p {
		font-size:22px; !important;
	}

	.box p {
		color:#AFAFAF;
		font-size:18px;
		text-align:center;
	}
</style>

<main>
	<section class="userProfile">
		<div>
			<div class="profile-1">
				<div class="profile-img">
					<img alt="" src="<%=profileImagePath%>" width=80 height=80>
					<p><span><%= loginUser.getUserNickName() %></span>님 안녕하세요!</p>
				</div>
				<button type="button" class="btn btn-dark" onclick="location.assign('<%=request.getContextPath()%>/mypage/updateprofilepage')"
						style="width: 91px; height: 34px; font-size:14px; border-radius: 50px;"><p>프로필 수정</p></button>
			</div>
			<div class="profile-2">
				<h3>고북이 속도</h3>
				<p>현재 <%=loginUser.getUserSpeed()%>km/s로 달리는 중</p>
				<div class="progress" style="height: 25px; width: 500px; position: relative; overflow: visible;">
					<!-- 게이지 바 -->
					<div class="progress-bar bg-success" role="progressbar"
						 style="width: <%=progressPercent%>%;"
						 aria-valuenow="<%=loginUser.getUserSpeed()%>"
						 aria-valuemin="0" aria-valuemax="1000">
					</div>
					<!-- 아이콘을 % 위치에 표시 -->
					<span class="speed-logo" style="z-index: 101; position: absolute; top: -17px; left: <%=adjustedLeftPercent%>%;
							transform: translateX(-40%); font-size: 36px; transform: scaleX(-1);"><%=speedIcon %></span>
				</div>
				<div class="gobooke-speed">
					<div class="number">
						<p>1</p><p>2</p><p>3</p><p>4</p><p>5</p>
					</div>
					<div class="speed-icon">
						<p style="margin-left:14px;">👟</p><p style="transform: scaleX(-1);">🚲</p><p style="margin-left:14px; transform: scaleX(-1);">🚗</p><p style="transform: scaleX(-1);">🚅</p><p>✈️</p><p>🚀</p>
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
				<p></p>
			</div>
			<div class="box" onclick="location.assign(`<%=request.getContextPath()%>/mypage/myboard`)">
				<i class="bi bi-book-fill icon"></i>
				<h4>내가 쓴 글 목록</h4>
				<p><%= request.getAttribute("totalWriteCount") != null ? request.getAttribute("totalWriteCount") : 0 %></p>
			</div>
		</div>
		<div class="menulist">
			<div class="box" onclick="location.assign(`<%=request.getContextPath()%>/mypage/recboard`)">
				<i class="bi bi-hand-thumbs-up-fill icon"></i>
				<h4>내가 추천한 글</h4>
				<p><%= request.getAttribute("recommendedCount") %></p>
			</div>
			<div class="box" onclick="location.assign(`<%=request.getContextPath()%>/mypage/mystudy`)">
				<i class="bi bi-people-fill icon" ></i>
				<h4>내 스터디</h4>
				<p><%=request.getAttribute("myStudyTotalCount") %></p>
			</div>
		</div>
	</section>
</main>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>