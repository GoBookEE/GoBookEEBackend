<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="java.sql.Timestamp,java.util.List,com.gobookee.review.model.dto.*,com.gobookee.common.DateTimeFormatUtil,
	com.gobookee.users.model.dto.*"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%
User loginUser = (User) session.getAttribute("loginUser");
ReviewViewResponse review = (ReviewViewResponse) request.getAttribute("review");
List<CommentsViewResponse> comments = review.getComments();
%>
<section>
	<main>
		<div class="container py-4">
			<!-- 이전/더보기 상단 바 -->
			<div
				class="d-flex justify-content-between align-items-center py-2 border-bottom mb-3">
				<button class="btn btn-link text-dark text-decoration-none"
					onclick="history.back()">
					<i class="bi bi-arrow-left"></i> 뒤로
				</button>
				<%
				if (loginUser != null && loginUser.getUserSeq().equals(review.getUserSeq())) {
				%>
				<div class="dropdown">
					<button class="btn btn-link text-dark" id="moreMenu"
						data-bs-toggle="dropdown" aria-expanded="false">
						<i class="bi bi-three-dots-vertical"></i>
					</button>

					<ul class="dropdown-menu dropdown-menu-end"
						aria-labelledby="moreMenu">
						<li><a class="dropdown-item"
							href="<%=request.getContextPath()%>/review/updatepage?reviewSeq=<%=review.getReviewSeq()%>">게시물
								수정</a></li>
						<li><a class="dropdown-item text-danger" href="#"
							onclick="return confirmDeleteReview(<%=review.getReviewSeq()%>);">
								게시물 삭제 </a></li>
					</ul>
				</div>
				<%
				}
				%>
			</div>

			<!-- 제목 -->
			<h5 class="fw-bold mb-2"><%=review.getReviewTitle()%></h5>

			<!-- 유저 정보 -->
			<div class="d-flex align-items-center mb-3">
				<img src="https://via.placeholder.com/40"
					class="rounded-circle me-2" alt="user" width="40" height="40">
				<div>
					<div class="fw-semibold"><%=review.getUserNickName()%></div>
					<small class="text-muted"><%=DateTimeFormatUtil.format(review.getReviewCreateTime())%></small>
				</div>
			</div>
			<div
				class="d-flex border rounded p-3 mb-3 align-items-center bg-light">
				<img src="<%=review.getBookCover()%>" alt="book-cover" width="90"
					height="120" class="me-3 rounded">
				<div>
					<h6 class="mb-1"><%=review.getBookTitle()%></h6>
					<p class="mb-1 text-muted small"><%=review.getBookDescription()%></p>
					<div class="text-muted small"><%=review.getBookAuthor()%></div>
					<div class="review-meta mt-2">
						리뷰 <strong><%=review.getRecommendCount()%></strong>개 | 평점 <strong
							class="text-success">★ 4.0</strong>
					</div>
				</div>
			</div>
			<br>

			<!-- 본문 내용 -->

			<p class="text-muted mb-1 review-content"><%=review.getReviewContents()%></p>

			<%-- <!-- 추천 버튼 -->
			<div class="d-flex align-items-center gap-3 mb-3">
				<div class="text-success d-flex align-items-center">
					<i class="bi bi-hand-thumbs-up-fill me-1"></i>
					<%=review.getRecommendCount()%>
				</div>
				<div class="text-success d-flex align-items-center">
					<i class="bi bi-hand-thumbs-down-fill me-1"></i>
					<%=review.getNonRecommendCount()%>
				</div>
			</div> --%>
			<!-- 추천/비추천 버튼 -->
			<div class="d-flex align-items-center gap-3 mb-3">

				<!-- 👍 추천 -->
				<button
					class="btn btn-sm text-success d-flex align-items-center p-0 border-0 bg-transparent"
					onclick="recommendReview(<%=review.getReviewSeq()%>)">
					<i class="bi bi-hand-thumbs-up-fill me-1"></i> <span
						id="recommendCount"><%=review.getRecommendCount()%></span>
				</button>

				<!-- 👎 비추천 -->
				<button
					class="btn btn-sm text-danger d-flex align-items-center p-0 border-0 bg-transparent"
					onclick="nonRecommendReview(<%=review.getReviewSeq()%>)">
					<i class="bi bi-hand-thumbs-down-fill me-1"></i> <span
						id="nonRecommendCount"><%=review.getNonRecommendCount()%></span>
				</button>

			</div>

			<div class="border-top pt-3">
				<h6 class="fw-bold mb-3">댓글</h6>

				<%-- 댓글 작성 폼 --%>
				<form class="d-flex mt-3" method="post"
					action="<%=request.getContextPath()%>/review/insertcomment">
					<input type="hidden" name="reviewSeq"
						value="<%=review.getReviewSeq()%>"> <input type="text"
						class="form-control me-2" name="commentContent"
						placeholder="댓글을 입력하세요">
					<button class="btn btn-outline-success">등록</button>
				</form>
				<%
				if (comments != null && comments.size() > 0) {
					for (CommentsViewResponse c : comments) {
						boolean isReply = (c.getCommentLevel() != 1);
				%>
				<%-- 댓글 (레벨 1) --%>
				<%
				if (!isReply) {
				%>
				<div class="border rounded p-2 mb-2">
					<div class="d-flex justify-content-between">
						<div>
							<strong><%=c.getUserNickName()%></strong> <small
								class="text-muted ms-2"><%=DateTimeFormatUtil.format(c.getCommentsCreateTime())%></small>
						</div>
						<!-- 추천/비추천 버튼 -->
						<div class="d-flex align-items-center gap-3 mb-3">

							<!-- 👍 추천 -->
							<button
								class="btn btn-sm text-success d-flex align-items-center p-0 border-0 bg-transparent"
								onclick="recommendReview(<%=review.getReviewSeq()%>)">
								<i class="bi bi-hand-thumbs-up-fill me-1"></i> <span
									id="recommendCount"><%=review.getRecommendCount()%></span>
							</button>

							<!-- 👎 비추천 -->
							<button
								class="btn btn-sm text-danger d-flex align-items-center p-0 border-0 bg-transparent"
								onclick="nonRecommendReview(<%=review.getReviewSeq()%>)">
								<i class="bi bi-hand-thumbs-down-fill me-1"></i> <span
									id="nonRecommendCount"><%=review.getNonRecommendCount()%></span>
							</button>

						</div>
						<div class="ms-auto">
							<button class="btn btn-sm btn-outline-secondary btn-reply-toggle"
								data-comment-id="<%=c.getCommentsSeq()%>">
								<i class="bi bi-chevron-down"></i>
							</button>
						</div>
						<%
						if (loginUser != null && c.getUserSeq().equals(loginUser.getUserSeq())) {
						%>
						<div class="dropdown dropup">
							<button class="btn btn-sm btn-outline-secondary dropdown-toggle"
								type="button" id="commentDropdown<%=c.getCommentsSeq()%>"
								data-bs-toggle="dropdown" aria-expanded="false">
								<i class="bi bi-three-dots-vertical"></i>
							</button>
							<ul class="dropdown-menu dropdown-menu-end"
								aria-labelledby="commentDropdown<%=c.getCommentsSeq()%>">
								<li>
									<form
										action="<%=request.getContextPath()%>/review/updatecomment"
										method="post" class="px-2 py-1">
										<input type="hidden" name="commentSeq"
											value="<%=c.getCommentsSeq()%>"> <input type="hidden"
											name="reviewSeq" value="<%=review.getReviewSeq()%>">
										<input type="text" name="newContent"
											class="form-control form-control-sm mb-1"
											value="<%=c.getCommentsContents()%>" required>
										<button type="submit" class="btn btn-sm btn-success w-100">수정</button>
									</form>
								</li>
								<li>
									<form
										action="<%=request.getContextPath()%>/review/deletecomment"
										method="post" class="px-2 py-1"
										onsubmit="return confirm('정말 삭제하시겠습니까?');">
										<input type="hidden" name="commentSeq"
											value="<%=c.getCommentsSeq()%>"><input type="hidden"
											name="reviewSeq" value="<%=review.getReviewSeq()%>">
										<button type="submit" class="btn btn-sm btn-danger w-100">삭제</button>
									</form>
								</li>
							</ul>
						</div>
						<%
						}
						%>
					</div>
					<div class="mt-1 comment-content"><%=c.getCommentsContents()%></div>

					<!-- 댓글의 대댓글 영역 -->
					<div class="child-comments mt-2"
						id="child-comments-<%=c.getCommentsSeq()%>" style="display: none;">

						<%
						for (CommentsViewResponse child : comments) {
							if (child.getCommentLevel() == 2 && child.getCommentsParentSeq() == c.getCommentsSeq()) {
						%>
						<div class="border rounded p-2 mb-2 bg-light ms-4">
							<div class="d-flex justify-content-between">
								<div>
									<strong><%=child.getUserNickName()%></strong> <small
										class="text-muted ms-2"><%=DateTimeFormatUtil.format(child.getCommentsCreateTime())%></small>
								</div>
								<!-- 추천/비추천 버튼 -->
								<div class="d-flex align-items-center gap-3 mb-3">

									<!-- 👍 추천 -->
									<button
										class="btn btn-sm text-success d-flex align-items-center p-0 border-0 bg-transparent"
										onclick="recommendReview(<%=review.getReviewSeq()%>)">
										<i class="bi bi-hand-thumbs-up-fill me-1"></i> <span
											id="recommendCount"><%=review.getRecommendCount()%></span>
									</button>

									<!-- 👎 비추천 -->
									<button
										class="btn btn-sm text-danger d-flex align-items-center p-0 border-0 bg-transparent"
										onclick="nonRecommendReview(<%=review.getReviewSeq()%>)">
										<i class="bi bi-hand-thumbs-down-fill me-1"></i> <span
											id="nonRecommendCount"><%=review.getNonRecommendCount()%></span>
									</button>

								</div>
								<%
								if (c.getUserSeq().equals(loginUser.getUserSeq())) {
								%>
								<div class="dropdown">
									<button
										class="btn btn-sm btn-outline-secondary dropdown-toggle"
										type="button" id="replyDropdown<%=child.getCommentsSeq()%>"
										data-bs-toggle="dropdown" aria-expanded="false">
										<i class="bi bi-three-dots-vertical"></i>
									</button>
									<ul class="dropdown-menu dropdown-menu-end"
										aria-labelledby="replyDropdown<%=child.getCommentsSeq()%>">
										<li>
											<form
												action="<%=request.getContextPath()%>/review/updatecomment"
												method="post" class="px-2 py-1">
												<input type="hidden" name="commentSeq"
													value="<%=child.getCommentsSeq()%>"> <input
													type="hidden" name="reviewSeq"
													value="<%=review.getReviewSeq()%>"> <input
													type="text" name="newContent"
													class="form-control form-control-sm mb-1"
													value="<%=child.getCommentsContents()%>" required>
												<button type="submit" class="btn btn-sm btn-success w-100">수정</button>
											</form>
										</li>
										<li>
											<form
												action="<%=request.getContextPath()%>/review/deletecomment"
												method="post" class="px-2 py-1"
												onsubmit="return confirm('정말 삭제하시겠습니까?');">
												<input type="hidden" name="commentSeq"
													value="<%=child.getCommentsSeq()%>"><input
													type="hidden" name="reviewSeq"
													value="<%=review.getReviewSeq()%>">
												<button type="submit" class="btn btn-sm btn-danger w-100">삭제</button>
											</form>
										</li>
									</ul>
								</div>
								<%
								}
								%>

							</div>
							<div class="mt-1 comment-content"><%=child.getCommentsContents()%></div>
						</div>
						<%
						}
						}
						%>
						<%-- ✅ 대댓글 입력창 --%>
						<form class="d-flex mt-2 ms-4" method="post"
							action="<%=request.getContextPath()%>/review/insertcomment">
							<input type="hidden" name="reviewSeq"
								value="<%=review.getReviewSeq()%>"> <input type="hidden"
								name="parentCommentSeq" value="<%=c.getCommentsSeq()%>">
							<input type="text" class="form-control me-2"
								name="commentContent" placeholder="답글을 입력하세요">
							<button class="btn btn-outline-secondary btn-sm">등록</button>
						</form>
					</div>
				</div>
				<%
				}
				%>
				<%
				}
				} else {
				%>
				<p class="text-muted small">등록된 댓글이 없습니다.</p>
				<%
				}
				%>



			</div>

		</div>

	</main>
