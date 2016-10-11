function stones_newgame()
{
	var row = 4;//document.getElementById('row').value;
	var col = 4;//document.getElementById('col').value;
	stonesAjax('action=new&row='+row+'&col='+col);
}

function stones_parse(dat)
{
	if(dat.newgame)
		stones_initField(dat.rows, dat.cols);
	
	stones_paint(dat.field);
}

function stones_initField(rowc, colc)
{
	var rows;
	var cols;
	var outer = '<table id="minestone">';	
	for (rows = 0; rows<rowc; rows++)
	{
		outer+=  '<tr>';
		for (cols = 0; cols<colc; cols++)
			outer+= '<td id="ti_'+rows+'_'+cols+'" /></td>';
		outer+= '</tr>';
	}
	outer+= '</table>';
		
	document.getElementById('gamecontent').innerHTML = outer;
}


function stones_paint(clues)
{
	var zero;
	clues.forEach(
		function (clue) {
			var el = document.getElementById('ti_'+clue.row+'_'+clue.col); 
			el.className='close';

			if(clue.val == '0')
			{
				zero = clue;
				el.innerHTML= '';
				el.className='';
			}
			else
			el.innerHTML = clue.val;

			el.onclick = function () {};
			el.oncontextmenu = function () {return false;};
		}
	);
	
	for(var row =  -1; row<=1;row++)
		for(var col = -1; col<=1;col++)
			if(Math.abs(row) != Math.abs(col))
			stones_overzero(zero.row+row, zero.col+col);
}

function stones_overzero(row, col)
{
	try{
		var el = document.getElementById('ti_'+row+'_'+col); 
		if(el.innerHTML != '')
		{
			el.className+=' clickable';
			el.onclick = function () { stonesAjax("move="+this.innerHTML)};
		}
	}
	catch (e) {
	}
}
