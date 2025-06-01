<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<style>
  .rank-header {
    font-size: 1.5rem;
    text-align: center;
    margin-top: 2rem;
    font-weight: bold;
  }

  .my-rank-box {
    background: #eaf7ea;
    padding: 1rem;
    border-radius: 12px;
    margin: 2rem auto 1rem;
    text-align: center;
    font-size: 1.1rem;
    max-width: 500px;
  }

  .rank-box {
    display: flex;
    align-items: center;
    gap: 1rem;
    margin: 1rem 0;
  }

  .rank-box img {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    object-fit: cover;
  }

  .rank-label {
    font-weight: bold;
    color: #198754;
    font-size: 1.5rem;
    width: 50px;
  }

  .rank-progress {
    position: relative;
    height: 24px;
    background: #d0ead0;
    border-radius: 50px;
    flex: 1;
    overflow: hidden;
  }

  .rank-bar {
    height: 100%;
    background: linear-gradient(to right, #198754, #40c870);
    border-radius: 50px;
  }

  .turtle-icon {
    position: absolute;
    right: 0;
    top: 50%;
    transform: translateY(-50%);
    height: 28px;
  }
</style>

<section class="container">
  <h2 class="rank-header">이번 달 고북이 속도 랭킹 🐢💨</h2>

  <!-- 내 순위 표시 -->
  <div id="myRankBox"></div>

  <!-- 전체 랭킹 리스트 -->
  <div id="rankingContainer"></div>

  <!-- 페이지바 -->
  <div id="pageBar" class="mt-3 text-center"></div>
</section>

<script>
$(document).ready(function () {
  loadRanking(1);
});

//페이지바 클릭 이벤트
$(document).on("click", "#pageBar a.go-page-link", function (e) {
  e.preventDefault();
  const page = $(this).data("page");
  if (page) loadRanking(page);
});

function loadRanking(page) {
  $.ajax({
    url: "<%=request.getContextPath()%>/ranking/ajax",
    method: "GET",
    data: { cPage: page },
    success: function (res) {
      // 내 순위 박스
      const myRankHTML = `
        <div class="my-rank-box">
          👤 내 순위:
          <strong>${res.myRank > 0 ? res.myRank + "위" : "권외"}</strong> /
          속도: <strong>${res.mySpeed}km/h</strong>
        </div>`;
      $("#myRankBox").html(myRankHTML);

      // 전체 유저 랭킹 리스트
      const list = res.list;
      const container = $("#rankingContainer").empty();

      list.forEach(user => {
        const percent = user.userSpeed / (list[0]?.userSpeed || 1) * 100;
        container.append(`
          <div class="rank-box">
            <div class="rank-label">${user.rnum}등</div>
            <img src="<%=request.getContextPath()%>/upload/user/${user.userProfile || 'default.png'}"
                 onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png'">
            <div style="flex: 1;">
              <div><strong>${user.userNickname}</strong> 님 고북이</div>
              <div>${user.userSpeed}km/h로 달리는 중!</div>
              <div class="rank-progress">
                <div class="rank-bar" style="width:${percent}%"></div>
                <img src="<%=request.getContextPath()%>/resources/images/turtle-icon.png" class="turtle-icon">
              </div>
            </div>
          </div>
        `);
      });

      // 페이지바
      $("#pageBar").html(res.pageBar);
    }
  });
}
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>