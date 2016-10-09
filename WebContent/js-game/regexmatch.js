function regexmatch_newgame()
{
	gameAjax('action=new');
}

function regexmatch_parse(dat)
{
	console.log("remach", dat);
	if(dat.newgame)
		regexmatch_initGame(dat);
	
	
	/*
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
	}*/
}

function regexmatch_initGame(dat)
{
	var outer = '<div class="hotwo"><div>Regex to find a word</div><div id="regs"></div><div id="rtries"></div><div></div></div>';
	// TODO: sem dat este policko na slovo
	
	var counter = 0;
	dat.words.forEach(function (word)  {
		outer+=  '<span id="rwo'+counter+'" class="rwrd" onclick="rgm_play(this);"/>'+word+'</span>&nbsp;';
	});
	
	document.getElementById('gamecontent').innerHTML = outer;
}

function rgm_play(sender)
{
	gameAjax('action=tryhit&word='+sender.innerText);
	
	/*if(mem_first!=null && mem_second!= null) // cant click
		return;

	if(mem_first==null)
		mem_first = sender;
	else
	mem_second = sender;
	
	var regex = /[\d]+/g;
	var oid = regex.exec(sender.id);
	if(oid!=null)
	{
		
		console.log("send data:", oid[0]);
	}*/
	
}