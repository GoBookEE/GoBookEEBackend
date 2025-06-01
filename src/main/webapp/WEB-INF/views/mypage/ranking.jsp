<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<section>
<h2 class="text-center fw-bold my-4">이번 달 고북이 속도 랭킹 🐢💨</h2>

<!-- TOP 3 -->
<div class="row justify-content-center text-center mb-4">
  <div class="col-4">
    <div class="card border-success">
      <div class="card-body">
        <h5>🥇 닉네임1</h5>
        <p>480점</p>
      </div>
    </div>
  </div>
  <div class="col-4">
    <div class="card border-secondary">
      <div class="card-body">
        <h5>🥈 닉네임2</h5>
        <p>460점</p>
      </div>
    </div>
  </div>
  <div class="col-4">
    <div class="card border-warning">
      <div class="card-body">
        <h5>🥉 닉네임3</h5>
        <p>445점</p>
      </div>
    </div>
  </div>
</div>

<!-- 전체 리스트 -->
<!-- <table class="table text-center">
  <thead>
    <tr><th>순위</th><th>닉네임</th><th>속도 점수</th></tr>
  </thead>
  <tbody>
    <tr><td>4</td><td>닉네임4</td><td>430점</td></tr>
    <tr><td>5</td><td>닉네임5</td><td>410점</td></tr>
    ...
  </tbody>
</table> -->

<!-- 내 순위 하단 고정 -->
<div class="fixed-bottom bg-light p-2 border-top text-center">
  👤 <strong>내 순위: 15위</strong> / <strong>속도 점수: 355점</strong>
</div> -->


</section>
<%@ include file="/WEB-INF/views/common/footer.jsp"%>