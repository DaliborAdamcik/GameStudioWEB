//var masterURL='';
var activegame = null;
var prevdiv = null;

function mainAjax(params, callback) { callAjax('SvcGame', params, callback); }
function guessAjax(params) { callAjax('SvcGame', 'gameID='+activegame+'&option=play&'+params, studio_parse); }
function stonesAjax(params) { 	guessAjax(params); }
function minesAjax(params) { 	guessAjax(params); }

function initGameList(resp)
{
	var gamelist;
	gamelist = resp ;//= JSON.parse(resp);
	//console.log(gamelist);
	var menu = '';
	var newgames = '';
	
	gamelist.gamelist.forEach(function (game) {
		menu+='<span onclick="menuItemClick('+game.ID+');" id="mgi_'+game.ID+'">'+game.name+'</span> ';
		
		callAjaxF('SvcGame?gameID='+game.ID+'&option=play', '', function (ares){
			document.getElementById('newgames').innerHTML+=
				'<div id="ngs_'+game.ID+'" style="display: none;">'+ares+'</div>'; }, 'GET');
	})

	menu+='<span id="gs_signin" onclick="gs_signin_div();">Sign IN</span> ';
	
	console.log(newgames);
	document.getElementById('mainmenu').innerHTML= menu;
}

function menuItemClick(gID)
{
	document.getElementById('gamecontent').innerHTML = "";
	
	mainAjax('gameID='+gID+'&option=getrate', studio_parse);

	// change menu
	if(activegame!= null)
		document.getElementById('mgi_'+activegame).className = '';
	

	showdiv('ngs_'+gID);
	activegame = gID;
	document.getElementById('mgi_'+activegame).className = 'menuactive';
	
	gs_showComment();
	document.getElementById('gameattrib').style.display = 'inline-block';
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
	console.log(dat);
	
	if(dat.error)
	{
		alert('error: '+dat.error);
		console.log('error: '+dat.error);
	}
	
	if(dat.gamelist)
		initGameList(dat);
	
	switch (dat.game) {
	case 'mines':
		mines_parse(dat);
		break;

	case 'stones':
		stones_parse(dat);
		break;

	case 'guess':
		guess_parse(dat);
		break;
		
	default:
		//alert('undefined game: '+dat.game);
		break;
	}

	if(dat.won)
	{
		var el = document.getElementById("winner");
		el.className = el.className.replace('mfadeio', '').trim();
		document.getElementById("winscore").innerHTML = dat.score;
		setTimeout(function() {document.getElementById("winner").className+=' mfadeio';}, 500);
		gs_showComment();
	}
	
	if(dat.gamerating)
		gs_setrate(dat.gamerating, 'ratinggold');
	
	if(dat.myrating)
		gs_setrate(dat.myrating, 'ratte');
	else
		gs_setrate(0, 'ratte');
	
	if(dat.username)
	{
		var el = document.getElementById('gs_signin');
		el.innerHTML = dat.username;
		el.onclick= function () {mainAjax("signout=true", studio_parse)};
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

function gs_addComment(comme)
{
	// validate comment here
	var comval = { };
	comval.comm = comme.value;
	mainAjax('gameID='+activegame+'&option=addcomment&comment='+JSON.stringify(comval), function (comms){
		comme.value='';
		gs_showComment();
	})
}

function gs_setrate(rate, objid)
{
	var stars = parseFloat(rate);
	if(stars==='NaN')
		stars = 0;
	
	stars*= 32;
	stars = Math.round(stars);
	document.getElementById(objid).style.width= stars+'px';
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
	if(username.length<3)
	{
		alert("Username must have 3 and more characters.");
		return;
	}
	mainAjax("username="+username, studio_parse);
	gs_signin_div();
}

function gs_ratmousover(eve, own)
{
	var cusize = eve.clientX - own.offsetLeft; 
	document.getElementById('ratte').style.width = cusize+'px';
}

function gs_ratdo(own)
{
	var si = Math.ceil((parseInt(document.getElementById('ratte').style.width) / own.clientWidth)*5);
	mainAjax('gameID='+activegame+'&option=addrate&rate='+si, studio_parse);
}
