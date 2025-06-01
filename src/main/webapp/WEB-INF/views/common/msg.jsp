<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>GoBookE 알림</title>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body {
            margin: 0;
            background-color: #ffffff;
        }
    </style>
</head>
<body>
<script>
    const message = `<%=request.getAttribute("msg")%>`;
    const loc = `<%=request.getAttribute("loc")%>`;
    const error = `<%=request.getAttribute("error")%>`;

    Swal.fire({
        title: `<span style="color:#4CAF50;">\${message}</span>`,
        text: error === "error" ? '고북이가 슬퍼해요 🐢' : '고북이가 기뻐해요 🐢',
        icon: error === "error" ? 'error' : 'success',
        confirmButtonText: error === "error" ? '슬퍼요 😢' : '좋아요 😄',
        confirmButtonColor: '#4CAF50'
    }).then(() => {
        if (loc === "null") {
            location.replace("<%=request.getContextPath()%>");
        } else {
            location.replace("<%=request.getContextPath()%>" + loc);
        }
    });
</script>
</body>
</html>
