var activegame = null;
var prevdiv = null;

function mainAjax(params, callback) { callAjax('SvcGame', params, callback); }
function gameAjax(params) { callAjax('SvcGame', 'gameID='+activegame+'&option=play&'+params, studio_parse); }

function guessAjax(params) { gameAjax(params); }
function stonesAjax(params) { gameAjax(params); }
function minesAjax(params) { gameAjax(params); }

function initGameList(gamelist)
{
	var menu = '';
	
	gamelist.forEach(function (game) {
		menu+='<span onclick="menuItemClick('+game.ID+');" id="mgi_'+game.ID+'">'+game.name+'</span> ';
		
		callAjaxF('SvcGame?gameID='+game.ID+'&option=play', '', 
			function (ares){
				document.getElementById('newgames').innerHTML+=
				'<div id="ngs_'+game.ID+'" style="display: none;">'+ares+'</div>';
				//var regexrun = /<!--runnable([\s|\S|\n]*)-->/g;
				var regexrun = /<!--runnable([\s|\S|\n]*)\$-->[\s|\S|\n]*<!--linkscript([\s|\S|\n]*)#-->/g
				var runsc = regexrun.exec(ares);
				if(runsc!=null)
				{
					var runscript  = document.createElement("script"); // set runner for game
			        runscript.text = runsc[1].trim();
			        document.body.appendChild(runscript);

					runscript  = document.createElement("script"); // link game.js
			        runscript.src  = 'js-game/'+runsc[2].trim();
			        runscript.type = 'text/javascript';
			        document.body.appendChild(runscript);
				}
				else
					console.error('no runnable set for game id# '+game.ID);
			}, 'GET'); // ajax
	});// foreach

	// TODO: hidden statistic on welcome page
	//menu+='<span onclick="gs_showstatistics();">Statistics</span> ';
	menu+='<span id="gs_signin" onclick="gs_signin_showdlg();">Sign IN</span> ';
	
	document.getElementById('mainmenu').innerHTML= menu;
	gs_showstatistics();
}

function gs_showstatistics()
{
	mainAjax('stats=show', 
		function (ares){
			document.getElementById('gamecontent').innerHTML= ares;
	});	
}

function menuItemClick(gID)
{
	document.getElementById('gamecontent').innerHTML = '';
	
	mainAjax('gameID='+gID+'&option=getrate', studio_parse); // get rating for game 

	// change menu
	if(activegame!= null)
		document.getElementById('mgi_'+activegame).className = '';

	showdiv('ngs_'+gID); // TODO setting game divs
	
	activegame = gID;
	document.getElementById('mgi_'+activegame).className = 'menuactive';
	
	gs_showComment();
	document.getElementById('gameattrib').style.display = 'inline-block';
	var runner = 'run_game_'+gID;
	try {
		window[runner](); 
	}
	catch(err) {
		alert('An error starting game id "'+gID+'":\nLoader: '+runner+'\nMessage: '+err.message);
	}
}

function gs_showComment()
{
	mainAjax('gameID='+activegame+'&option=commentandscore', function (comandsco){
		var regex = /<!--\scom\s-->([\s|\S|\n]*)<!--\s\/com\s-->[\n|\s|\S]*<!--\ssco\s-->([\s|\S|\n]*)<!--\s\/sco\s-->/gm
		var resu = regex.exec(comandsco);
		
		document.getElementById('scores').innerHTML = '<i>You are first playing this game!</i>';
		document.getElementById('comments').innerHTML = '<i>Add comment as first</i>';

		if(resu==null)
			return;
		
		if(gs_replycount(resu[1])>0)
			document.getElementById('comments').innerHTML = resu[1];
		
		if(gs_replycount(resu[2])>0)
			document.getElementById('scores').innerHTML = resu[2];
	})
}

function gs_replycount(data) 
{
	var regex = /<!--ic:(\d+)-->/g;
	var res = regex.exec(data); 
	if(res==null)
		return 0;
	return parseInt(res[1]);
}

function showdiv(divid)
{
	el = document.getElementById(divid);
	
	if(prevdiv)
		prevdiv.style.display = 'none';
	
	prevdiv = el;
	prevdiv.style.display = 'block';
}

function studio_parse(resp) // an entry point for json DATA
{
	var dat = JSON.parse(resp);
	console.log('studio_parse received: ', dat);
	
	if(dat.error)
	{
		console.log('error: '+dat.error);
		alert('error: '+dat.error);
	}

	if(dat.gamelist)
		initGameList(dat.gamelist);

	if(dat.username) {
		dat.signed = true;
		gs_setsigned(dat);
	}
	
	if(dat.auth)
	{
		gs_signin(dat.auth);
		if(dat.auth.signedout)
			location.reload();
	}
		
	
	if(dat.gameout) // game logic run
		gs_gamelogicmain(dat.gameout);
	
	if(dat.rating) // OK
	{
		gs_setrate(dat.rating.game, 'ratinggold');
		gs_setrate(dat.rating.user?dat.rating.user:0, 'ratte');
	}
	
	if(dat.tooManyTries) 
		alert("You cant play '"+dat.tooManyTries+"' moore times due to limit of play.");
		
}

function gs_gamelogicmain(gamedata) // put data to game js, estimate won / loose game
{
	var runner = 'action_game_'+activegame; // dynamically load game javascript
	try {
		window[runner](gamedata); // put data to game js
	}
	catch(err) {
		var errmsg = 'An error ocured executing game "'+gamedata.game+'":\nLoader: '+runner+'\nMessage: '+err.message;
		console.error(errmsg);
	    alert(errmsg);
	}

	if(gamedata.won) // we won game
	{
		var el = document.getElementById("winner"); // show won message
		el.className = el.className.replace('mfadeio', '').trim();
		document.getElementById("winscore").innerHTML = gamedata.score;
		setTimeout(function() {document.getElementById("winner").className+=' mfadeio';}, 500); 
		gs_showComment(); // reload commnts and top score
	}
}