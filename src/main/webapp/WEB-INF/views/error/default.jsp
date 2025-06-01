<!-- /WEB-INF/views/error/default.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>에러 발생</title>
    <style>
        body {
            text-align: center;
            background-color: #fffbea;
            font-family: 'Segoe UI', sans-serif;
            padding: 50px;
        }
        img {
            width: 150px;
            margin-bottom: 20px;
        }
        h1 {
            color: #ffc107;
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
<h1>문제가 발생했어요</h1>
<p>이용 중 불편을 드려 죄송합니다.<br>다시 시도하거나 메인 페이지로 돌아가 주세요.</p>
<a href="<%=request.getContextPath()%>/">메인으로 돌아가기</a>
</body>
</html>
