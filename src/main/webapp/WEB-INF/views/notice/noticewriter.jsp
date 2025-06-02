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

<%@ include file="/WEB-INF/views/common/footer.jsp"%>