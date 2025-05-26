
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<style>
.insert-logo {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    margin-bottom: 14px;
  }

 .insert-logo h1 {
    font-weight: 800;
    font-size: 32px;
    margin: 0;
  }
  
 .input-group{
    background-color: white;
    box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.15);
    text-align: left;
    border-radius: 50px;
    width: 516px;
    height: 80px;
  }
  
 .input-group input {
	border:0px;
  	border-radius: 50px;
  	padding-left: 20px;
  }
  
 .input-group input:focus {
  	outline:none !important;
  	box-shadow:none !important;
  }
  
 .go-inner-btn{
    border:0px; 
    border-radius: 50px;
    font-size : 16px;
    width : 108px;
    height: 50px;
    font-weight:500;
  }
  
 .insert-title {
  	text-align:center;
  	font-size: 18px;
    font-weight: 600;
  }
  
 .input-group-text {
   	background-color:white; 
   	border:0px;  
   	border-radius: 50px;
  }
  
 .form-label {
    margin-top:26px;
    font-weight:500;
  }
  
 .login-title {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 20px;
    text-align:center;
  }
  
 .userBtn>button {
  	width: 516px;
  	height: 80px;
  	border-radius: 50px;
  	margin-top:85px
  }
  
 form {
  	display: flex;
	flex-direction: column;
    align-items: center; 
  }
  	
 .form-label, .radioInput{
  	text-align: left;
  	display: block;  
  	width: 75%;     
  	accent-color: black; 
}
 
 .eye-icon {
 	font-size:30px;
 	padding : 0 40px;
 }

</style>
<main class="flex-fill">
	<div class="form-container">
      <div class="insert-logo">
      <img height="70" src="https://avatars.githubusercontent.com/u/206787080" alt="Logo">
      <h1>GoBookE</h1>
    </div>
     <p class="insert-title">회원가입</p>
	<form action="<%=request.getContextPath()%>/user/userinsertpageend" method="post">
        <input type="hidden" name="userType" value="<%=request.getParameter("userType") %>" >
        <label class="form-label text-green">성별 <span style="color:#50A65D">*</span></label>
        <div class="radioInput">
        	<input type="radio" name="userGender" value="남자"> 남자
        	<input type="radio" name="userGender" value="여자"> 여자
        </div>
        <!-- 닉네임 -->
        <label class="form-label text-green">닉네임 <span style="color:#50A65D">*</span></label>
        <div class="input-group input-group-custom">
            <input type="text" class="form-control" name="userNickname" placeholder="닉네임을 입력하세요">
            <span class="input-group-text">
                <button class="go-inner-btn btn btn-dark">중복확인</button>
            </span>
        </div>
		 <label class="form-label text-green">주소 <span style="color:#50A65D">*</span></label>
		  <div class="input-group input-group-custom">
            <input type="text" name="userAddress" class="form-control" placeholder="주소를 입력하세요">
            <span class="input-group-text">
            	 <button class="go-inner-btn btn btn-dark">주소찾기</button>
            ️</span>
        </div>
    	<!-- 상세주소 -->
    	 <label class="form-label text-green">상세주소 <span style="color:#50A65D">*</span></label>
        <div class="input-group input-group-custom">
            <input type="text" class="form-control" name="userAddressDetail" placeholder="상세주소를 입력하세요">
        </div>
        <!-- 아이디 -->
        <label class="form-label text-green">아이디 <span style="color:#50A65D">*</span></label>
        <div class="input-group input-group-custom">
            <input type="text" class="form-control" name="userId" placeholder="아이디를 입력하세요">
            <span class="input-group-text">
                <button class="go-inner-btn btn btn-dark">중복확인</button>
            </span>
        </div>
        <!-- 비밀번호 -->
        <label class="form-label text-green">비밀번호 <span style="color:#50A65D">*</span></label>
        <div class="input-group input-group-custom">
            <input type="password" name="userPwd" class="form-control" placeholder="비밀번호를 입력하세요">
            <span class="input-group-text icon"><i class="bi bi-eye-slash eye-icon"></i>️</span>
        </div>

        <!-- 비밀번호 재확인 -->
        <label class="form-label text-green">비밀번호 재확인 <span style="color:#50A65D">*</span></label>
        <div class="input-group input-group-custom">
            <input type="password" name="userPwdCheck" class="form-control" placeholder="비밀번호를 입력하세요">
            <span class="input-group-text icon"><i class="bi bi-eye-slash  eye-icon"></i>️</span>
        </div>

        <!-- 이메일 인증 -->
        <label class="form-label text-green">이메일 인증 <span style="color:#50A65D">*</span></label>
        <div class="input-group input-group-custom">
            <input type="email" class="form-control" name="userEmail" placeholder="이메일을 입력하세요">
            <span class="input-group-text">
                <button class="go-inner-btn btn btn-dark">인증요청</button>
            </span>
        </div>

        <!-- 인증번호 -->
        <label class="form-label text-green">인증번호 <span style="color:#50A65D">*</span></label>
        <div class="input-group input-group-custom">
            <input type="text" class="form-control" name="userEmailCheckNum" placeholder="인증번호를 입력하세요">
            <span class="input-group-text">
                <button class="go-inner-btn btn btn-dark">확인</button>
            </span>
        </div>

        <!-- 휴대폰 번호 -->
        <label class="form-label text-green">휴대폰 번호 <span style="color:#50A65D">*</span></label>
        <div class="input-group input-group-custom">
            <input type="text" class="form-control" name="userPhone" placeholder="휴대폰 번호를 -없이 입력하세요">
        </div>

        <!-- 회원가입 버튼 -->
       <div class="userBtn">
        <button type="submit" class="btn btn-dark btn-lg">회원가입</button>
       </div>
		</form>
    </div>
</main>

<%@include file="/WEB-INF/views/common/footer.jsp" %>