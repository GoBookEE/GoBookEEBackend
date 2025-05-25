<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<style>
header, footer {
    display: none !important;
}
</style>
<!-- 📘 책 검색 모달 -->
<div class="modal fade" id="bookSearchModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-scrollable modal-fullscreen-sm-down">
    <div class="modal-content">
      <div class="modal-header">
        <h6 class="modal-title">책 검색</h6>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <div class="input-group mb-3">
          <input id="bookSearchInput" type="text" class="form-control" placeholder="도서명 또는 저자명">
          <button class="btn btn-outline-secondary" onclick="searchBooks()">검색</button>
        </div>

        <div id="bookSearchResults" class="list-group small">
          <!-- 검색 결과 또는 '없음' 메시지 삽입 -->
        </div>
      </div>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp"%>