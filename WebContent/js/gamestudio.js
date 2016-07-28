var activegame = null;
var prevdiv = null;

function mainAjax(params, callback) { callAjax('SvcGame', params, callback); }
function guessAjax(params) { callAjax('SvcGame', 'gameID='+activegame+'&option=play&'+params, studio_parse); }
function stonesAjax(params) { 	guessAjax(params); }
function minesAjax(params) { 	guessAjax(params); }

function initGameList(gamelist)
{
	var menu = '';
	
	gamelist.forEach(function (game) {
		menu+='<span onclick="menuItemClick('+game.ID+');" id="mgi_'+game.ID+'">'+game.name+'</span> ';
		
		callAjaxF('SvcGame?gameID='+game.ID+'&option=play', '', 
			function (ares){
				document.getElementById('newgames').innerHTML+=
				'<div id="ngs_'+game.ID+'" style="display: none;">'+ares+'</div>';
				var regexrun = /<!--runnable([\s|\S|\n]*)-->/g;
				var runsc = regexrun.exec(ares);
				if(runsc!=null)
				{
					var runscript  = document.createElement("script");
			        runscript.text = runsc[1];
			        document.body.appendChild(runscript);
				}
				else
					console.error('no runnable set for game id# '+game.ID);
			}, 'GET'); // ajax
	});// foreach

	menu+='<span onclick="gs_showstatistics();">Statistics</span> ';
	menu+='<span id="gs_signin" onclick="gs_signin_div();">Sign IN</span> ';
	
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
		alert('error: '+dat.error);
		console.log('error: '+dat.error);
	}
	
	if(dat.gamelist)
		initGameList(dat.gamelist);
	
	if(dat.game) // game logic run
	{
		var runner = 'action_game_'+activegame;
		try {
			window[runner](dat); 
		}
		catch(err) {
		    alert('An error ocured executing game "'+dat.game+'":\nLoader: '+runner+'\nMessage: '+err.message);
		}
	}

	if(dat.won)
	{
		var el = document.getElementById("winner");
		el.className = el.className.replace('mfadeio', '').trim();
		document.getElementById("winscore").innerHTML = dat.score;
		setTimeout(function() {document.getElementById("winner").className+=' mfadeio';}, 500);
		gs_showComment();
	}
	
	if(dat.rating) // OK
	{
		gs_setrate(dat.rating.game, 'ratinggold');
		gs_setrate(dat.rating.user?dat.rating.user:0, 'ratte');
	}
	
	if(dat.username)
	{
		var el = document.getElementById('gs_signin');
		el.innerHTML = dat.username;
		el.onclick= function () {if(confirm('Do you want log out?')) mainAjax("accact=signout", studio_parse)};
		document.getElementById('rate').style.display='block';
		document.getElementById('addcoment').style.display='block';
	}

	if(dat.signout)	
	{
		var el = document.getElementById('gs_signin');
		el.innerHTML = "Sign IN";
		el.onclick= function () { gs_signin_div(); };
		document.getElementById('rate').style.display='none';
		document.getElementById('addcoment').style.display='none';
	}
}

function gs_signin_div()
{
	var sig =document.getElementById("signin");

	if(sig.className.search('mfadout')<0 && sig.className.search('mfadin')<0)
	{
		sig.className+= ' mfadin';
		return;
	}

	
	if(sig.className.search('mfadout')<0)
		sig.className = sig.className.replace('mfadin', 'mfadout');
	else
		sig.className = sig.className.replace('mfadout', 'mfadin');
}

function gs_dosignin()
{
	var username = document.getElementById("gs_user_name").value.trim();
	var pass = document.getElementById("gs_user_password").value.trim();
	if(username.length<3 || pass.length<3)
	{
		alert("User name and password must be minimal 3 character long.");
		return;
	}
	mainAjax("accact=signin&username="+username+'&pass='+pass, studio_parse);
	gs_signin_div();
}