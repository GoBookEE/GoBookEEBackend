<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<style>
    .reset-box {
        max-width: 400px;
        margin: 80px auto;
        text-align: center;
    }

    .reset-box h1 {
        font-weight: bold;
        font-size: 28px;
        margin-bottom: 40px;
    }

    .form-control {
        border-radius: 50px;
        height: 50px;
        padding-left: 20px;
        border: 1px solid #ccc;
        margin-bottom: 15px;
        width: 100%;
    }

    .btn-black {
        background-color: #000;
        color: #fff;
        border: none;
        border-radius: 50px;
        height: 50px;
        width: 100%;
        font-weight: bold;
    }

    .btn-black:hover {
        background-color: #333;
    }

    .error-msg {
        color: red;
        font-size: 14px;
        margin-bottom: 10px;
        text-align: left;
    }
</style>

<main class="reset-box">
    <h1>비밀번호 재설정</h1>

    <form action="<%=request.getContextPath()%>/updatepwd" method="post" onsubmit="return validatePasswordForm()">
        <input type="hidden" name="userId" value="<%=request.getParameter("userId")%>">

        <input type="password" id="newPassword" name="newPassword" class="form-control" placeholder="새 비밀번호 입력">
        <input type="password" id="confirmPassword" class="form-control" placeholder="비밀번호 재입력">
        <div id="errorMsg" class="error-msg"></div>

        <button type="submit" class="btn btn-black">비밀번호 재설정</button>
    </form>
</main>

<script>
    function validatePasswordForm() {
        const pw = document.getElementById("newPassword").value.trim();
        const confirm = document.getElementById("confirmPassword").value.trim();
        const errorDiv = document.getElementById("errorMsg");

        const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;

        if (!pwRegex.test(pw)) {
            errorDiv.textContent = "영문자와 숫자를 포함하여 8자 이상 입력해주세요.";
            return false;
        }

        if (pw !== confirm) {
            errorDiv.textContent = "비밀번호가 일치하지 않습니다.";
            return false;
        }

        return true;
    }
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
