<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page import="com.gobookee.study.model.dto.StudyList,java.util.*" %>
<%
    List<StudyList> studies = (List<StudyList>) request.getAttribute("studylist");
%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<style>
    .studylistcontainer {
        display: flex;
        margin-bottom: 5px;
    }
</style>
<main class="flex-fill">
<div>
	<div class="d-inline-block" style="padding-top: 20px; padding-left: 1rem;">
	    <select id="studysortby" class="form-select study-select-style">
	      <option value="latest">최신순</option>
	      <option value="recommend">추천순</option>
	    </select>
  </div>
    <div id="studyListContainer"></div>
    <div id="studyPageBar" style="padding-bottom:80px"></div>
    <script>
        let select = "latest"

        $(document).ready(function () {
            studieslist("latest");
            $("#studysortby").on("change", function () {
                select = $(this).val();
                studieslist(select, 1);
            });
        });

        $(document).on("click", "#studyPageBar a.go-page-link", function (e) {
            e.preventDefault();
            const page = $(this).data("page");
            if (page) {
                studieslist(select, page);
            }
        });

        function studieslist(sortType, cPage = 1) {
            $.ajax({
                url: "<%=request.getContextPath()%>/study/studylist",
                type: "GET",
                data: {sort: sortType, cPage: cPage},
                dataType: "json",
                success: function (response) {
                    const container = $("#studyListContainer");
                    const pageBarDiv = $("#studyPageBar");
                    container.empty();
                    pageBarDiv.empty();
                    const studies = response.studies;
                    const pageBar = response.pageBar;

                    if (!studies || studies.length == 0) {
                        container.append("<div>등록된 스터디가 없습니다</div>");
                    } else {
                        studies.forEach(function (b) {
                            const itemHtml = `
							        <div class="p-4 book-card" onclick="location.assign('<%=request.getContextPath()%>/study/view?seq=\${b.studySeq}')">
							            <div class="row book-card-row">
							            <div class="book-card-img col col-5" style="height: 180px; overflow: hidden; display: flex; align-items: center; justify-content: center;">
							            <img src="<%=request.getContextPath()%>/resources/upload/study/\${b.photoRenamedName}" 
							                 onerror="this.src='<%=request.getContextPath()%>/resources/images/default.png';"
							                 style="height: 100%; width: auto; object-fit: contain;"
							                 alt="스터디 이미지">
							          </div>
							                <div class="book-card-content col col-7" >
							                <div class="book-card-title"
							                    style="width:300px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
							                 \${b.studyTitle}
							               </div>
							                    <div class="book-card-desc">\${b.studyDate ? b.studyDate : "날짜 미입력"}</div>
							                    <div>
							                    	<i class="bi bi-people-fill"></i>
							                       <span>\${b.confirmedCount+1}</span>
							                       <span>/<span>
							                       <span>\${b.studyMemberLimit}</span>
							                    </div>
							                    <div id="studyAddress">\${b.studyAddress}</div>
							                    &nbsp;&nbsp;&nbsp;
							                    <span><i class="bi bi-hand-thumbs-up me-1" style="font-size: 0.9rem;"></i>
							                    <span>\${b.likeCount}</span>
							                    <span><i class="bi bi-hand-thumbs-down me-1" style="font-size: 0.9rem;"></i>
							                    <span>\${b.dislikeCount}</span>
							                </div>
							            </div>
							        </div>
								`;
                            container.append(itemHtml);
                        });
                    }
                    pageBarDiv.html(pageBar);
                },
                error: function () {
                    alert("스터디  오류");
                }
            })
        }
    </script>
</div>

<script>
    const addr = document.getElementById("studyAddress");
    if (!addr.innerText.trim() || addr.innerText === "undefined" || addr.innerText === "null") {
        addr.innerText = "주소 미입력";
    }
</script>

<style>
    .book-card-img img {
        width: 100%;
        height: 100%;
        max-width: 200px;
        object-fit: cover; /* 이미지가 잘리더라도 꽉 차게 */
        box-shadow: none;
    }

    .book-card-img {
        height: 150px; /* 원하는 높이로 조절 */
        overflow: hidden; /* 넘치는 부분 잘리도록 */
        box-shadow: none;
        border-radius: 0;
    }

.study-select-style {
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  background-color: #4CAF50; /* 초록색 */
  color: white;
  padding: 0.375rem 2.5rem 0.375rem 1rem; /* 오른쪽 패딩 여유 */
  border: none;
  border-radius: 999px; /* pill 형태 */
  background-image: url("data:image/svg+xml;charset=UTF-8,<svg xmlns='http://www.w3.org/2000/svg' fill='white' viewBox='0 0 16 16'><path d='M1.5 5.5l6 6 6-6'/></svg>");
  background-repeat: no-repeat;
  background-position: right 1rem center;
  background-size: 12px;
  font-weight: 500;
}
</style>

</main>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>