<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@page contentType="text/html" pageEncoding="windows-1250"%><%@page import="sk.tsystems.gamestudio.entity.*" %><!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1250">
	<title>GameStudio</title>

	<link rel="stylesheet" type="text/css" href="mines.css">
	<link rel="stylesheet" type="text/css" href="main.css">
</head>
<body>
	<header><span class="title">Game studio - Hourly statistics best players</span></header>
<%@ include file="StatMenu.jsp" %>

<form method="get">
		<select name="gameid">
		<option value="0">-- All games --</option>
		<c:forEach items="${games}" var="gams">
	  		<option value="${gams.getID()}">${gams.getName()}</option>
		</c:forEach>
		</select>
		<input type="submit" value="filter" />
	</form>
	
<c:forEach items="${hourlyreport}" var="hrr">
	<h2>Best players - ${hrr.key}</h2>

	<table>
	<tr><th>Pos.</th><th>Score</th><th>Game</th><th>Name</th><th>Mail</th><th>Date</th><th>Descriptor</th></tr><c:set var="count" value="1" scope="page" />
	<c:forEach items="${hrr.value}" var="score">
	    <tr>
	        <td>${count}.</td>
	        <td>${score.getScoreTime()}</td>
	        <td>${score.getGame().getName()}</td>
	        <td>${score.getUser().getName()}</td>
	        <td>${score.getUser().getMail()}</td>
	        <td>${score.getFmtDate()}</td>
	        <td>${score.getDesc()}</td>
	        <c:set var="count" value="${count + 1}" scope="page"/>
	    </tr>
	</c:forEach>
	</table>
</c:forEach>
<!--ic:${count-1}-->
</body>
</html>
