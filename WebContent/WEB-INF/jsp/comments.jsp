<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page contentType="text/html" pageEncoding="windows-1250"%>
<%@page import="sk.tsystems.gamestudio.entity.*" %>
<c:forEach items="${comments}" var="comment">
    <div class="comment">
        <span class="comusr">${comment.getUser().getName()}</span>
        <span class="comdat">${comment.getDate()}</span>
        <div class="comcom">${comment.getComment()}</div>
    </div>
</c:forEach>
