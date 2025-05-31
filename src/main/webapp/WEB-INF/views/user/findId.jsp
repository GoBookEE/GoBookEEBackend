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

    #userIdResult {
        margin-top: 20px;
        font-weight: bold;
        color: green;
    }

    .submit-btn {
        margin-top: 30px;
    }
</style>

<main class="find-box">
    <img src="<%=request.getContextPath()%>/resources/images/logo.png" alt="logo" height="50">
    <h1>아이디 찾기</h1>

    <div class="form-group">
        <input type="email" class="form-control" id="findEmail" placeholder="가입한 이메일을 입력하세요">
        <button class="btn btn-small btn-black" onclick="sendFindIdEmail()">인증요청</button>
    </div>


    <div class="form-group align-items-center">
        <input type="text" class="form-control" id="findEmailCode" placeholder="인증번호를 입력하세요">
        <button class="btn btn-small btn-black" onclick="verifyFindIdCode()">확인</button>
        <span id="timer" style="min-width: 60px; font-weight: bold; color: #dc3545;"></span>
    </div>


    <div id="userIdResult"></div>

    <!-- 비밀번호 재설정 페이지 이동 버튼 (초기에는 숨김 처리) -->
    <div id="goResetPwBtn" class="submit-btn" style="display:none;">
        <button class="btn btn-black btn-lg" onclick="goToResetPassword()">비밀번호 찾기</button>
    </div>

</main>

<script>
    let timerInterval;
    let timeLeft = 300; // 5분

    function sendFindIdEmail() {
        const email = $("#findEmail").val();
        if (!email) {
            alert("이메일을 입력해주세요.");
            return;
        }

        $.post("<%=request.getContextPath()%>/sendemail", {email: email}, function (res) {
            if (res === true) {
                alert("인증번호가 전송되었습니다.");
                startTimer();
            } else {
                alert("이메일 전송에 실패했습니다.");
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

    function verifyFindIdCode() {
        const code = $("#findEmailCode").val();
        const email = $("#findEmail").val();

        if (!code) {
            alert("인증번호를 입력해주세요.");
            return;
        }

        $.post("<%=request.getContextPath()%>/verifycode", {code: code}, function (res) {
            if (res === true) {
                clearInterval(timerInterval);
                $("#timer").text("인증 완료").css("color", "#28a745");

                $.get("<%=request.getContextPath()%>/findid?email=" + email, function (data) {
                    if (data && data.length > 0) {
                        const list = data.map(id => `<div>\${id}</div>`).join("");
                        $("#userIdResult").html("가입된 아이디:<br>" + list);
                        $("#goResetPwBtn").show(); // ✅ 버튼 보이기
                    } else {
                        $("#userIdResult").html("가입된 아이디가 없습니다.");
                    }
                }, "json");
            } else {
                alert("인증번호가 올바르지 않습니다.");
            }
        }, "json");
    }

    function goToResetPassword() {
        window.location.href = "<%=request.getContextPath()%>/findpwdpage";
    }


    function updateTimerDisplay() {
        const minutes = String(Math.floor(timeLeft / 60)).padStart(2, '0');
        const seconds = String(timeLeft % 60).padStart(2, '0');
        $("#timer").text(`\${minutes}:\${seconds}`);
    }

</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
