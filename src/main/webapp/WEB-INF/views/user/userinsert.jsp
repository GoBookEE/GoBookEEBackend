<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<script src="<%=request.getContextPath()%>/resources/js/commonTemplate.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>

    function sample5_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function (data) {
                var addr = data.address; // 최종 주소 변수

                // 주소 정보를 해당 필드에 넣는다.
                document.getElementById("userAddress").value = addr;

            }
        }).open();
    }
</script>
<style>
    .error-msg {
        color: red;
        font-size: 14px;
        margin-top: 4px;
        margin-left: 12px;
    }

    .insert-logo {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
        margin-bottom: 10px;
    }

    .insert-logo h1 {
        font-weight: 800;
        font-size: 32px;
        margin: 0;
    }

    .input-group {
        background-color: white;
        box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.15);
        text-align: left;
        border-radius: 50px;
        width: 516px;
        height: 80px;
    }

    .input-group i {
        display: flex;
    }

    .input-group input {
        border: 0px;
        border-radius: 50px;
        padding-left: 20px;
    }

    .input-group input:focus {
        outline: none !important;
        box-shadow: none !important;
    }

    .go-inner-btn {
        border: 0px;
        border-radius: 50px;
        font-size: 16px;
        width: 108px;
        height: 50px;
        font-weight: 500;
    }

    .insert-title {
        text-align: center;
        font-size: 18px;
        font-weight: 600;
    }

    .input-group-text {
        background-color: white;
        border: 0px;
        border-radius: 50px;
    }

    .form-label {
        margin-top: 30px;
        font-weight: 600;
    }

    .login-title {
        font-size: 18px;
        font-weight: 600;
        margin-bottom: 20px;
        text-align: center;
    }

    .userBtn > button {
        width: 516px;
        height: 80px;
        border-radius: 50px;
        margin-top: 85px
    }

    form {
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .form-label, .radioInput {
        text-align: left;
        display: block;
        width: 75%;
        accent-color: black;
    }

    .eye-icon {
        font-size: 30px;
        padding: 0 40px;
    }

    .wrong-type::placeholder {
        color: red;
        font-style: italic;
    }

</style>
<main class="flex-fill">
    <div class="form-container">
        <div class="insert-logo">
            <img height="70" src="<%=request.getContextPath()%>/resources/images/logo.png" alt="Logo">
            <h1>GoBookE</h1>
        </div>
        <p class="insert-title">회원가입</p>
        <form action="<%=request.getContextPath()%>/signup" method="post" onsubmit="return validatorForm()">
            <input type="hidden" name="userType" value="<%=request.getParameter("userType") %>">
            <label class="form-label text-green">성별 <span style="color:#50A65D">*</span></label>
            <div class="radioInput">
                <input type="radio" name="userGender" value="M"> 남자
                <input type="radio" name="userGender" value="F" style="margin-left:18px;"> 여자
                <div><span id="spanGender"></span></div>
            </div>
            <!-- 닉네임 -->
            <label class="form-label text-green">닉네임 <span style="color:#50A65D">*</span></label>
            <div class="input-group input-group-custom">
                <input type="text" class="form-control" name="userNickname" id="userNickname" placeholder="닉네임을 입력하세요">
                <span class="input-group-text">
                <button type="button" class="go-inner-btn btn btn-dark" onclick="nickNameDuplicate();">중복확인</button>
                <input type="hidden" id="nickNameDuplicateFlag" value="false" name="nickNameDuplicateFlag" readonly>
            </span>
            </div>
            <label class="form-label text-green">주소 <span style="color:#50A65D">*</span></label>
            <div class="input-group input-group-custom">
                <input type="text" name="userAddress" id="userAddress" class="form-control" placeholder="주소를 입력하세요"
                       readonly>
                <span class="input-group-text">
            	 <button type="button" onclick="sample5_execDaumPostcode();"
                         class="go-inner-btn btn btn-dark">주소찾기</button>
            ️</span>
            </div>
            <!-- 상세주소 -->
            <label class="form-label text-green">상세주소 <span style="color:#50A65D">*</span></label>
            <div class="input-group input-group-custom">
                <input type="text" class="form-control" id="userAddressDetail" name="userAddressDetail"
                       placeholder="상세주소를 입력하세요">
            </div>
            <!-- 아이디 -->
            <label class="form-label text-green">아이디 <span style="color:#50A65D">*</span></label>
            <div class="input-group input-group-custom">
                <input type="text" class="form-control" name="userId" id="userId" placeholder="아이디를 입력하세요">
                <span class="input-group-text">
                <button type="button" class="go-inner-btn btn btn-dark" onclick="idDuplicate();">중복확인</button>
                <input type="hidden" id="idDuplicateFlag" value="false" name="idDuplicateFlag" readonly>
            </span>
            </div>
            <!-- 비밀번호 -->
            <label class="form-label text-green">비밀번호 <span style="color:#50A65D">*</span></label>
            <div class="input-group input-group-custom">
                <input type="password" name="userPwd" class="form-control" placeholder="비밀번호를 입력하세요" id="userPwd">
                <span class="input-group-text icon">
            <i class="bi bi-eye-slash eye-icon toggle-eye" data-target="userPwd"></i>️</span>
            </div>

            <!-- 비밀번호 재확인 -->
            <label class="form-label text-green">비밀번호 재확인 <span style="color:#50A65D">*</span></label>
            <div class="input-group input-group-custom">
                <input type="password" name="userPwdCheck" class="form-control" id="userPwdCheck"
                       placeholder="비밀번호를 입력하세요">
                <span class="input-group-text icon">
            <i class="bi bi-eye-slash  eye-icon toggle-eye" data-target="userPwdCheck"></i>️</span>
            </div>

            <!-- 이메일 인증 -->
            <label class="form-label text-green">이메일 인증 <span style="color:#50A65D">*</span></label>
            <div class="input-group input-group-custom">
                <input type="email" class="form-control" name="userEmail" id="userEmail" placeholder="이메일을 입력하세요">
                <span class="input-group-text">
                <button type="button" class="go-inner-btn btn btn-dark" id="sendEmail">인증요청</button>
            </span>
            </div>

            <!-- 인증번호 -->
            <label class="form-label text-green">인증번호 <span style="color:#50A65D">*</span></label>
            <div class="input-group input-group-custom align-items-center">
                <input type="text" class="form-control" name="userEmailCheckNum" id="userEmailCheckNum"
                       placeholder="인증번호를 입력하세요">
                <span class="input-group-text">
        <button type="button" class="go-inner-btn btn btn-dark" id="verifyEmail">확인</button></span>
                <span class="ms-2" id="timer" style="font-weight: 600; color: #dc3545;"></span>
            </div>

            <!-- 이메일 인증 여부를 서버로 전송할 hidden input -->
            <input type="hidden" name="emailVerified" id="emailVerified" value="false">

            <!-- 휴대폰 번호 -->
            <label class="form-label text-green">휴대폰 번호 <span style="color:#50A65D">*</span></label>
            <div class="input-group input-group-custom">
                <input type="text" class="form-control" name="userPhone" id="userPhone" placeholder="휴대폰 번호를 -없이 입력하세요">
            </div>

            <!-- 회원가입 버튼 -->
            <div class="userBtn">
                <button type="submit" class="btn btn-dark btn-lg">회원가입</button>
            </div>
        </form>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="idDuplicatemodal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">아이디 중복 확인 결과</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">

                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="nickNameDuplicatemodal" tabindex="-1" aria-labelledby="exampleModalLabel2"
         aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel2">닉네임 중복 확인 결과</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">

                </div>
            </div>
        </div>
    </div>

    <script>
        // 인증 요청
        $("#sendEmail").on("click", function () {
            const email = $("#userEmail").val();
            if (!email) {
                alert("이메일을 입력하세요.");
                return;
            }

            $.post("<%=request.getContextPath()%>/sendemail", {email: email}, function (res) {
                if (res === true) {
                    alert("인증번호가 전송되었습니다.");
                    startTimer(); // 타이머 시작
                } else {
                    alert("이메일 전송 실패");
                }
            }, "json");
        });

        // 인증번호 확인
        $("#verifyEmail").on("click", function () {
            const code = $("#userEmailCheckNum").val();
            if (!code) {
                alert("인증번호를 입력하세요.");
                return;
            }

            $.post("<%=request.getContextPath()%>/verifycode", {code: code}, function (res) {
                if (res === true) {
                    alert("이메일 인증 성공!");

                    clearTimeout(timerInterval);
                    $("#timer").text("인증 완료").css("color", "#28a745");


                    // 인증번호 확인 성공 후
                    $("#userEmail").prop("readonly", true);
                    $("#sendEmail").prop("disabled", true);
                    $("#userEmailCheckNum").prop("readonly", true);
                    $("#verifyEmail").prop("disabled", true);

                    // 인증 여부 hidden input 값 변경
                    $("#emailVerified").val("true");
                } else {
                    alert("인증번호가 일치하지 않습니다.");
                }
            }, "json");
        });

        let timerInterval;
        let timeLeft = 300; // 5분 = 300초

        function startTimer() {
            clearInterval(timerInterval); // 기존 타이머 제거
            timeLeft = 300;
            updateTimerDisplay();

            timerInterval = setInterval(() => {
                timeLeft--;
                updateTimerDisplay();

                if (timeLeft <= 0) {
                    clearInterval(timerInterval);
                    $("#timer").text("만료됨");
                }
            }, 1000);
        }

        function updateTimerDisplay() {
            const minutes = String(Math.floor(timeLeft / 60)).padStart(2, '0');
            const seconds = String(timeLeft % 60).padStart(2, '0');
            $("#timer").text(`\${minutes}:\${seconds}`);
        }


        $("#userId").on("change", function () {
            $("#idDuplicateFlag").val("false");
        });

        $("[name='userGender']").on("change", function () {
            $("#spanGender").text("");
        });

        document.querySelectorAll('.toggle-eye').forEach(icon => {
            icon.addEventListener('click', function () {
                const inputId = this.getAttribute('data-target');
                const input = document.getElementById(inputId);

                if (input.type === 'password') {
                    input.type = 'text';
                    this.classList.remove('bi-eye-slash');
                    this.classList.add('bi-eye');
                } else {
                    input.type = 'password';
                    this.classList.remove('bi-eye');
                    this.classList.add('bi-eye-slash');
                }
            });
        });

        function nickNameDuplicate() {
            const userNickname = $("#userNickname").val();
            if (userNickname.length >= 2) {
                fetch("<%=request.getContextPath()%>/ajax/nicknameduplicate?userNickname=" + userNickname)
                    .then(response => {
                        if (response.ok) {
                            return response.json();
                        } else {
                            alert("요청실패 " + response.status);
                        }
                    })
                    .then(data => {
                        console.log(data);
                        const modalBody = document.querySelector("#nickNameDuplicatemodal .modal-body");

                        if (data.result) {
                            modalBody.innerText = `"\${userNickname}"는 사용 가능한 닉네임입니다.`;
                            const flag2 = $("#nickNameDuplicateFlag").val("true");
                        } else {
                            modalBody.innerText = `"\${userNickname}"는 이미 사용중입니다.`;
                            const flag2 = $("#nickNameDuplicateFlag").val("false");
                        }

                        const modal = new bootstrap.Modal(document.getElementById('nickNameDuplicatemodal'));
                        modal.show();
                    });
            } else {
                alert("닉네임은 2자 이상이어야 합니다.");
            }
        }


        const idDuplicate = () => {
            const userId = $("#userId").val();
            if (userId.length >= 4) {
                fetch("<%=request.getContextPath()%>/ajax/useridduplicate?userId=" + userId)
                    .then(response => {
                        if (response.ok) {
                            return response.json();
                        } else {
                            alert("요청실패 " + response.status);
                        }
                    })
                    .then(data => {
                        console.log(data);
                        const modalBody = document.querySelector("#idDuplicatemodal .modal-body");

                        if (data.result) {
                            modalBody.innerText = `"\${userId}"는 사용 가능한 아이디입니다.`;
                            const flag = $("#idDuplicateFlag").val("true");

                        } else {
                            modalBody.innerText = `"\${userId}"는 이미 사용중 입니다.`;
                            const flag = $("#idDuplicateFlag").val("false");
                        }
                        const modal = new bootstrap.Modal(document.getElementById('idDuplicatemodal'));
                        modal.show();
                    });
            } else {
                alert("아이디는 4자 이상이어야 합니다.");
            }
        }


        const validatorForm = () => {
            $("#phoneSpan").text("");
            const gender = $("input[name='userGender']:checked").val();
            const email = $("#userEmail").val();
            const userId = $("#userId").val();
            const userPwd = $("#userPwd").val();
            const userPwdCheck = $("#userPwdCheck").val();
            const userNickname = $("#userNickname").val();
            const userAddress = $("#userAddress").val();
            const userPhone = $("#userPhone").val();
            const flag = $("#idDuplicateFlag").val();
            const flag2 = $("#nickNameDuplicateFlag").val();

            var validateFlag = true;

            if (!gender) {
                $("#spanGender").text("성별을 선택해주세요").css("color", "red");
                $("input[name='userGender']").first().focus(); // 첫 번째 라디오에 포커스
                return false;
            }

            const nickNameInput = document.getElementById("userNickname");
            if (Validator.isEmpty(userNickname)) {
                nickNameInput.focus();
                nickNameInput.placeholder = "닉네임을 입력하세요";
                nickNameInput.value = "";
                nickNameInput.classList.add("border-danger", "wrong-type");

                const Msg6 = () => {
                    nickNameInput.placeholder = "닉네임을 입력하세요";
                    nickNameInput.value = "";
                    nickNameInput.classList.remove("border-danger", "wrong-type");
                    nickNameInput.removeEventListener("click", Msg6);
                }
                nickNameInput.addEventListener("click", Msg6)
                validateFlag = false;
            } else if (flag2 == "false") {
                nickNameInput.placeholder = "닉네임 중복확인을 진행하세요.";
                nickNameInput.value = "";
                nickNameInput.classList.add("border-danger", "wrong-type");

                const Msg6 = () => {
                    nickNameInput.placeholder = "닉네임을 입력하세요.";
                    nickNameInput.value = "";
                    nickNameInput.classList.remove("border-danger", "wrong-type");
                    nickNameInput.removeEventListener("click", Msg6);
                }
                nickNameInput.addEventListener("click", Msg6)
                validateFlag = false;
            }

            const addressInput = document.getElementById("userAddress");
            if (Validator.isEmpty(userAddress)) {
                addressInput.focus();
                addressInput.placeholder = "주소를 입력하세요.";
                addressInput.value = "";
                addressInput.classList.add("border-danger", "wrong-type");

                const Msg5 = () => {
                    addressInput.placeholder = "주소를 입력하세요.";
                    addressInput.value = "";
                    addressInput.classList.remove("border-danger", "wrong-type");
                    addressInput.removeEventListener("click", Msg5);
                }
                addressInput.addEventListener("click", Msg5)
                validateFlag = false;
            }

            const userIdInput = document.getElementById("userId");
            if (Validator.isEmpty(userId)) {
                userIdInput.focus();
                userIdInput.placeholder = "아이디를 입력하세요.";
                userIdInput.value = "";
                userIdInput.classList.add("border-danger", "wrong-type");

                const Msg4 = () => {
                    userIdInput.placeholder = "아이디를 입력하세요.";
                    userIdInput.value = "";
                    userIdInput.classList.remove("border-danger", "wrong-type");
                    userIdInput.removeEventListener("click", Msg4);
                }
                userIdInput.addEventListener("click", Msg4)
                validateFlag = false;
            } else if (flag == "false") {
                userIdInput.focus();
                userIdInput.placeholder = "아이디 중복확인을 진행하세요.";
                userIdInput.value = "";
                userIdInput.classList.add("border-danger", "wrong-type");

                const Msg4 = () => {
                    userIdInput.placeholder = "아이디를 입력하세요.";
                    userIdInput.value = "";
                    userIdInput.classList.remove("border-danger", "wrong-type");
                    userIdInput.removeEventListener("click", Msg4);
                }
                userIdInput.addEventListener("click", Msg4)
                validateFlag = false;
            }

            // 비밀번호 유효성 체크
            const pwdInput = document.getElementById("userPwd");
            const pwdCheckInput = document.getElementById("userPwdCheck");
            if (Validator.isEmpty(userPwd)) {
                pwdInput.focus();
                pwdInput.placeholder = "비밀번호를 입력하세요";
                pwdInput.value = "";
                pwdInput.classList.add("border-danger", "wrong-type");

                const Msg3 = () => {
                    pwdInput.placeholder = "비밀번호를 입력하세요";
                    pwdInput.value = "";
                    pwdInput.classList.remove("border-danger", "wrong-type");
                    pwdInput.removeEventListener("click", Msg3);
                }
                pwdInput.addEventListener("click", Msg3)
                validateFlag = false;

            } else if (!Validator.isPassword(userPwd)) {
                pwdInput.focus();
                pwdInput.placeholder = "영문자와 숫자를 포함해 8자 이상이어야 합니다.";
                pwdInput.value = "";
                pwdInput.classList.add("border-danger", "wrong-type");

                const Msg3 = () => {
                    pwdInput.placeholder = "비밀번호를 입력하세요.";
                    pwdInput.value = "";
                    pwdInput.classList.remove("border-danger", "wrong-type");
                    pwdInput.removeEventListener("click", Msg3);
                }
                pwdInput.addEventListener("click", Msg3)
                validateFlag = false;

            } else if (userPwd !== userPwdCheck) {
                pwdInput.focus();
                pwdInput.placeholder = "비밀번호가 일치하지 않습니다.";
                pwdCheckInput.value = "";
                pwdInput.value = "";
                pwdInput.classList.add("border-danger", "wrong-type");

                const Msg3 = () => {
                    pwdInput.placeholder = "비밀번호를 입력하세요.";
                    pwdInput.value = "";
                    pwdInput.classList.remove("border-danger", "wrong-type");
                    pwdInput.removeEventListener("click", Msg3);
                }
                pwdInput.addEventListener("click", Msg3)
                validateFlag = false;
            }


            // 이메일 유효성 체크
            const emailInput = document.getElementById("userEmail");
            if (Validator.isEmpty(email)) {
                emailInput.focus();
                emailInput.placeholder = "이메일을 입력하세요.";
                emailInput.value = "";
                emailInput.classList.add("border-danger", "wrong-type");

                const Msg2 = () => {
                    emailInput.placeholder = "이메일을 입력하세요.";
                    emailInput.value = "";
                    emailInput.classList.remove("border-danger", "wrong-type");
                    emailInput.removeEventListener("click", Msg2);
                }
                emailInput.addEventListener("click", Msg2)
                validateFlag = false;
            } else if (!Validator.isEmail(email)) {
                emailInput.focus();
                emailInput.placeholder = "잘못된 이메일 형식입니다.";
                emailInput.value = "";
                emailInput.classList.add("border-danger", "wrong-type");

                const Msg2 = () => {
                    emailInput.placeholder = "이메일을 입력하세요.";
                    emailInput.value = "";
                    emailInput.classList.remove("border-danger", "wrong-type");
                    emailInput.removeEventListener("click", Msg2);
                }
                emailInput.addEventListener("click", Msg2)
                validateFlag = false;
            }

            // 이메일 인증 여부 검사
            const emailVerifiedInput = document.getElementById("emailVerified");
            const emailVerifiedValue = emailVerifiedInput.value;
            const emailCheckNumInput = document.getElementById("userEmailCheckNum");

            if (emailVerifiedValue !== "true") {
                emailCheckNumInput.focus();
                emailCheckNumInput.placeholder = "이메일 인증을 완료해주세요.";
                emailCheckNumInput.value = "";
                emailCheckNumInput.classList.add("border-danger", "wrong-type");

                const Msg = () => {
                    emailCheckNumInput.placeholder = "인증번호를 입력하세요";
                    emailCheckNumInput.value = "";
                    emailCheckNumInput.classList.remove("border-danger", "wrong-type");
                    emailCheckNumInput.removeEventListener("click", Msg);
                };

                emailCheckNumInput.addEventListener("click", Msg);
                validateFlag = false;
            }

            // 휴대폰번호 유효성 체크
            const phoneInput = document.getElementById("userPhone");
            if (Validator.isEmpty(userPhone)) {
                phoneInput.focus();
                phoneInput.placeholder = "휴대폰 번호를 입력하세요.";
                phoneInput.value = "";
                phoneInput.classList.add("border-danger", "wrong-type");

                const Msg = () => {
                    phoneInput.placeholder = "휴대폰 번호를 -없이 입력하세요.";
                    phoneInput.value = "";
                    phoneInput.classList.remove("border-danger", "wrong-type");
                    phoneInput.removeEventListener("click", Msg);
                };

                phoneInput.addEventListener("click", Msg);
                validateFlag = false;

            } else if (!Validator.isPhone(userPhone)) {
                phoneInput.focus();
                phoneInput.placeholder = "잘못된 번호 형식입니다.";
                phoneInput.value = "";
                phoneInput.classList.add("border-danger", "wrong-type");

                const Msg = () => {
                    phoneInput.placeholder = "휴대폰 번호를 -없이 입력하세요.";
                    phoneInput.value = "";
                    phoneInput.classList.remove("border-danger", "wrong-type");
                    phoneInput.removeEventListener("click", Msg);
                };

                phoneInput.addEventListener("click", Msg);
                validateFlag = false;
            }

            // 하이픈 제거 처리
            document.getElementById("userPhone").value = userPhone.replace(/-/g, '');
            return validateFlag;
        };


    </script>
</main>

<%@include file="/WEB-INF/views/common/footer.jsp" %>