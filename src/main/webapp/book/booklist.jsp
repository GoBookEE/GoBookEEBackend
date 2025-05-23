<%@ page import="com.gobookee.book.model.dto.Book" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/header.jsp" %>
<%
    List<Book> bookList = (List<Book>) request.getAttribute("bookList");

%>
<main class="flex-fill">

    <div class="form-container">
        <div>책 리스트</div>
        <div>
            <%for(Book b : bookList){%>
            <div>
                <img src="<%=b.getBookCover().replace("coversum","cover500")%>">
            </div>
            <div>
                <div><%=b.getBookTitle()%></div>
                <div><%=b.getBookDescription()%></div>
                <div><%=b.getBookAuthor()%></div>
                <div><%=b.getBookPublisher()%></div>
                <div>리뷰</div>
                <div>별점</div>
            </div>
            <%}%>
        </div>
    </div>

</main>

<%@include file="/WEB-INF/views/common/footer.jsp" %>

