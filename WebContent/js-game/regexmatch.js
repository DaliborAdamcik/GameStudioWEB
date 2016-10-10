function regexmatch_newgame()
{
	gameAjax('action=new');
}

function regexmatch_parse(dat)
{
	console.log("remach", dat);
	if(dat.newgame)
		regexmatch_initGame(dat);
	
	if(dat.retid) {
		document.getElementById(dat.retid).className = (dat.hit?'rehit':'remiss');
		if(dat.hit) {
			document.getElementById(dat.retid).onClick = function () {};
			
			document.getElementById("rexy").childNodes.forEach(function (child) {
				if(child.className=='remiss')
					child.className='rwrd';
			});
		}
	}
	
	document.getElementById('rtries').innerText = 'Try #'+dat.tryc;
	//document.getElementById('regs').innerText = 'Regexs count: '+dat.size;
	document.getElementById('regs').innerHTML = dat.regex;
}

function regexmatch_initGame(dat)
{
	var outer = '<div class="hotwo"> <div>Regex to find a word</div><div id="regs"></div> <div id="rtries"></div> </div>';
	outer+= '<div id="rexy">';
	
	
	var counter = 0;
	dat.words.forEach(function (word)  {
		outer+=  '<span id="rwo'+(counter++)+'" class="rwrd" onclick="rgm_play(this);"/>'+word+'</span> ';
	});
	
	document.getElementById('gamecontent').innerHTML = outer+'</div>';
}

function rgm_play(sender)
{
	gameAjax('action=tryhit&word='+encodeURI(sender.innerText)+'&retid='+sender.id);	
}