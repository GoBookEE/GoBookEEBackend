<%@ page import="com.gobookee.book.model.dto.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.gobookee.book.model.dto.BookCategory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<script>
<%--    헤더 변경해주기--%>
$("header").html(
    `<div class="container d-flex justify-content-between align-items-center text-center small">
        <a class="col-1" style="color:black" href="<%=request.getContextPath()%>/books/booklist">
            <i class="bi bi-x fs-1"></i>
        </a>
    </div>`
)

</script>
<style>


</style>
<% List<BookCategory> categories = (List<BookCategory>) request.getAttribute("bookCategory"); %>
<main class="flex-fill">

    <div class="container py-4" style="max-width: 600px;">
        <div class="book-detail">
            <form action="<%=request.getContextPath()%>/books/searchaladinrequest" onsubmit="">
            <div class="book-detail-content">
                <div class="book-detail-sub-content d-flex justify-content-between align-items-center">
                    <div class="fw-bold mb-0" >검색어</div>
                </div>
                <div class="dropdown-filter d-flex align-items-center gap-2">
                    <div>
                        <select id="filterType" class="form-select" name="QueryType" style="width: 150px;">
                            <option value="Keyword">제목+저자</option>
                            <option value="Title">제목검색</option>
                            <option value="Author">저자검색</option>
                            <option value="Publisher">출판사검색</option>
                        </select>
                    </div>
                    <div class="flex-grow-1">
                        <input type="text" name="Query" class="form-control" required>
                    </div>
                </div>
                <div class="book-detail-sub-content d-flex justify-content-between align-items-center">
                    <div class="fw-bold" style="margin-bottom: 0 !important;" >카테고리</div>
                </div>
                <div class="category-selects">
                    <div>
                        <select id="dept1" name="dept1" class="form-select" required>
                            <option value="">선택하세요</option>
                            <% for (BookCategory category : categories) {%>
                            <option value="<%=category.getBcCid()%>"><%=category.getBcCidName()%></option>
                            <%}%>
                        </select>
                    </div>
                    <div class="hide">
                        <select id="dept2" name="dept2" class="form-select" style="display:none;"></select>
                        <select id="dept3" name="dept3" class="form-select" style="display:none;"></select>
                        <select id="dept4" name="dept4" class="form-select" style="display:none;"></select>
                        <select id="dept5" name="dept5" class="form-select" style="display:none;"></select>
                    </div>
                    <div style="justify-self: center;">
                      <button class="btn btn-outline-success" type="submit"
                		style="height: 38px; width: 200px; padding: 0;  margin-top: 30px;">
                		검색하기 
            			 <i class="bi bi-search" style="font-size: 16px;"></i>
        			</button>		
                    </div>
                </div>
            </div>
            </form>
        </div>

    </div>
</main>
<script>
    $("#dept1").on("change", function(e){
        const cid = $(e.target).val();
        getCategory(cid,2);
        html = "";
        $("#dept3, #dept4, #dept5").empty();
        $('#dept3').hide(); 
    });
    
    $("#dept2").on("change", function(e){
        const cid = $(e.target).val();
        getCategory(cid,3)
    });
    $("#dept4").on("change", function(e){	
        const cid = $(e.target).val();
        getCategory(cid,4)
    });
    $("#dept5").on("change", function(e){
        const cid = $(e.target).val();
        getCategory(cid,5)
    });

    const getCategory=(cid,level)=>{

        $.ajax({
            url:"<%=request.getContextPath()%>/books/category/ajaxgetcategory",
            type : "GET",
            data : {cid: cid, level : level},
            success:(response)=>{
                $("#dept"+level).html("");
                const category = response;
                let html = '<option value="">선택하세요</option>';
                category.forEach(c => {
                    html += '<option value = "'+c.bcCid+'" >'+c.bcCidName+'</option>';
                });
                $("#dept"+level).append(html);
                $("#dept"+level).show();
            }

        })
    }
</script>
<%@include file="/WEB-INF/views/common/footer.jsp" %>

