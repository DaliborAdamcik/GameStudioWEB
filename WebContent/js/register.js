function gs_showregister()
{
	gs_signin_div();
	var regel = document.getElementById('registerme');
	regel.className= regel.className.replace('mfadout', '').trim();
	regel.className+= ' mfadin';
	document.getElementById("gs_reg_name").value='';	
	document.getElementById("gs_reg_pass1").value='';
	document.getElementById("gs_reg_pass2").value='';
	gs_reg_validpass();
	gs_reg_validnick();
}

function gs_hideregister()
{
	var regel = document.getElementById('registerme');
	regel.className= regel.className.replace('mfadin', 'mfadout');
}

function gs_reg_validnick(callsend){

	var img = document.getElementById("val_nick");
	var usr = document.getElementById("gs_reg_name").value.trim();	
	
	if(usr.length<3)
	{
		img.title= "Name must be minimal 3 characters long";
		img.src="img/err.png";
		return false;
	}

	img.title= "Checking name... please wait";
	img.src="img/loader.gif";
	
	mainAjax('checknick='+usr, function(resp){
		var jsn = JSON.parse(resp);
		console.log(jsn);
		if(jsn.usernonexists)
		{
			img.src="img/ok.png";
			img.title= "NickName is OK";
			if(callsend)
				gs_register_send(jsn.nick, callsend);
		}
		else
		{
			img.title= "Nick already exists, please type new one";
			img.src="img/err.png";
		}
	});
}

function gs_reg_validpass()
{
	var img = document.getElementById("val_pass");
	
	var pass1 = document.getElementById("gs_reg_pass1").value.trim();
	var pass2 = document.getElementById("gs_reg_pass2").value.trim();
	
	if(pass1.length<3 || pass2.length<3)
	{
		img.title= "Password must be minimal 3 chars long";
		img.src="img/err.png";
		return false;
	}
	
	if(pass1 != pass2)
	{
		img.title= "Password and his retype is not same";
		img.src="img/err.png";
		return false;
	}
	img.src="img/ok.png";
	img.title= "Passwords is ok";
	return true;
}

function gs_register_send(name, password)
{
	mainAjax('accact=register&name='+name+'&pass='+password, function(resp){
		var json = JSON.parse(resp);
		console.log(json);
		if(json.regmsg==="ok")
		{
			gs_hideregister();
			studio_parse(resp);
			//alert("Registration is OK");
		}
		else
		alert(json.regmsg+' '+json.error);
	});
}

function gs_doregister()
{
	if(!gs_reg_validpass())
		return;
	
	gs_reg_validnick(document.getElementById("gs_reg_pass1").value.trim());
}
