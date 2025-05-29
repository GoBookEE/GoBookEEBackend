<!-- /WEB-INF/views/error/404.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>404 - 페이지를 찾을 수 없습니다</title>
    <style>
        body {
            text-align: center;
            background-color: #f8f9fa;
            font-family: 'Segoe UI', sans-serif;
            padding: 50px;
        }
        img {
            width: 150px;
            margin-bottom: 20px;
        }
        h1 {
            color: #dc3545;
        }
        p {
            color: #6c757d;
        }
        a {
            color: #198754;
            text-decoration: none;
        }
    </style>
</head>
<body>
<img src="<%=request.getContextPath()%>/resources/images/error.png" alt="Go Book E Logo">
<h1>404 - 페이지를 찾을 수 없습니다</h1>
<p>요청하신 페이지가 존재하지 않거나 이동되었습니다.</p>
<a href="<%=request.getContextPath()%>/">메인으로 돌아가기</a>
</body>
</html>