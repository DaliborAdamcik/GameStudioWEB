function mines_doOpen(caller)
{
	return mines_doaction(caller, 'open')
}

function mines_doMark(caller)
{
	return mines_doaction(caller, 'mark')
}

function mines_doaction(caller, action) {
	minesAjax('action='+action
			+'&row='+caller.getAttribute('data-r')
			+'&col='+caller.getAttribute('data-c') );
	return false;
}

function mines_newgame()
{
	var row = document.getElementById('mine_row').value;
	var col = document.getElementById('mine_col').value;
	var mine = document.getElementById('mine_mine').value;
	minesAjax('action=new&row='+row+'&col='+col+'&mine='+mine);
}


function mines_parse(dat)
{
	if(dat.state)
		document.getElementById('state').innerHTML = dat.state;

	if(dat.nummines)
		document.getElementById('unmark').innerHTML = dat.nummines;

	switch(dat.client)
	{
		case 'field': mines_initField(dat); break;
		case 'mark': 
		case 'open': mines_paintField(dat.clues); break;
	}
}

function mines_initField(dat)
{
	var rows;
	var cols;
	var outer = '<table>';	
	for (rows = 0; rows<dat.row; rows++)
	{
		outer+=  '<tr>';
		for (cols = 0; cols<dat.col; cols++)
		{
			outer+= '<td data-r="'+rows+'"  data-c="'+cols+'" id="ti_'+rows+'_'+cols+
			'" onclick="mines_doOpen(this);" oncontextmenu="return mines_doMark(this);" class="close" />'+				
				'</td>';
		}
		outer+= '</tr>';
	}
	outer+= '</table>';
		
	document.getElementById('gamecontent').innerHTML = outer;
}
function mines_paintField(clues)
{
	clues.forEach(
		function (clue) {
			console.log(clue);
			var el = document.getElementById('ti_'+clue.row+'_'+clue.col); 
			
			if(clue.value && clue.value>0 )
				el.innerHTML = clue.value;
			
			el.className = clue.state;
			if(clue.state=='open')
			{
				el.onclick = function () {};
				el.oncontextmenu = function () {return false;};
			}
		}
	);
}

