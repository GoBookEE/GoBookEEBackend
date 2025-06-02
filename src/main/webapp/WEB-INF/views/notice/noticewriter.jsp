<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="com.gobookee.notice.model.dto.Notice, com.gobookee.users.model.dto.*"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%
String mode = (String) request.getAttribute("mode"); //write / update
boolean isUpdate = "update".equals(mode);
Notice notice = (Notice) request.getAttribute("notice");
%>
<style>
header, footer {
	display: none !important;
}
</style>

<div class="py-4 px-3" style="width: 40%; max-width: none; margin: 0;">
<form action="<%=request.getContextPath() %>/notice/<%= isUpdate? "update" : "write" %>" method="post">
	<div class="d-flex justify-content-between align-items-center mb-3">
		<button type="button" class="btn btn-link text-decoration-none text-dark fs-4"
			onclick="history.back()">
			<i class="bi bi-x-lg"></i>
		</button>
	</div>
	<input required value="<%=isUpdate? notice.getNoticeSeq() : ""%>" name="noticeSeq" type="hidden">
	
	<h5 class="fw-bold mb-4"><%=  isUpdate? "공지사항 수정" : "공지사항 작성" %></h5>

		<div class="mb-3">
			<label class="form-label">제목을 입력해 주세요 (20자 이내)</label> <input
				type="text" class="form-control" name="noticeTitle" maxlength="20"
				required value="<%= isUpdate ? notice.getNoticeTitle() : "" %>">
		</div>

		<div class="mb-3">
			<textarea class="form-control" name="noticeContents" rows="10"
				placeholder="내용을 입력해 주세요" required><%= isUpdate ? notice.getNoticeContents() : "" %></textarea>
		</div>
		<div class="mb-3">
			<input type="number" name="noticeOrder" value="<%= isUpdate ? notice.getNoticeOrder() : "0"%>" required>
		</div>
		<button type="submit" class="btn btn-dark w-100">
			<i class="bi bi-pencil-fill me-1"></i>
			<%= isUpdate ? "수정 완료" : "등록" %>
		</button>
	</form>
</div>

<script>
function openBookModal() {
	const modalEl = document.getElementById('bookSearchModal');
	if (modalEl) {
		new bootstrap.Modal(modalEl).show();
	}
}

function searchBooks() {
	const keyword = document.getElementById('bookSearchInput').value.trim();
	const resultArea = document.getElementById('bookSearchResults');
	resultArea.innerHTML = '';

	if (!keyword) {
		resultArea.innerHTML = '<div class="text-muted px-3 py-2">검색어를 입력하세요.</div>';
		return;
	}

	$.ajax({
		url: '<%=request.getContextPath()%>/book/search',
		data: { keyword },
		success: function (books) {
			resultArea.innerHTML='';
			if (!books || books.length === 0) {
				resultArea.innerHTML = `
				  <div class="text-center py-5">
					<i class="bi bi-book fs-1 text-muted"></i>
					<p class="text-muted">등록된 책이 없습니다</p>
					<a href="<%=request.getContextPath()%>/book/insert" class="btn btn-dark">책 등록하기</a>
				  </div>`;
			} else {
				books.forEach(book => {
					const item = document.createElement('div');
					item.className = 'book-card p-3 mb-2 ';
					item.style.cursor = 'pointer';
					item.innerHTML = `
						<div class="row book-card-row">
							<div class="book-card-img col-5">
								<img src="\${book.bookCover}" alt="book" width="50">
								<i class="bi bi-bookmark-fill"></i>
							</div>
							<div class="book-card-content col-7">
								<div class="book-card-title fw-bold">\${book.bookTitle}</div>
								<div class="mb-1 text-muted">\${book.bookAuthor}</div>
								<div class="mb-1 text-muted">\${book.bookPublisher} | \${book.publishDate}</div>
							</div>
						</div>
					`;

					item.onclick = function () {
						document.getElementById('bookSeq').value = book.bookSeq;
						document.getElementById('searchInput').value = book.bookTitle;
						document.getElementById('selectedBookCard').classList.remove('d-none');
						document.getElementById('selectedBookTitle').innerText = book.bookTitle;
						document.getElementById('selectedBookAuthor').innerText = book.bookAuthor;
						document.getElementById('selectedBookPublisher').innerText = book.bookPublisher;
						document.getElementById('selectedBookImg').src = book.bookCover || 'images/default-cover.png';

						const modalInstance = bootstrap.Modal.getInstance(document.getElementById('bookSearchModal'));
						if (modalInstance) modalInstance.hide();
					};

					resultArea.appendChild(item);
				});

			}
		},
		error: function () {
			resultArea.innerHTML = '<div class="text-danger px-3 py-2"><strong>오류:</strong> 검색 중 문제가 발생했습니다.</div>';
		}
	});
}

function clearSelectedBook() {
	document.getElementById('bookSeq').value = '';
	document.getElementById('searchInput').value = '';
	document.getElementById('selectedBookCard').classList.add('d-none');
}

const stars = document.querySelectorAll('#starRating i');
stars.forEach(star => {
	star.addEventListener('click', function () {
		const rating = this.dataset.value;
		document.getElementById('reviewRate').value = rating;

		stars.forEach(s => s.className = 'bi bi-star');
		for (let i = 0; i < rating; i++) {
			stars[i].className = 'bi bi-star-fill';
		}
	});
});

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
<script>
document.addEventListener("DOMContentLoaded", function () {
    const stars = document.querySelectorAll("#starRating i");
    const reviewRateInput = document.getElementById("reviewRate");


    // ⭐ 별 클릭 이벤트
    stars.forEach(star => {
        star.addEventListener("click", function () {
            const rating = parseInt(this.getAttribute("data-value"));
            highlightStars(rating);
            reviewRateInput.value = rating;
        });
    });

    // ⭐ 별 채우기 함수
    function highlightStars(rating) {
        stars.forEach((star, index) => {
            if (index < rating) {
                star.classList.remove("bi-star");
                star.classList.add("bi-star-fill");
            } else {
                star.classList.remove("bi-star-fill");
                star.classList.add("bi-star");
            }
        });
    }
});
</script>

<%@ include file="/WEB-INF/views/review/bookSearchModal.jsp"%>

<%@ include file="/WEB-INF/views/common/footer.jsp"%>