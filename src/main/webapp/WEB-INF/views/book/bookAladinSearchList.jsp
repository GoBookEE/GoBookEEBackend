<%@ page import="com.gobookee.book.model.dto.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<%
    List<Book> bookList = (List<Book>) request.getAttribute("bookList");
    StringBuffer pageBar = (StringBuffer) request.getAttribute("pageBar");
    String query = (String) request.getAttribute("query");

%>
<script>
$("header").html(
`<div class="container d-flex justify-content-between align-items-center text-center small">
<a class="col-1" style="color:black" href="<%=request.getContextPath()%>/books/searchaladin">
    <i class="bi bi-x fs-1"></i>
</a>
</div>`);
</script>
<main class="flex-fill">

    <div class="form-container" style="padding: 70px 0">
        <%for (Book b : bookList) {%>
        <div class="p-4 book-card">
            <div class="row book-card-row">
                <div class="book-card-img col col-5">
                    <img src="<%=b.getBookCover().replace("coversum","cover500")%>">
                </div>
                <div class="book-card-content col col-7">
                    <div class="book-card-title"><%=b.getBookTitle()%>
                    </div>
                    <div class="book-card-desc"><%=b.getBookDescription()%>
                    </div>
                    <div>
                        <%
                            List aa = new ArrayList();
                            List aut = List.of(b.getBookAuthor().split(", "));
                            for (Object a : aut) {
                        %>
                        <span><%=a.toString().split(" ")[0]%></span>
                        <%}%>
                    </div>
                    <div><%=b.getBookPublisher()%> | <%=b.getBookPubdate()%>
                    </div>
                </div>
            </div>
            <div class="book-overlay d-none">
                <div class="overlay-buttons">
                    <div><button class="btn btn-light" value="review"><i class="bi bi-pencil-fill"></i>리뷰 바로 등록</button></div>
                    <div><button class="btn btn-light" value="detail"><i class="bi bi-book-fill"></i>책 상세 보기</button></div>
                    <div><button class="btn btn-light" value="insert"><i class="bi bi-cloud-plus-fill"></i>책 등록 하기</button></div>
                </div>
                <div>
                    <input value="<%=b.getBookID()%>" type="hidden">
                </div>
            </div>
        </div>
        <%}%>
        <div>
            <%=pageBar%>
        </div>
    </div>
</main>
<script>
    $(document).ready(function () {
        // 카드 클릭 시 오버레이 보이기
        $(".book-card").click(function () {
            // 먼저 기존 오픈된 오버레이는 닫기
            $(".book-overlay").addClass("d-none");

            // 현재 클릭된 카드의 오버레이 열기
            $(this).find(".book-overlay").removeClass("d-none");
        });

        // 오버레이 닫기 (오버레이 영역 외 클릭 시)
        $(document).click(function (e) {
            // 오버레이 안이나 카드 안을 클릭한 경우 닫지 않음
            if (
                $(e.target).closest(".book-card").length === 0 &&
                $(e.target).closest(".book-overlay").length === 0
            ) {
                $(".book-overlay").addClass("d-none");
            }
        });

        // 오버레이 내 버튼 클릭 시에는 닫지 않음
        $(".book-overlay button").click(function (e) {
            e.stopPropagation(); // 이벤트 버블링 방지
            const buttonValue = $(this).val(); // 또는 const buttonValue = e.target.value;
            // 현재 클릭된 버튼이 포함된 오버레이 내의 input value를 가져옴
            const bookId = $(this).closest(".book-overlay").find("input").val();
            $.ajax({
                url:"<%=request.getContextPath()%>/books/aladin/insertbookbyid",
                type:"POST",
                data:{bookId : bookId},
                before:()=>{

                },
                success:(response)=>{
                    if(response.success){
                        let bookSeq = response.bookSeq;
                        if (buttonValue === "review") {
                            window.location.href ="<%=request.getContextPath()%>/review/insertpage?bookSeq="+bookSeq;
                        } else if (buttonValue === "detail") {
                            window.location.href= "<%=request.getContextPath()%>/books/bookdetail?bookSeq="+bookSeq;
                        } else if (buttonValue === "insert"){
                            alert("책이 고북이 서버에 전송되었습니다.");
                        }
                    }else{
                        alert("오류가 발생했습니다. 다시 시도해 주세요!");
                    }
                },
                error:(response)=>{
                    alert("오류가 발생했습니다. 다시 시도해 주세요!");
                }
            });
        });

    });
</script>
<script src="https://unpkg.com/fast-average-color/dist/index.browser.min.js"></script>
<script>
    const fac = new FastAverageColor();

    document.querySelectorAll('.book-card-img > img').forEach(img => {
        // CORS 허용 안 되는 경우 자동 처리 시도
        img.crossOrigin = 'anonymous';

        if (img.complete) {
            applyAvgColor(img);
        } else {
            img.onload = () => applyAvgColor(img);
        }
    });

    function applyAvgColor(img) {
        fac.getColorAsync(img)
            .then(color => {
                img.parentElement.style.backgroundColor = color.hex;
            })
            .catch(err => {
                console.warn('색상 추출 실패:', err);
            });
    }

    $('header').append("<div class='book-card-head'>\"<%=query%>\" 의 검색 결과</div>");
</script>
<%@include file="/WEB-INF/views/common/footer.jsp" %>


