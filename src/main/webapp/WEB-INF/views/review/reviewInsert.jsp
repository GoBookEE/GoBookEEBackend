<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="com.gobookee.review.model.dto.ReviewViewResponse, com.gobookee.users.model.dto.*"%>
<%@ page import="com.gobookee.book.model.dto.BookReviewResponse"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%
ReviewViewResponse review = (ReviewViewResponse) request.getAttribute("review");
String mode = (String) request.getAttribute("mode");
boolean isUpdate = "update".equals(mode);
BookReviewResponse selectedBook = (BookReviewResponse) request.getAttribute("selectedBook");
boolean isInsertWithBook = !isUpdate && selectedBook != null;
%>
<style>
header, footer {
	display: none !important;
}
</style>

<div class="py-4 px-3" style="width: 40%; max-width: none; margin: 0;">


	<div class="d-flex justify-content-between align-items-center mb-3">
		<button class="btn btn-link text-decoration-none text-dark fs-4"
			onclick="history.back()">
			<i class="bi bi-x-lg"></i>
		</button>
	</div>

	<h5 class="fw-bold mb-4"><%= isUpdate ? "수정하기" : "리뷰 쓰기" %></h5>

	<!-- 선택된 도서 카드 -->
	<div id="selectedBookCard"
		class="p-3 book-card mb-2 <%= isUpdate || isInsertWithBook ? "" : "d-none" %>">
		<div class="row book-card-row align-items-center">
			<div class="book-card-img col-5">
				<img id="selectedBookImg"
					src="<%= isUpdate ? review.getBookCover() : isInsertWithBook ? selectedBook.getBookCover() : "" %>"
					alt="book" width="40">
			</div>
			<div class="book-card-content col-7">
				<div id="selectedBookTitle" class="book-card-title fw-bold">
					<%= isUpdate ? review.getBookTitle() : isInsertWithBook ? selectedBook.getBookTitle() : "" %>
				</div>
				<div id="selectedBookAuthor" class="mb-1 text-muted">
					<%= isUpdate ? review.getBookAuthor() : isInsertWithBook ? selectedBook.getBookAuthor() : "" %>
				</div>
				<div id="selectedBookPublisher" class="mb-1 text-muted">
					<%= isUpdate ? review.getBookPublisher() : isInsertWithBook ? selectedBook.getBookPublisher() : "" %>
				</div>
				<button type="button" class="btn btn-sm btn-outline-danger mt-2"
					onclick="clearSelectedBook()">X</button>
			</div>
		</div>
	</div>

	<!-- 도서 검색 입력 필드 -->
	<div class="mb-3">
		<br> <label class="form-label fw-semibold">리뷰를 쓰고 싶은 책을
			검색해 보세요</label>
		<div class="input-group">
			<input type="text" id="searchInput" class="form-control"
				placeholder="도서명이나 저자명으로 찾아보세요" readonly onclick="openBookModal()">
			<span class="input-group-text"><i class="bi bi-search"></i></span>
		</div>
	</div>

	<!-- 리뷰 입력 폼 -->
	<form
		action="<%= isUpdate ? request.getContextPath() + "/review/update" : request.getContextPath() + "/review/insert" %>"
		method="post">
		<% if (isUpdate && review != null) { %>
		<input type="hidden" name="reviewSeq"
			value="<%= review.getReviewSeq() %>"> <input type="hidden"
			name="bookSeq" id="bookSeq" value="<%= review.getBookSeq() %>">
		<% } else { %>
		<input type="hidden" name="bookSeq" id="bookSeq" value="">
		<% } %>


		<div class="mb-3">
			<label class="form-label">제목을 입력해 주세요 (20자 이내)</label> <input
				type="text" class="form-control" name="reviewTitle" maxlength="20"
				required value="<%= isUpdate ? review.getReviewTitle() : "" %>">
		</div>

		<div class="mb-3">
			<textarea class="form-control" name="reviewContents" rows="6"
				placeholder="내용을 입력해 주세요" required><%= isUpdate ? review.getReviewContents() : "" %></textarea>
		</div>

		<div class="mb-3">
			<label class="form-label">이 책 어떠셨나요?</label><br>
			<div id="starRating" class="text-success fs-4">
				<!-- JS로 별점 설정 -->
				<i class="bi bi-star" data-value="1"></i> <i class="bi bi-star"
					data-value="2"></i> <i class="bi bi-star" data-value="3"></i> <i
					class="bi bi-star" data-value="4"></i> <i class="bi bi-star"
					data-value="5"></i>
			</div>
			<input type="hidden" name="reviewRate" id="reviewRate" value="0">
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
							</div>
							<div class="book-card-content col-7">
								<div class="book-card-title fw-bold">\${book.bookTitle}</div>
								<div class="mb-1 text-muted">\${book.bookAuthor}</div>
								<div class="mb-1 text-muted">\${book.bookPublisher}</div>
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

    // ⭐ 기본 별점 설정 (수정 모드)
    const initialRate = <%=isUpdate ? review.getReviewRate() : 0%>;
    highlightStars(initialRate);
    reviewRateInput.value = initialRate;

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

