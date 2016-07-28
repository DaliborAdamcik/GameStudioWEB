<%@ page language="java" contentType="text/html; charset=windows-1250"
    pageEncoding="windows-1250"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1250">
	<title>GameStudio</title>

	<link rel="stylesheet" type="text/css" href="mines.css">
	<link rel="stylesheet" type="text/css" href="main.css">
	<script type='text/javascript' src='js/ajax.js'></script>
	<script type='text/javascript' src='js/gamestudio.js'></script>
	<script type='text/javascript' src='js/register.js'></script>
	<script type='text/javascript' src='js/services.js'></script>
	
	<script type='text/javascript' src='stones.js'></script>
	<script type='text/javascript' src='mines.js'></script>
	<script type='text/javascript' src='guess.js'></script>
	
</head>
<body>
	<header><span class="title">Game studio</span></header>
	<div id="mainmenu">
	</div>

	<div>
		<div id="gameboxer">
			<div id="newgames"></div>
			<div id="gamecontent">Welcome in GameStudio. Please, select game from menu. Enjoy FUN!</div>
		</div>
		<div id="gameattrib">
			Game rating: <div title="Game rating" class="rating"><div id="ratinggold" class="ratinggold"></div></div>
			<div id="rate">
				Rate me!
				<div class="rating" title="Rate this game" onmousemove="gs_ratmousover(event, this);" onclick="gs_ratdo(this);"><div id="ratte" class="ratinggold"></div></div>
			</div>

			<div id="scores"></div>
			<div id="addcoment">
				Add comment<br/>
				<textarea rows="5" cols="30" id="newcomment"></textarea><br/>
				<input type="button" value="Add comment" onclick="gs_addComment(document.getElementById('newcomment'));">
			</div>
			<div id="comments"></div>
		</div>
	</div>	
	
	<div id="winner" class="nicewindow"><div>You won game</div>Your score: <span id="winscore"></span></div>
	<div id="signin" class="nicewindow"><span class="loglab">Name:</span><input type="text" id="gs_user_name" />
	<br/><span class="loglab">Pass:</span><input type="password" id="gs_user_password" /><br/>
	<span class="aslink" onclick="gs_showregister();">Register me!</span>
	<input type="button" value="Sign in" onclick="gs_dosignin();"/></div>

	<div id="registerme" class="nicewindow">
		<span class="title">Game studio registration</span><br/>
		<table>
			<tr><td>Your name:</td><td><input type="text" id="gs_reg_name" onkeyup="gs_reg_validnick();" /></td><td><img src="img/err.png" id="val_nick" alt="bee" /></td></tr>
			<tr><td>Password:</td><td><input type="password" id="gs_reg_pass1" onkeyup="gs_reg_validpass();" /></td><td><img src="img/err.png" id="val_pass" alt="bee" /></td></tr>
			<tr><td>Retype password:</td><td><input type="password" id="gs_reg_pass2" onkeyup="gs_reg_validpass();" /></td></tr>
			<tr><td>&nbsp;</td><td></td></tr>
			<tr><td></td><td><input type="button" onclick="gs_doregister();" value="Register me">  <input type="button" onclick="gs_hideregister();" value="Cancel"></td></tr>
		</table>
	</div>
	
	<script type="text/javascript">mainAjax('option=gamelist', studio_parse);</script>
</body>
</html>
