<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<style>
    body {
        background-color: #fff;
    }

    .find-box {
        max-width: 400px;
        margin: 80px auto;
        text-align: center;
    }

    .find-box h1 {
        font-weight: bold;
        font-size: 28px;
        margin-bottom: 40px;
        color: #000;
    }

    .form-group {
        display: flex;
        gap: 10px;
        margin-bottom: 15px;
    }

    .form-control {
        flex: 1;
        border-radius: 50px;
        height: 50px;
        padding-left: 20px;
        border: 1px solid #ccc;
    }

    .btn-small {
        border-radius: 50px;
        height: 50px;
        font-weight: bold;
        padding: 0 20px;
        white-space: nowrap;
    }

    .btn-black {
        background-color: #000;
        color: #fff;
        border: none;
    }

    .btn-black:hover {
        background-color: #333;
    }

    #timer {
        min-width: 60px;
        font-weight: bold;
        color: #dc3545;
    }

    .submit-btn {
        margin-top: 20px;
    }

    .error-msg {
        color: red;
        margin-bottom: 10px;
        font-weight: bold;
    }
</style>

<main class="find-box">
    <img src="<%=request.getContextPath()%>/resources/images/logo.png" alt="logo" height="50">
    <h1>비밀번호 재발급</h1>

    <div class="form-group">
        <input type="text" class="form-control" id="findUserId" placeholder="아이디를 입력하세요">
    </div>

    <div class="form-group">
        <input type="email" class="form-control" id="findUserEmail" placeholder="가입된 이메일을 입력하세요">
        <button class="btn btn-small btn-black" onclick="sendFindPwEmail()">인증요청</button>
    </div>

    <div class="form-group align-items-center">
        <input type="text" class="form-control" id="findEmailCode" placeholder="인증번호를 입력하세요">
        <button class="btn btn-small btn-black" onclick="verifyCode()">확인</button>
        <span id="timer"></span>
    </div>

    <div class="submit-btn">
        <button class="btn btn-black btn-lg" id="pwdUpdateBtn" hidden="hidden" onclick="forwardUpdatePage()">비밀번호 재설정
        </button>
    </div>
</main>

<script>
    let timerInterval;
    let timeLeft = 300;

    function sendFindPwEmail() {
        const email = $("#findUserEmail").val();
        if (!email) {
            alert("이메일을 입력해주세요.");
            return;
        }

        $.post("<%=request.getContextPath()%>/sendemail", {email: email}, function (res) {
            if (res === true) {
                alert("인증번호가 전송되었습니다.");
                startTimer();
            } else {
                alert("이메일 전송 실패");
            }
        }, "json");
    }

    function startTimer() {
        clearInterval(timerInterval);
        timeLeft = 300;
        updateTimerDisplay();

        timerInterval = setInterval(() => {
            timeLeft--;
            updateTimerDisplay();

            if (timeLeft <= 0) {
                clearInterval(timerInterval);
                $("#timer").text("만료됨").css("color", "red");
            }
        }, 1000);
    }

    function updateTimerDisplay() {
        const minutes = String(Math.floor(timeLeft / 60)).padStart(2, '0');
        const seconds = String(timeLeft % 60).padStart(2, '0');
        $("#timer").text(`\${minutes}:\${seconds}`).css("color", "#dc3545");
    }

    function verifyCode() {
        const code = $("#findEmailCode").val();
        const userId = $("#findUserId").val();
        const email = $("#findUserEmail").val();

        if (!code || !userId || !email) {
            alert("모든 정보를 입력해주세요.");
            return;
        }

        $.post("<%=request.getContextPath()%>/verifycode", {code: code}, function (res) {
            if (res === true) {
                clearInterval(timerInterval);
                $("#timer").text("인증 완료").css("color", "#28a745");
                $("#pwdUpdateBtn").removeAttr("hidden");
            } else {
                alert("인증번호가 일치하지 않습니다.");
            }
        }, "json");
    }

    function forwardUpdatePage() {
        // ✅ 인증 성공 시 비밀번호 재설정 페이지로 이동
        const userId = $("#findUserId").val();
        const email = $("#findUserEmail").val();
        window.location.href = "<%=request.getContextPath()%>/updatepwdpage?userId=" + userId + "&email=" + encodeURIComponent(email);
    }
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
