function gs_showregister()
{
	gs_signin_showdlg();
	var regel = document.getElementById('registerme');
	regel.className= regel.className.replace('mfadout', '').trim();
	regel.className+= ' mfadin';
	document.getElementById("gs_reg_name").value='';	
	document.getElementById("gs_reg_pass1").value='';
	document.getElementById("gs_reg_pass2").value='';
	document.getElementById("gs_reg_mail").value='';
	gs_reg_validpass();
	gs_reg_validnick();
}

function gs_hideregister()
{
	var regel = document.getElementById('registerme');
	regel.className= regel.className.replace('mfadin', 'mfadout');
}

function gs_reg_validnick(callsend){

	var img_nick = document.getElementById("val_nick");
	var img_mail = document.getElementById("val_mail");

	var usr = document.getElementById("gs_reg_name").value.trim();	
	var pass = document.getElementById("gs_reg_pass1").value.trim();	
	var mail = document.getElementById("gs_reg_mail").value.trim();	

	var rgmail = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i
	
	if(usr.length<3)
	{
		img_nick.title= "Name must be minimal 3 characters long";
		img_nick.src="img/err.png";
	}
	else {
		img_nick.title= "Name must be validated online";
		img_nick.src="img/okw.png";
	}

	if(!rgmail.test(mail))
	{
		img_mail.title= "You need to enter a valid email address";
		img_mail.src="img/err.png";
	}
	else {
		img_mail.title= "Email must be validated online";;
		img_mail.src="img/okw.png";
	}

	if(!rgmail.test(mail) || usr.length<3)
		return false;

	img_nick.title= "Checking name... please wait";
	img_nick.src="img/loader.gif";

	img_mail.title= "Checking email address... please wait";
	img_mail.src="img/loader.gif";
	
	mainAjax('checknick='+usr+"&mail="+mail, function(resp){
		var jsn = JSON.parse(resp);
		console.log(jsn);
		
		if(jsn.usernonexists)
		{
			img_nick.src="img/ok.png";
			img_nick.title= "Nick-name is OK";
		}
		else
		{
			img_nick.title= "Nick already exists, please type new one";
			img_nick.src="img/err.png";
		}
		
		if(jsn.mailok)
		{
			if(jsn.mailnotexists) {
				img_mail.src="img/ok.png";
				img_mail.title= "Email is OK";
			}
			else
			{
					img_mail.src="img/err.png";
					img_mail.title= "Email is already registered";
			}
		}
		else
		{
			img_mail.title= "Email have invalid format";
			img_mail.src="img/err.png";
		}

		if(callsend && jsn.usernonexists && jsn.mailnotexists && jsn.mailok) 
			gs_register_send(jsn.nick, pass, mail);
		
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

function gs_register_send(name, password, email)
{
	mainAjax('accact=register&username='+name+'&pass='+password+'&mail='+email, studio_parse);
}

function gs_doregister()
{
	if(!gs_reg_validpass())
		return;
	
	gs_reg_validnick(true);
}