</section>
<style>
.review-content, .comment-content {
	word-break: break-word;
	overflow-wrap: break-word;
	white-space: pre-wrap; /* 줄바꿈 + 공백 유지 */
}
</style>
<script>
function confirmDeleteReview(seq) {
  if (confirm("정말 이 게시물을 삭제하시겠습니까?")) {
    location.href = "<%=request.getContextPath()%>/review/delete?reviewSeq=" + seq;
  }
  return false; // ❗ 링크 기본 동작 방지
}
</script>
<script>
	$(document).ready(
			function() {
				$(".btn-reply-toggle").on(
						"click",
						function() {
							const id = $(this).data("comment-id");
							const target = $("#child-comments-" + id);

							target.slideToggle(200, function() {
								const isShown = target.is(":visible");
								$(`button[data-comment-id='${id}']`).text(
										isShown ? "답글 닫기" : "답글");
							});
						});
			});
</script>
<script>
function recommendReview(reviewSeq) {
  $.ajax({
    url: "<%=request.getContextPath()%>/review/recommend",
    type: "POST",
    data: { reviewSeq },
    success: function (data) {
      if (data.success) {
        $("#recommendCount").text(data.count);
      } else {
        alert(data.message || "이미 추천하셨습니다.");
      }
    },
    error: function () {
      alert("추천 처리 중 오류 발생!");
    }
  });
}
function nonRecommendReview(reviewSeq) {
	  $.ajax({
	    url: "<%=request.getContextPath()%>/review/nonrecommend",
	    type: "POST",
	    data: { reviewSeq },
	    success: function (data) {
	      if (data.success) {
	        $("#nonRecommendCount").text(data.count);
	      } else {
	        alert(data.message || "이미 비추천하셨습니다.");
	      }
	    },
	    error: function () {
	      alert("비추천 처리 중 오류 발생!");
	    }
	  });
	}

</script>


<%@ include file="/WEB-INF/views/common/footer.jsp"%>