<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main>
    <div class="container mt-5">
        <h3>신청자 목록</h3>
        <div id="requestListContainer"></div>
    </div>
</main>

<script>
    const studySeq = new URLSearchParams(window.location.search).get("studySeq");

    $(document).ready(function () {
        loadRequests();
    });

    function loadRequests() {
        $.ajax({
            url: "<%=request.getContextPath()%>/study/request/list",
            type: "GET",
            data: { studySeq: studySeq },
            dataType: "json",
            success: function (data) {
                const container = $("#requestListContainer");
                container.empty();

                if (!data || data.length === 0) {
                    container.append("<div class='text-center mt-3'>신청 내역이 없습니다.</div>");
                } else {
                    for (let i = 0; i < data.length; i++) {
                        const req = data[i];
                        let actionButtons = "";

                        if (req.requestConfirm === "Y") {
                            actionButtons = `<span class="badge bg-success">승인됨</span>`;
                        } else if (req.requestConfirm === "R") {
                            actionButtons = `<span class="badge bg-danger">거절됨</span>`;
                        } else {
                            actionButtons = `
                                <button class="btn btn-success btn-sm approve-btn">승인</button>
                                <button class="btn btn-danger btn-sm reject-btn">거절</button>
                                `;
                        }

                        const html = `
                            <div class="card mb-2">
                                <div class="card-body d-flex justify-content-between" data-userseq="\${req.userSeq}">
                                    <div>
                                    <img src="\${req.userProfile && req.userProfile.trim() !== '' && req.userProfile !== 'null' ? '<%=request.getContextPath()%>/resources/upload/study/' + req.userProfile : '<%=request.getContextPath()%>/resources/images/default.png'}" class="rounded-circle" width="40">
                                        <strong>\${req.userNickName}</strong> <br>
                                        <small>\${req.requestMsg}</small>
                                    </div>
                                    <div>
                                    	거북이속도 \${req.userSpeed}
                                    </div>
                                </div>
                                <div class="card-body d-flex justify-content-end">
                                	<div>
                                		\${actionButtons}
                                	<div>
                                </div>
                            </div>`;

                        container.append(html);
                    }
                }
            },
            error: function () {
                alert("신청 목록 로딩 실패");
            }
        });
    }

    $(document).on("click", ".approve-btn, .reject-btn", function () {
        const card = $(this).closest(".card-body");
        const userSeq = card.data("userseq");
        const status = $(this).hasClass("approve-btn") ? "Y" : "R";

        $.ajax({
            url: "<%=request.getContextPath()%>/study/approve",
            type: "POST",
            data: {
                studySeq: studySeq,
                userSeq: userSeq,
                status: status
            },
            dataType: "json", 
            success: function (res) {
                console.log("서버 응답:", res);  // 디버깅용 로그

                if (res.success) {
                    loadRequests(); // 승인/거절 후 목록 새로 불러오기
                } else {
                    alert("처리 실패");
                }
            },
            error: function (xhr, status, error) {
                console.error("에러 발생:", status, error);
                alert("요청 중 오류 발생");
            }
        });
    });
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
