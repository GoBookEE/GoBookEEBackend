<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>GO-BOOK-E</title>

    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/common.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/han.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-SgOJa3DmI69IUzQ2PVdRZhwQ+dy64/BUtbMJw1MZ8t5HZApcHrRKUc4W0kG879m7" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.5/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-k6d4wzSIapyDyv1kpU366/PK5hCdSbCRGRCMv+eplOQJWyd1fbcAu9OCUj5zNLiq"
            crossorigin="anonymous"></script>
    <script src="<%=request.getContextPath()%>/resources/js/jquery-3.7.1.min.js"></script>

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--  노토산스 공용폰트  -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
</head>
<header>
    <div class="container d-flex justify-content-between align-items-center text-center small">
        <div class="col-2">
            <img height="70" src="<%=request.getContextPath()%>/resources/images/logo.png">
        </div>
        <div class="col-2">
            <i class="bi bi-search fs-1" onclick="location.assign('<%=request.getContextPath()%>/search/page')"></i>
        </div>
    </div>
</header>
<body class="d-flex flex-column min-vh-100">
