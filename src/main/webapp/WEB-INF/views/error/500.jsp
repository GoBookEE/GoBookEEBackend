<!-- /WEB-INF/views/error/500.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>500 - 서버 오류</title>
    <style>
        body {
            text-align: center;
            background-color: #fff3f3;
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
<h1>500 - 내부 서버 오류</h1>
<p>서버에서 예기치 않은 오류가 발생했습니다.</p>
<a href="<%=request.getContextPath()%>/">메인으로 돌아가기</a>
</body>
</html>