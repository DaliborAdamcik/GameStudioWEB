function memorize_newgame()
{
	gameAjax('action=new');
}

function memorize_parse(dat)
{
	console.log(dat);
	if(dat.newgame)
		memorize_initGame(dat);
	
	if(dat.valfor>=0)
		document.getElementById('mery_'+dat.valfor).src = 'img/mery_'+dat.value+'.png';
	
	if(dat.docompare)
	{
		if(dat.equals)
		{
			mem_first.onclick = function () {}
			mem_second.onclick = mem_first.onclick;
			
			mem_first = null;
			mem_second = null;
		}
		else
		setTimeout(function () {

			mem_first.src = 'img/tileback.gif';
			mem_second.src = 'img/tileback.gif';

			mem_first = null;
			mem_second = null;
		}, 2000);
	}
}

function memorize_initGame(dat)
{
	var items;
	var outer = '';	
	for (items = 0; items<dat.size; items++)
	{
		outer+=  '<img src="img/tileback.gif" class="memoitem" id="mery_'+items+'" onclick="memorize_play(this);"/>';
	}
	document.getElementById('gamecontent').innerHTML = outer;
}

var mem_first = null;
var mem_second = null;

function memorize_play(sender)
{
	if(mem_first!=null && mem_second!= null) // cant click
		return;

	if(mem_first==null)
		mem_first = sender;
	else
	mem_second = sender;
	
	var regex = /[\d]+/g;
	var oid = regex.exec(sender.id);
	if(oid!=null)
	{
		gameAjax('action=open&num='+oid[0]);
		console.log("send data:", oid[0]);
	}
	
}