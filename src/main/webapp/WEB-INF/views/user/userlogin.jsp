<%@ page pageEncoding="UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ page import="com.gobookee.users.model.dto.User" %>

<%
    User loginUser = (User) session.getAttribute("loginUser");
    Cookie[] cookies = request.getCookies();
    String saveId = null;
    if (cookies != null) {
        for (Cookie c : cookies) {
            if (c.getName().equals("saveId")) {
                saveId = c.getValue();
                break;
            }
        }
    }

%>
<style>
    .login-logo {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
        margin-bottom: 14px;
    }

    .login-logo h1 {
        font-weight: 800;
        font-size: 32px;
        margin: 0;
    }

    .form-group input:focus {
        box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.15);
    }

    .login-title {
        font-size: 18px;
        font-weight: 600;
        margin-bottom: 20px;
    }

    .login-form {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 20px;
    }

    .form-group {
        text-align: left;
        width: 420px;
    }

    .form-group label {
        font-weight: 600;
        margin-bottom: 10px;
        margin-left: 16px;
        display: inline-block;
    }

    .form-group input {
        background-color: white;
        box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.15);
        text-align: left;
        border-radius: 50px;
        width: 100%;
        height: 54px;
        padding-left: 20px;
    }

    .idpw-find {
        display: flex;
        justify-content: flex-end;
        color: #50A65D;
        text-decoration: none;
    }

    .userBtn {
        margin-top: 50px;
    }

    .userBtn > button {
        width: 420px;
        height: 64px;
        border-radius: 50px;
        margin-bottom: 16px
    }

    .userTypeBtn {
        margin-top: 30px;
    }

    .userTypeBtn button {
        width: 350px;
        height: 60px;
        border-radius: 50px;
        margin-bottom: 16px;
        background-color: #50A65D;
    }

    .userBtn button {
        font-size: 20px;
        font-weight: 400;
    }

    .form-group input::placeholder {
        font-size: 14px;
        color: #aaa;
        font-weight: 400;
    }

    .line {
        gap: 0.8em;
        align-items: center;
        flex-grow: 1;
        height: 1px;
        background-color: currentColor;
        opacity: 0.4;
    }

    .wrapper {
        display: flex;
        align-items: center;
        gap: 30px;
    }

    .iconBtn i {
        background-color: #000000;
        border: 1px solid #000000;
        font-size: 30px;
    }

    .loginCheckbox {
        font-weight: 400;
        margin-left: 16px;
        display: flex;
        color: #AFAFAF;
    }

    .userFunction {
        display: flex;
        align-items: center;
        justify-content: space-between;
        width: 420px;
        margin-top: 10px;
    }

    .loginCheckbox label {
        display: flex;
        align-items: center;
        gap: 6px;
        font-weight: 400;
        color: #AFAFAF;
        margin-left: 6px;
    }

</style>

<main>
    <section class="login-section">
        <div class="container text-center small">


            <div class="login-logo">
                <img height="70" src="<%=request.getContextPath()%>/resources/images/logo.png" alt="Logo">
                <h1>GoBookE</h1>
            </div>

            <p class="login-title">로그인</p>


            <form action="<%=request.getContextPath()%>/login" method="post">
                <div class="login-form">
                    <div class="form-group">
                        <label for="userId">아이디</label>
                        <input id="userId" class="btn btn-lg" type="text" name="userId"
                               value="<%=saveId!=null?saveId:"" %>" placeholder="아이디를 입력하세요"/>
                    </div>


                    <div class="form-group">
                        <label for="userPwd">비밀번호</label>
                        <input id="userPwd" class="btn btn-lg" type="password" name="userPwd"
                               placeholder="비밀번호를 입력하세요"/>
                    </div>

                    <div class="userFunction">
                        <div class="loginCheckbox">
                            <input type="checkbox" class="checkbox" name="saveId"
                                   id="saveId" <%=saveId != null ? "checked" : "" %> />
                            <label for="saveId">아이디 저장</label>
                        </div>
                        <div>
                            <a href="<%=request.getContextPath()%>/findidpwdpage " class="idpw-find">아이디/비밀번호 찾기</a>
                        </div>
                    </div>
                    <div class="userBtn">
                        <button type="submit" class="btn btn-dark btn-lg">로그인</button>
                        <button type="button" data-bs-toggle="modal" data-bs-target="#exampleModal"
                                class="btn btn-dark btn-lg">회원가입
                        </button>
                    </div>
                </div>
            </form>


            <!-- 로그인, 회원가입 버튼 -->


            <!--
            선
            <div class="wrapper">
                <div class="line"></div>
                <div class="text">or</div>
                <div class="line"></div>
            </div>

            아이콘
            <div id="iconBtn">
                <i class="bi bi-facebook"></i>
                <i class="bi bi-google"></i>
                <i class="bi bi-apple"></i>
            </div> -->

            <!-- Modal -->
            <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" style="line-heiht:1.5rem">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="exampleModalLabel"></h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="login-logo">
                                <img height="70" src="<%=request.getContextPath()%>/resources/images/logo.png" alt="Logo">
                                <h1>GoBookE</h1>
                            </div>
                            <p style="font-weight:600; font-size:18px; letter-spacing: -0.3px;">회원 가입 유형을 선택해 주세요</p>
                            <div class="userTypeBtn">
                                <div>
                                    <button type="submit" class="btn btn-success btn-lg"
                                            onclick="location.assign('<%=request.getContextPath()%>/signuppage?userType=USER')">
                                        일반 회원
                                    </button>
                                </div>
                                <div>
                                    <button type="submit" class="btn btn-success btn-lg"
                                            onclick="location.assign('<%=request.getContextPath()%>/signuppage?userType=OWNER')">
                                        사업자 회원
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <script>
        /* 	const userTypeModal=()=>{
                const myModal = document.getElementById('myModal')
                const myInput = document.getElementById('myInput')

                myModal.addEventListener('shown.bs.modal', () => {
                  myInput.focus()
                })
            } */

    </script>

</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>