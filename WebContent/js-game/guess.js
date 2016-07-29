function guess_newgame(max)
{
	guessAjax('action=new&max='+max);
}

function guess_parse(dat)
{
	if(dat.newgame)
		guess_init(dat.size);
	
	switch(dat.hit)
	{
		case 'lower': guess_removeitems(dat.number, 1); break;
		case 'higher': guess_removeitems(dat.number, -1); break;
	}
	
	if(dat.won)
		{
			guess_removeitems(dat.number, -1);
			guess_removeitems(dat.number, 1);
		}
	
/*	if(dat.hit)
		document.getElementById('iguess').innerHTML = 'I guess a '+ dat.hit +' number';
	else
		document.getElementById('iguess').innerHTML = '';*/
}

function guess_removeitems(from, inc)
{
	var eleid = parseInt(from);
	var incr = parseInt(inc); 
	while(true)
	{
		el = document.getElementById('ti_'+eleid);
		if(!el)
			break;
		//el.style='display: none';
		
		el.className= 'fadeo '+el.className.replace('fadei', '').replace('fadeo', '');
		eleid+=incr;
	}
}

function guess_init(size)
{
	document.getElementById('gamecontent').innerHTML = 
		'<div style="width:500px; height:500px; position: relative;" id="guess"></div>';
	var elg= document.getElementById('guess')
	var outer = '';	
	var elis = 48;
	for (var items = 1; items<=size; items++)
	{
		outer+= '<div class="guessdiv fadei" id="ti_'+items+'" onclick="guess_it('+items+');" '
		var posx = (Math.random() * (elg.clientWidth - elis)).toFixed();
	    var posy = (Math.random() * (elg.clientHeight - elis)).toFixed();
			
	    outer+='style="left: '+posx+'px; top: '+posy+'px; background-color: rgb('+posx+', '+posy+', '+((posx+posy)%10)+'); cursor: pointer;"';
			
		outer+= '>'+items+'</div>';
	}
		
	elg.innerHTML = outer;
}

function guess_it(number)
{
	guessAjax('action=guess&number='+number);
}