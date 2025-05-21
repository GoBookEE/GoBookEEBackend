const fn_makeTr = (dataArr = [], tagType = "td") => {
    return dataArr.reduce((p, n) => {
        const $tr = document.createElement(tagType);
        $tr.innerText = n;
        p.appendChild($tr);
        return p;
    }, document.createElement("tr"));
}

//유효성 검사 템플릿 함수
const Validator = {
    //빈 값 확인 함수
    isEmpty(value) {
        return value.trim() === '';
    },

    //이메일 여부 확인 함수
    isEmail(value) {
        const emailRegex = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
        return emailRegex.test(value);
    },

    //전화번호 여부 확인 함수
    isPhone(value) {
        const phoneRegex = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;
        return phoneRegex.test(value);
    },

    //패스워드 최소 8자, 영문 + 숫자 조합인지 확인하는 함수
    isPassword(value) {
        const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
        return pwRegex.test(value);
    },

    //문자열 최소, 최대 길이 확인하는 함수
    isLengthBetween(value, min, max) {
        return value.length >= min && value.length <= max;
    }
};
