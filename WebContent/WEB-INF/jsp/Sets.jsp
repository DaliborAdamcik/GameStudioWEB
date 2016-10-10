<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@page contentType="text/html" pageEncoding="windows-1250"%><%@page import="sk.tsystems.gamestudio.entity.*" %><!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1250">
	<title>GameStudio</title>

	<link rel="stylesheet" type="text/css" href="mines.css">
	<link rel="stylesheet" type="text/css" href="main.css">
	<style>
		table, td {
			border-top: 1px solid black;
			border-bottom: 1px solid black;
			border-collapse: collapse;
		}
	</style>
	
</head>
<body>
	<header><span class="title">Game studio - Settings</span></header>
<%@ include file="StatMenu.jsp" %>


<table>
<tr><th>Game</th><th>Setting name</th><th>value</th></tr>

<c:forEach items="${settings}" var="sets">
    <tr>
        <td>${sets.getGame().getName()}</td>
        <td>${sets.getName()}</td>
        <td>
	        <form method="post">
	        	<input type="hidden" name="gameid" value="${sets.getGame().getID()}" />
	        	<input type="hidden" name="sname" value="${sets.getName()}" />
	        	<textarea name="value">${sets.getValue()}</textarea>
	        	<input type="submit" value="Save" />
	        </form>
        </td>
    </tr>
</c:forEach>
</table>

<form method="post">
	<select name="gameid">
	<c:forEach items="${games}" var="gams">
  		<option value="${gams.getID()}">${gams.getName()}</option>
	</c:forEach>
	</select>

	<input type="text" name="sname" value="${sets.getName()}" />
	<textarea name="value"></textarea>
	<input type="submit" value="Save" />
</form>

</body>
</html>
