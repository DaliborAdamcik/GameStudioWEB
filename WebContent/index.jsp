<%@ page language="java" contentType="text/html; charset=windows-1250"
    pageEncoding="windows-1250"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1250">
	<meta charset="windows-1250">
	<title>GameStudio</title>

	<link rel="stylesheet" type="text/css" href="mines.css">
	<link rel="stylesheet" type="text/css" href="main.css">
	<script type='text/javascript' src='ajax.js'></script>
	<script type='text/javascript' src='gamestudio.js'></script>
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
			<div id="rating" title="Game rating"><div id="ratinggold"></div></div>
			<div id="rate">
				Rate me!
				<img src="img/star.png" onclick="gs_rate(1);" alt="star">
				<img src="img/star.png" onclick="gs_rate(2);" alt="star">
				<img src="img/star.png" onclick="gs_rate(3);" alt="star">
				<img src="img/star.png" onclick="gs_rate(4);" alt="star">
				<img src="img/star.png" onclick="gs_rate(5);" alt="star">
			</div>
	
			<div id="addcoment">
				Add comment<br/>
				<textarea rows="5" cols="30" id="newcomment"></textarea><br/>
				<input type="button" value="Add comment" onclick="gs_addComment(document.getElementById('newcomment').value);">
			</div>
			<div id="comments"></div>
			Top score: <br/>
			<div id="scores">
			</div>
		</div>
	</div>	
	
	<div id="loader"><img src="img/loader.gif" alt="loader" /></div>
	<div id="winner" class="nicewindow"><div>You won game</div>Your score: <span id="winscore"></span></div>
	<div id="signin" class="nicewindow">Your name: <input type="text" id="gs_user_name" /> <input type="button" value="Sign in" onclick="gs_dosignin();"/></div>
	
	<script type="text/javascript">mainAjax('option=gamelist', studio_parse);</script>
	<!--  <script type="text/javascript">mainAjax('option=gamelist', initGameList);</script>  -->
</body>
</html>
