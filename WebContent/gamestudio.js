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
	gs_showSocre();
	document.getElementById('gameattrib').style.display = 'inline-block';
}

function gs_showComment()
{
	mainAjax('gameID='+activegame+'&option=comment', function (comms){document.getElementById('comments').innerHTML = comms;})
}

function gs_showSocre()
{
	mainAjax('gameID='+activegame+'&option=score', function (score){document.getElementById('scores').innerHTML = score;})
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
		el.style.visibility = 'visible';
		el.style.opacity = '1';
		setTimeout(function (){	document.getElementById("winner").style.opacity = '0'; }, 3000);
		setTimeout(function (){	document.getElementById("winner").style.visibility = 'hidden';}, 5000);
		document.getElementById("winscore").innerHTML = dat.score;
	}
	
	if(dat.gamerating)
		gs_setrate(dat.gamerating);
	
	//dat.myrating
	
	if(dat.username)
	{
		var el = document.getElementById('gs_signin');
		el.innerHTML = dat.username;
		el.onclick= function () {};
		
	}
	
}

function gs_addComment(comme)
{
	// validate comment here
	var comval = { };
	comval.comm = comme;
	mainAjax('gameID='+activegame+'&option=addcomment&comment='+JSON.stringify(comval), function (comms){
		gs_showComment();
		})
}

function gs_rate(num)
{
	mainAjax('gameID='+activegame+'&option=addrate&rate='+num, studio_parse);
}

function gs_setrate(rate)
{
	var stars = parseFloat(rate);
	if(stars==='NaN')
		stars = 0;
	
	stars*= 32;
	stars = Math.round(stars);
	document.getElementById('ratinggold').style.width= stars+'px';
}

function gs_signin_div(own)
{
	var sig =document.getElementById("signin");
	if(sig.style.opacity > 0)
	{
		sig.style.opacity = '0'; 
		setTimeout(function (){	sig.style.visibility = 'hidden';}, 5000);
	}
	else
	{
		sig.style.visibility = 'visible';
		sig.style.opacity = '1'; 
	}		
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
