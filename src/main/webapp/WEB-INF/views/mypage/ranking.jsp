<%@ page language="java" contentType="text/html;charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>

<style>
	.rank-header {
		font-size: 1.5rem;
		text-align: center;
		font-weight: bold;
		margin-bottom: 2rem;
	}

	.my-rank-box {
		background: #eaf7ea;
		padding: 1rem;
		border-radius: 12px;
		text-align: center;
		font-size: 1.1rem;
		max-width: 100%;
	}

	.rank-box {
		display: flex;
		align-items: center;
		gap: 1rem;
		margin-bottom: 2rem;
	}

	.rank-box img.profile {
		width: 60px;
		height: 60px;
		border-radius: 50%;
		object-fit: cover;
	}

	.rank-label {
		font-weight: bold;
		color: #198754;
		font-size: 1.5rem;
		width: 40px;
	}

	.rank-progress {
		position: relative;
		height: 25px;
		background: #d0ead0;
		border-radius: 30px;
		flex: 1;
		overflow: visible;
	}

	.rank-bar {
		height: 100%;
		background: linear-gradient(to right, #3D6C44, #50A65D);
		border-radius: 30px;
	}

	.turtle-icon {
		position: absolute;
		top: 50%;
		transform: translate(-50%, -50%);
		height: 40px;
	}

	.rank-box.top3 {
		background: #f0fff0;
		border: 1px solid #c5e7c5;
		border-radius: 12px;
		padding: 1rem;
		box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
	}
</style>

<main>
	<h2 class="rank-header">이번 달 고북이 속도 랭킹 🐢💨</h2>
	<div id="myRankBox" class="my-rank-box mx-auto"></div>
	<div id="rankingContainer" style="margin-top: 10px"></div>
	<div id="pageBar" class="mt-4 text-center pb-5"></div>
</main>

<script>
	$(document).ready(function () {
		loadRanking(1);
	});

	$(document).on("click", "#pageBar a.go-page-link", function (e) {
		e.preventDefault();
		const page = $(this).data("page");
		if (page) loadRanking(page);
	});

	function getSpeedInfo(speed) {
		const ctx = "<%=request.getContextPath()%>";
		let iconPath = ctx + "/resources/images/sad.png";
		let progressPercent = 0;
		let logoLeftPercent = 0;

		if (speed >= 0) {
			if (speed >= 1000) {
				iconPath = ctx + "/resources/images/rocket.png";
			} else if (speed >= 500) {
				iconPath = ctx + "/resources/images/airplane.png";
			} else if (speed >= 300) {
				iconPath = ctx + "/resources/images/train.png";
			} else if (speed >= 50) {
				iconPath = ctx + "/resources/images/car.png";
			} else if (speed >= 10) {
				iconPath = ctx + "/resources/images/bike.png";
			} else {
				iconPath = ctx + "/resources/images/walk.png";
			}

			const ratio = Math.min(speed, 1000) / 1000;
			progressPercent = Math.round(ratio * 100);
			logoLeftPercent = ratio * 100;
		}

		return {
			progressPercent,
			logoLeftPercent: logoLeftPercent - 3, // 아이콘 보정 위치
			iconPath
		};
	}


	function loadRanking(page) {
		$.ajax({
			url: "<%=request.getContextPath()%>/ranking/ajax",
			method: "GET",
			data: { cPage: page },
			success: function (res) {
				const myRankHTML = `
        <div class="my-rank-box">
          👤 내 순위:
          <strong>\${res.myRank > 0 ? res.myRank + "위" : "권외"}</strong> /
          속도: <strong>\${res.mySpeed}km/h</strong>
        </div>`;
				$("#myRankBox").html(myRankHTML);

				const list = res.list;
				const container = $("#rankingContainer").empty();

				list.forEach(user => {
					const { progressPercent, logoLeftPercent, iconPath } = getSpeedInfo(user.userSpeed);

					container.append(`
						  <div class="rank-box">
							<div class="rank-label">\${user.rnum}등</div>
							<img class="profile" src="<%=request.getContextPath()%>/upload/user/\${user.userProfile || 'default.png'}"
								 onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png'" style="margin-right: 10px">
							<div style="flex: 1;">
							  <div><strong>\${user.userNickName}</strong> 님 고북이</div>
							  <div>\${user.userSpeed}km/h로 달리는 중!</div>
							  <div class="rank-progress">
								<div class="rank-bar" style="width:\${progressPercent}%"></div>
								<span class="turtle-icon" style="left:\${logoLeftPercent}%">
								  <img src="\${iconPath}" style="height: 40px;">
								</span>
							  </div>
							  <div class="d-flex justify-content-between mt-1">
								<span>1</span><span>2</span><span>3</span><span>4</span><span>5</span>
							  </div>
							</div>
						  </div>
						`);
				});

				$("#pageBar").html(res.pageBar);
			}
		});
	}
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp"%>
