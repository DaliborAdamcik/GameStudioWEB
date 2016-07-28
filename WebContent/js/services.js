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
	console.log('set rating for ', objid, stars);
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
