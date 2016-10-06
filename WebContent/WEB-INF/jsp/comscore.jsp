<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="windows-1250"%>
<%@page import="sk.tsystems.gamestudio.entity.*" %>
<c:set var="count" value="1" scope="page" />
<!-- com -->
<c:forEach items="${comments}" var="comment">
    <div class="comment">
        <span class="comusr">${comment.getUser().getName()}</span>
        <span class="comdat">${comment.getDate()}</span>
        <div class="comcom">${comment.getComment()}</div>
        <c:set var="count" value="${count + 1}" scope="page"/>
    </div>
</c:forEach>
<!--ic:${count-1}-->
<!-- /com -->

<!-- sco -->
Top score: <br/>
<table>
<tr><th>Pos.</th><th>Score</th><th>Name</th><th>Date</th></tr><c:set var="count" value="1" scope="page" />
<c:forEach items="${scores}" var="score">
    <tr>
        <td>${count}.</td>
        <td>${score.getScoreTime()}</td>
        <td>${score.getUser().getName()}</td>
        <td>${score.getFmtDate()}</td>
        <c:set var="count" value="${count + 1}" scope="page"/>
    </tr>
</c:forEach>
</table>
<!--ic:${count-1}-->
<!-- /sco -->
