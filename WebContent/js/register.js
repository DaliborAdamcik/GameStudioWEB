function gs_showregister()
{
	gs_signin_showdlg();
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
	var mail = document.getElementById("gs_reg_mail").value.trim();	

	
	if(usr.length<3)
	{
		img.title= "Name must be minimal 3 characters long";
		img.src="img/err.png";
		return false;
	}

	img.title= "Checking name... please wait";
	img.src="img/loader.gif";
	
	mainAjax('checknick='+usr+(callsend==true?"&mail="+mail:""), function(resp){
		var jsn = JSON.parse(resp);
		console.log(jsn);
		if(jsn.usernonexists)
		{
			img.src="img/ok.png";
			img.title= "NickName is OK";
			if(callsend && jsn.mailnotexists && jsn.mailok)
				gs_register_send(jsn.nick, callsend);
		}
		else
		{
			img.title= "Nick already exists, please type new one";
			img.src="img/err.png";
		}
	});
}

function gs_reg_validmail(callsend){

	var img = document.getElementById("val_mail");
	var mail = document.getElementById("gs_reg_mail").value.trim();	
	var rgmail = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i
	console.log(mail, !rgmail.test(mail));
	if(!rgmail.test(mail))
	{
		img.title= "You need to enter a valid email address";
		img.src="img/err.png";
		return false;
	}

	img.title= "Checking email address... please wait";
	img.src="img/loader.gif";
	
	
	mainAjax('checknick='+usr, function(resp){
		var jsn = JSON.parse(resp);
		console.log(jsn);
		if(jsn.usernonexists)
		{
			img.src="img/ok.png";
			img.title= "Email is OK";
			if(callsend)
				gs_register_send(jsn.nick, callsend);
		}
		else
		{
			img.title= "Email address is already registered";
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
	mainAjax('accact=register&username='+name+'&pass='+password, studio_parse);
}

function gs_doregister()
{
	if(!gs_reg_validpass())
		return;
	
	gs_reg_validnick(document.getElementById("gs_reg_pass1").value.trim());
}
