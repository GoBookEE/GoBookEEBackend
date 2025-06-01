<%@ page import="com.gobookee.users.model.dto.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%
    User loginUser = (User) request.getSession().getAttribute("loginUser");
%>
<style>
    body {
        background-color: #f5f5f5;
    }

    .profile-container {
        max-width: 430px;
        margin: 0 auto;
        padding: 30px 20px 60px;
        background: #fff;
        border-radius: 16px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
        margin-top: 40px;
    }

    .top-bar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 30px;
    }

    .top-bar button {
        background: none;
        border: none;
        font-size: 20px;
        font-weight: bold;
    }

    .profile-img-wrapper {
        text-align: center;
        margin-bottom: 30px;
    }

    .profile-img {
        width: 100px;
        height: 100px;
        object-fit: cover;
        border-radius: 50%;
        cursor: pointer;
        border: 2px solid #ccc;
        transition: border-color 0.2s;
    }

    .profile-img:hover {
        border-color: #28a745;
    }

    .form-group {
        margin-bottom: 20px;
    }

    .form-control {
        border-radius: 50px;
        height: 48px;
        padding: 0 20px;
    }

    .input-with-btn {
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .btn-black {
        background-color: black;
        color: white;
        border: none;
        border-radius: 30px;
        padding: 4px 12px;
        font-size: 14px;
        white-space: nowrap;
    }

    .text-label {
        font-weight: bold;
        margin-bottom: 6px;
    }
</style>

<main>
    <div class="profile-container">
        <div class="top-bar">
            <button onclick="history.back()">✕</button>
            <span class="fw-bold fs-5">프로필 수정</span>
            <button class="text-success fw-bold" onclick="submitIfValid()">완료</button>
        </div>

        <form id="updateForm" action="<%=request.getContextPath()%>/mypage/updateprofile" method="post"
              enctype="multipart/form-data" onsubmit="return validateAndSubmit();">
            <div class="profile-img-wrapper">
                <label for="profileImageInput" class="d-block">
                    <img src="<%=request.getContextPath()%>/resources/upload/user/<%=loginUser.getUserProfile()%>"
                         class="profile-img" alt="프로필 이미지" id="previewImage"
                         onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png'">
                </label>
                <input type="file" name="profileImage" id="profileImageInput" accept="image/*" style="display: none;">
            </div>

            <div class="form-group">
                <div class="text-label">닉네임</div>
                <div class="input-with-btn">
                    <input type="text" name="userNickname" id="nickname" class="form-control"
                           placeholder="닉네임 입력" value="<%=loginUser.getUserNickName()%>">
                    <button type="button" class="btn-black" onclick="nickNameDuplicate()">중복확인</button>
                </div>
                <small id="nicknameMsg" class="text-danger"></small>
            </div>

            <input type="hidden" id="nicknameChecked" value="false">
            <input type="hidden" id="nicknameAvailable" value="false">

            <div class="form-group">
                <div class="text-label">아이디</div>
                <input type="text" class="form-control" value="<%=loginUser.getUserId()%>" readonly>
            </div>

            <div class="form-group">
                <div class="text-label">변경 비밀번호</div>
                <input type="password" name="newPassword" id="newPassword" class="form-control"
                       placeholder="변경할 비밀번호 입력">
                <small id="passwordMsg" class="text-danger"></small>
            </div>

            <div class="form-group">
                <div class="text-label">비밀번호 확인</div>
                <input type="password" id="confirmPassword" class="form-control" placeholder="변경할 비밀번호 재입력">
                <small id="confirmPasswordMsg" class="text-danger"></small>
            </div>

            <div class="form-group">
                <div class="text-label">이메일</div>
                <input type="email" class="form-control" value="<%=loginUser.getUserEmail()%>" readonly>
            </div>

            <div class="form-group">
                <div class="text-label">휴대폰 번호</div>
                <input type="text" name="userPhone" id="userPhone" class="form-control"
                       value="<%=loginUser.getUserPhone()%>">
                <small id="phoneMsg" class="text-danger"></small>
            </div>
        </form>
    </div>
</main>

<script>
    // 이미지 미리보기
    document.getElementById('profileImageInput').addEventListener('change', function (e) {
        const file = e.target.files[0];
        if (file?.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = event => {
                document.getElementById('previewImage').src = event.target.result;
            };
            reader.readAsDataURL(file);
        }
    });

    function nickNameDuplicate() {
        const nicknameMsg = document.getElementById("nicknameMsg");
        const userNickname = document.getElementById("nickname").value.trim();

        if (userNickname.length < 2) {
            nicknameMsg.innerText = "닉네임은 2자 이상이어야 합니다.";
            document.getElementById("nicknameChecked").value = "false";
            document.getElementById("nicknameAvailable").value = "false";
            return;
        }

        fetch("<%=request.getContextPath()%>/ajax/nicknameduplicate?userNickname=" + encodeURIComponent(userNickname))
            .then(response => response.json())
            .then(data => {
                document.getElementById("nicknameChecked").value = "true";
                document.getElementById("nicknameAvailable").value = data.result ? "true" : "false";

                if (data.result) {
                    nicknameMsg.innerText = "사용 가능한 닉네임입니다.";
                } else {
                    nicknameMsg.innerText = "이미 사용 중인 닉네임입니다.";
                }
            })
            .catch(() => {
                nicknameMsg.innerText = "중복 확인 실패";
            });
    }


    function validateAndSubmit() {
        let valid = true;

        // 닉네임 중복 확인 검사
        const nicknameInput = document.getElementById("nickname");
        const nicknameMsg = document.getElementById("nicknameMsg");
        const checked = document.getElementById("nicknameChecked").value === "true";
        const available = document.getElementById("nicknameAvailable").value === "true";
        const userNickname = nicknameInput.value.trim();
        const originalNickname = "<%=loginUser.getUserNickName()%>";

        // 닉네임이 기존 닉네임과 동일하면 중복확인 패스
        if (userNickname !== originalNickname) {
            if (!checked) {
                nicknameMsg.innerText = "닉네임 중복 확인을 진행해주세요.";
                valid = false;
            } else if (!available) {
                nicknameMsg.innerText = "이미 사용 중인 닉네임입니다.";
                valid = false;
            } else {
                nicknameMsg.innerText = "";
            }
        } else {
            // 기존 닉네임 그대로일 경우 중복 확인 무시
            nicknameMsg.innerText = "";
        }

        // 비밀번호
        const newPw = document.getElementById("newPassword").value;
        const confirmPw = document.getElementById("confirmPassword").value;
        const pwMsg = document.getElementById("passwordMsg");
        const confirmMsg = document.getElementById("confirmPasswordMsg");
        const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;

        if (newPw.length > 0 && !pwRegex.test(newPw)) {
            pwMsg.innerText = "비밀번호는 영문+숫자 포함 8자 이상이어야 합니다.";
            valid = false;
        } else {
            pwMsg.innerText = "";
        }

        if (newPw.length > 0 && newPw !== confirmPw) {
            confirmMsg.innerText = "비밀번호가 일치하지 않습니다.";
            valid = false;
        } else {
            confirmMsg.innerText = "";
        }

        // 휴대폰 번호
        const phoneInput = document.getElementById("userPhone");
        const phoneMsg = document.getElementById("phoneMsg");
        const phoneValue = phoneInput.value.trim();
        const phoneRegex = /^010-?\d{4}-?\d{4}$/;

        if (!phoneRegex.test(phoneValue)) {
            phoneMsg.innerText = "휴대폰 번호는 010-1234-5678 또는 01012345678 형식이어야 합니다.";
            valid = false;
        } else {
            phoneMsg.innerText = "";
        }

        // 하이픈 제거 후 전송
        phoneInput.value = phoneValue.replace(/-/g, "");

        return valid;
    }

    function submitIfValid() {
        if (validateAndSubmit()) {
            document.getElementById("updateForm").submit();
        }
    }
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
