<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/WEB-INF/views/common/header.jsp"%>
<%@ page
	import="java.util.List,java.text.SimpleDateFormat,com.gobookee.notice.model.dto.Notice,com.gobookee.users.model.dto.User"%>
<%
List<Notice> notices = (List<Notice>) request.getAttribute("notices");
StringBuffer pageBar = (StringBuffer) request.getAttribute("pageBar");
%>

<style>
main {
	margin-top: 140px;
}

.accordion-header p {
	margin-left: 100px;
	margin-bottom: 0;
}

.row {
	width: 100%;
}
</style>
<main>
	<section>
		<div>
			<%
			for (Notice n : notices) {
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
				String formattedDate = sdf.format(n.getNoticeCreateTime());
			%>
			<div class="accordion" id="accordionPanelsStayOpenExample">
				<div class="accordion-item">
					<h2 class="accordion-header">
						<button class="accordion-button collapsed" type="button"
							data-bs-toggle="collapse"
							data-bs-target="#collapseOne-<%=n.getNoticeSeq()%>"
							aria-expanded="false" aria-controls="collapseOne">
							<div class="row">
								<div class="col-2">
									<%
									if (n.getNoticeOrder() > 0) {
									%>
									<i class="bi bi-pin-angle-fill"
										style="font-size: 20px; color: #50A65D;"></i>
									<%
									}
									%>
								</div>
								<div class="col-7"><%=n.getNoticeTitle()%></div>
								<div class="col-3"><%=formattedDate%></div>
							</div>
						</button>
					</h2>
					<div id="collapseOne-<%=n.getNoticeSeq()%>"
						class="accordion-collapse collapse"
						data-bs-parent="#accordionExample">
						<div class="accordion-body">
							<div class="row">
								<div class="col-2"><%=n.getNoticeSeq()%></div>
								<div class="col-7"><%=n.getNoticeContents()%></div>
								<%
								if (loginUser != null && loginUser.getUserId().equals("admin")) {
								%>
								<div class="col-3">
									<button
										onclick="location.assign('<%=request.getContextPath()%>/notice/writepage?mode=update&noticeSeq=<%=n.getNoticeSeq()%>')"
										class="btn btn-success">수정</button>
										<button
										onclick="location.assign('<%=request.getContextPath()%>/notice/delete?noticeSeq=<%=n.getNoticeSeq()%>')"
										class="btn btn-success">삭제</button>
								<%
								}
								%>
							</div>
						</div>
					</div>
				</div>
			</div>
			<%
			}
			%>
		</div>
	</section>
	<section>
		<div class="functionBar"
			style="display: flex; justify-content: space-between; align-items: center; padding: 0 20px; margin-top: 10px; position: relative;">
			<div class="pageBar" style="margin: -2px auto;">
				<%=pageBar%>
			</div>
			<%
			if (loginUser != null && loginUser.getUserId().equals("admin")) {
			%>
			<div class="writer">
			
				<button onclick="location.assign('<%=request.getContextPath()%>/notice/writepage?mode=write')"
					class="btn btn-success">작성</button>

			</div>
			<%
			}
			%>
		</div>
	</section>


</main>



<script>
	$('header').append("<div class='book-card-head'>공지사항</div>");
</script>

<%@include file="/WEB-INF/views/common/footer.jsp"%>