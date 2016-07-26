<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page contentType="text/html" pageEncoding="windows-1250"%>
<%@page import="sk.tsystems.gamestudio.entity.*" %>
<table border="1">
<tr>
<td>Pos.</td>
<td>Date</td>
<td>Name</td>
<td>Score</td>
</tr>
<c:set var="count" value="1" scope="page" />
<c:forEach items="${scores}" var="score">
    <tr>
        <td>${count}.</td>
        <td>${score.getDate()}</td>
        <td>${score.getUser().getName()}</td>
        <td>${score.getScore()}</td>
        <c:set var="count" value="${count + 1}" scope="page"/>
    </tr>
</c:forEach>
</table>