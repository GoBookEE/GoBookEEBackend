<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main>
    <div class="container mt-5">
        <div id="requestListContainer"></div>
    </div>
</main>

<script>

	$(document).ready(function () {
	    $("header").html(`
	        <div style="height: 4rem"class="container d-flex justify-content-between align-items-center text-center small position-relative">
	    		<a class="col-1" style="color:black" href="<%=request.getContextPath()%>/study/view?seq=<%= request.getParameter("studySeq") %>">
	    		 <i class="bi bi-chevron-left"></i>
	    		</a>
	        </div>
	    `);
	});

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
                } 

                const confirmedCount = data.filter(r => r.requestConfirm === "Y").length;
                const studyLimit = data[0].studyMemberLimit;

                if ((confirmedCount + 1) >= studyLimit) {
                    container.append("<div class='text-center mt-3 text-danger fw-bold'>스터디 모집 완료</div>");
                    return;
                }

                
                
                    for (let i = 0; i < data.length; i++) {
                        const req = data[i];
                        let actionButtons = "";

                        if (req.requestConfirm === "Y") {
                            actionButtons = `<span class="btn btn-success btn-sm" disabled>승인됨</span>`;
                        } else if (req.requestConfirm === "R") {
                            actionButtons = `<span class="btn btn-danger btn-sm" disabled>거절됨</span>`;
                        } else {
                            actionButtons = `
                                <button class="btn btn-success btn-sm me-2 approve-btn">승인</button>
                                <button class="btn btn-danger btn-sm reject-btn">거절</button>
                            `;
                        }

                        const profileUrl = (req.userProfile && req.userProfile.trim() !== '' && req.userProfile !== 'null')
                            ? '<%=request.getContextPath()%>/resources/upload/study/' + req.userProfile
                            : '<%=request.getContextPath()%>/resources/images/default.png';

                        const html = `
                            <div class="card mb-3">
                                <div class="card-body" data-userseq="\${req.userSeq}">
                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                        <div class="d-flex align-items-center">
                                            <img src="\${profileUrl}" class="rounded-circle me-3" width="40" height="40">
                                            <div>
                                                <strong>\${req.userNickName}</strong><br>
                                                <small>\${req.requestMsg}</small>
                                            </div>
                                        </div>
                                        <div>거북이속도 \${req.userSpeed}</div>
                                    </div>
                                    <div class="d-flex justify-content-end mt-3 gap-1">
                                    	\${actionButtons}
                                	</div>

                                </div>
                            </div>`;

                        container.append(html);
                    }
                
            },
            error: function () {
                alert("신청 목록 로딩 실패");
            }
        });
    }

    $(document).on("click", ".approve-btn, .reject-btn", function () {
        const card = $(this).closest(".card");
        const userSeq = card.find(".card-body").data("userseq");
        const isApprove = $(this).hasClass("approve-btn");
        const confirmValue = isApprove ? "Y" : "R";

        $.ajax({
            url: "<%=request.getContextPath()%>/study/approve",
            type: "POST",
            data: {
                userSeq: userSeq,
                studySeq: studySeq,
                status: confirmValue
            },
            success: function () {
                loadRequests();
            },
            error: function () {
                alert("요청 처리 중 오류가 발생했습니다.");
            }
        });
    });
</script>

</html>
