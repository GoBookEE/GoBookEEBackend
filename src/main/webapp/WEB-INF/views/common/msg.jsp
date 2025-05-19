<%--
  Created by IntelliJ IDEA.
  User: heebu
  Date: 2025-05-16
  Time: 오전 11:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>알림페이지</title>
</head>
<body>
<script>
    alert('<%=request.getAttribute("msg")%>');

    if (<%=request.getAttribute("close")!=null%>) {
        window.close();
    }

    if (<%=request.getAttribute("loc")==null%>) {
        location.replace("<%=request.getContextPath()%>");
    } else {
        location.replace("<%=request.getContextPath()%><%=request.getAttribute("loc")%>");
    }
</script>
</body>
</html>
