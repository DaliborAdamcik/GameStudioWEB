<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@page contentType="text/html" pageEncoding="windows-1250"%><!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1250">
	<title>GameStudio Setting - Login</title>

	<link rel="stylesheet" type="text/css" href="mines.css">
	<link rel="stylesheet" type="text/css" href="main.css">
	
</head>
<body>
	<header><span class="title">Game studio Setting Login</span></header>
	<div style="text-align: center;">
<form method="post">
	<input type="text" name="name" value="" placeholder="Login" /><br/>
	<input type="password" name="pass" value="" placeholder="Password" /><br/>
	<input type="submit" value="Login" />
</form>
<h3><c:out value="${authresult}"/></h3>
</div>
</body>
</html>
