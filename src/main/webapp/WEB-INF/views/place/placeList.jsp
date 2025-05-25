<%@ page pageEncoding="utf-8" %>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<main>
    <h2>장소 목록 페이지</h2>
    <button onclick="location.assign('<%=request.getContextPath()%>/place/insertpage')">장소 등록</button>
    <button onclick="location.assign('<%=request.getContextPath()%>/place/view?placeSeq=9')">장소 상세 페이지</button>
    <button onclick="location.assign('<%=request.getContextPath()%>/place/reservepage')">장소 예약 페이지</button>
</main>
<%@include file="/WEB-INF/views/common/footer.jsp" %>