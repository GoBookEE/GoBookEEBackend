<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<style>
header, footer {
	display: none !important;
}
</style>
<!-- 📘 책 검색 모달 -->
<div class="modal fade" id="bookSearchModal" tabindex="-1"
	aria-hidden="true">
	<div
		class="modal-dialog modal-dialog-scrollable modal-fullscreen-sm-down">
		<div class="modal-content">
			<div class="modal-header">
				<h6 class="modal-title">책 검색</h6>
				<button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			</div>
			<div class="modal-body">
				<div class="input-group mb-3">
					<input id="bookSearchInput" type="text" class="form-control"
						placeholder="도서명 또는 저자명">
					<button class="btn btn-outline-secondary" onclick="searchBooks()">검색</button>
				</div>

				<div id="bookSearchResults" class="list-group small">
					<!-- 검색 결과 또는 '없음' 메시지 삽입 -->
				</div>
			</div>
		</div>
	</div>
</div>
<script>
//평균 색상 적용 (선택된 카드 포함)
function applyAvgColor(img) {
    fac.getColorAsync(img)
        .then(color => {
            img.parentElement.style.backgroundColor = color.hex;
        })
        .catch(err => {
            console.warn('색상 추출 실패:', err);
        });
}

document.querySelectorAll('.book-card-img > img').forEach(img => {
	img.crossOrigin = 'anonymous';
	if (img.complete) {
		applyAvgColor(img);
	} else {
		img.onload = () => applyAvgColor(img);
	}
});
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp"%>