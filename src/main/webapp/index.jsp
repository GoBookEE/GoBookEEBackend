<%@ page pageEncoding="UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<%@ page import="com.gobookee.users.model.dto.User"  %>
<%
User u = (User)session.getAttribute("loginUser");
%>
<main>
    <h1>Hello, GoBookEE!</h1>
  
    <%
    Object loginUser = session.getAttribute("loginUser");

    if (loginUser != null) {
%>
        <p>로그인된 사용자입니다.</p>
        <a href="<%=request.getContextPath() %>/logout">로그아웃</a>
           <p><strong><%= u.getUserNickName() %></strong> 님, 안녕하세요!</p>
        <p>아이디: <%= u.getUserId() %></p>
         <p>아이디: <%= u.getUserPhone()%></p>
<%
    } else {
%>
        <p>로그인이 필요합니다.</p>
        <a href="<%=request.getContextPath()%>/loginpage">로그인</a>
<%
    }
%>



</main>
<%@include file="/WEB-INF/views/common/footer.jsp" %>