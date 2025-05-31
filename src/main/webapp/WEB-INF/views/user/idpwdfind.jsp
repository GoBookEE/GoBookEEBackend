<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<style>

    .find-logo {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
        margin-bottom: 14px;
    }

    .find-logo h1 {
        font-weight: 800;
        font-size: 32px;
        margin: 0;
    }

    .find-title {
        font-size: 18px;
        font-weight: 600;
        margin-bottom: 20px;
        text-align: center;
    }

    .userBtn {
        margin-top: 56px;
    }

    .userBtn > button {
        width: 420px;
        height: 64px;
        border-radius: 50px;
        margin-bottom: 16px
    }

    .userBtn button {
        font-size: 20px;
        font-weight: 500;
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

</style>
<main>
    <section style="margin-top:20px;">
        <div class="container text-center small">
            <div class="find-logo">
                <img height="70" src="<%=request.getContextPath()%>/resources/images/logo.png" alt="Logo">
                <h1>GoBookE</h1>
            </div>
            <p class="find-title">아이디/비밀번호 찾기</p>
        </div>

    </section>
    <section style="text-align:center;">
        <div class="userBtn">
            <button type="button" class="btn btn-dark btn-lg" onclick="location.assign('<%=request.getContextPath()%>/findidpage')">아이디 찾기</button>
            <button type="button" class="btn btn-dark btn-lg">비밀번호 찾기</button>
        </div>
    </section>
    <section style="text-align:center; margin-top: 140px;">
        <p class="insertUser">계정이 없으신가요? <a data-bs-toggle="modal" data-bs-target="#exampleModal" style="color:#50A65D">등록</a>
        </p>
    </section>

    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" style="line-height:1.5rem; text-align:center;">
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
                                    onclick="location.assign('<%=request.getContextPath()%>/signuppage?userType=0')">일반
                                회원
                            </button>
                        </div>
                        <div>
                            <button type="submit" class="btn btn-success btn-lg"
                                    onclick="location.assign('<%=request.getContextPath()%>/signuppage?userType=1')">사업자
                                회원
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<%@include file="/WEB-INF/views/common/footer.jsp" %>