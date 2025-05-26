<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="java.sql.Timestamp,java.util.List,com.gobookee.review.model.dto.*,com.gobookee.common.DateTimeFormatUtil,
	com.gobookee.users.model.dto.*"%>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%
User loginUser = (User)session.getAttribute("loginUser");
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
				<%if(loginUser!=null&&loginUser.getUserSeq().equals(review.getUserSeq())){ %>
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
						<li><a class="dropdown-item"
							href="/review/delete?reviewSeq=<%=review.getReviewSeq()%>">게시물
								삭제</a></li>
					</ul>
				</div>
				<%} %>
			</div>

			<!-- 상단 태그 -->
			<!-- <div class="mb-2 text-muted small">IT/개발</div> -->

			<!-- 제목 -->
			<h5 class="fw-bold mb-2"><%=review.getReviewTitle()%></h5>

			<!-- 유저 정보 -->
			<div class="d-flex align-items-center mb-3">
				<img src="https://via.placeholder.com/40"
					class="rounded-circle me-2" alt="user" width="40" height="40">
				<div>
					<div class="fw-semibold"><%=review.getUserNickName()%></div>
					<small class="text-muted"><%=review.getReviewCreateTime()%></small>
				</div>
			</div>

			<!-- 책 정보 카드 -->
			<div id="selectedBookCard" class="p-3 book-card mb-2">
				<div class="row book-card-row align-items-center">
					<div class="book-card-img col-5">
						<img id="selectedBookImg" src="<%=review.getBookCover()%>"
							alt="book" width="40"> <i class="bi bi-bookmark-fill"></i>
					</div>
					<div class="book-card-content col-7">
						<div id="selectedBookTitle" class="book-card-title fw-bold"><%=review.getBookTitle()%></div>
						<div id="selectedBookTitle" class="book-card-desc"><%=review.getBookDescription()%></div>
						<div id="selectedBookAuthor" class="mb-1 text-muted"><%=review.getBookAuthor()%></div>
						<div id="selectedBookPublisher" class="mb-1 text-muted"><%=review.getBookPublisher()%></div>
						<!-- <button type="button" class="btn btn-sm btn-outline-danger mt-2"
							onclick="clearSelectedBook()">X</button> -->
					</div>
				</div>
			</div>
			<br>

			<!-- 본문 내용 -->
			<p class="mb-3">정리로운 내용을 알기 쉽게 구성으로 입문자에게 뛰어난 느낌을 지울 수 없습니다. 저자의
				노력과 그만큼의 노하우는 정성스러운 책 내용에서도 보입니다...</p>

			<!-- 추천/공유 버튼 -->
			<div class="d-flex align-items-center gap-3 mb-3">
				<div class="text-success d-flex align-items-center">
					<i class="bi bi-hand-thumbs-up-fill me-1"></i> 35
				</div>
				<div class="text-muted d-flex align-items-center">
					<i class="bi bi-chat-left-text me-1"></i> 2
				</div>
				<!-- <div class="text-muted ms-auto">
					<i class="bi bi-share-fill"></i>
				</div> -->
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
						<div>
							<button class="btn btn-sm btn-outline-secondary btn-reply-toggle"
								data-comment-id="<%=c.getCommentsSeq()%>">답글</button>
						</div>
						<%-- <%
						if (c.getUserNickName().equals(loginUser.getUserNickName())) {
						%> --%>
						<div class="dropdown">
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
											value="<%=c.getCommentsSeq()%>">
										<button type="submit" class="btn btn-sm btn-danger w-100">삭제</button>
									</form>
								</li>
							</ul>
						</div>
						<%-- <%
						}
						%> --%>
					</div>
					<div class="mt-1"><%=c.getCommentsContents()%></div>

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
								<%-- <%
								if (child.getUserNickName().equals(loginUser.getUserNickName())) {
								%> --%>
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
													value="<%=child.getCommentsSeq()%>">
												<button type="submit" class="btn btn-sm btn-danger w-100">삭제</button>
											</form>
										</li>
									</ul>
								</div>
								<%-- <%
								}
								%> --%>

							</div>
							<div class="mt-1"><%=child.getCommentsContents()%></div>
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

<%@ include file="/WEB-INF/views/common/footer.jsp"%>