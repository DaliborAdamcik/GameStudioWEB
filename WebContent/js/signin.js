var loggeduser = null;

function gs_signin_showdlg() // show sign in / sign out / change password dialog
{
	var sig =document.getElementById("signindlg");

	if(sig.className.search('mfadout')<0 && sig.className.search('mfadin')<0) // first time try signin 
	{
		gs_signin_dlgopen(false);
		sig.className+= ' mfadin'; 
		return;
	}
	
	if(sig.className.search('mfadout')<0)
		sig.className = sig.className.replace('mfadin', 'mfadout'); // close dialog
	else
	{ 
		gs_signin_dlgopen(false);
		sig.className = sig.className.replace('mfadout', 'mfadin'); // open dialog
	}
}

function gs_signin_dlgopen(passchan) // set valid option of signin dialog 
{
	document.getElementById("gs_user_name").value=''; // empty fields
	document.getElementById("gs_user_password").value='';
	document.getElementById("gs_oldpass").value='';
	document.getElementById("gs_newpass1").value='';
	document.getElementById("gs_newpass2").value='';

	// show option
	document.getElementById('signindlg_do').style.display = (loggeduser==null?'block':'none'); // show options in dialog
	document.getElementById('signindlg_ins').style.display = (loggeduser!=null && !passchan ?'block':'none');
	document.getElementById('signindlg_pass').style.display = (passchan?'block':'none');
}

function gs_dochangepass()
{
	var oldp = document.getElementById("gs_oldpass").value.trim();
	var newp1 = document.getElementById("gs_newpass1").value.trim();
	var newp2 = document.getElementById("gs_newpass2").value.trim();
	
	if(oldp.length<1)
	{
		alert('Type old password');
		return;
	}

	if(newp1!=newp2)
	{
		alert('New password and confirmation are not same');
		return;
	}

	if(newp1.length<3)
	{
		alert('Password must be at least 3 characters long.');
		return;
	}
	mainAjax('accact=newpass&pass='+oldp+'&new='+newp1, studio_parse);	
}

function gs_dosignin()
{
	var username = document.getElementById("gs_user_name").value.trim();
	var pass = document.getElementById("gs_user_password").value.trim();
	if(username.length<3 || pass.length<3)
	{
		alert("User name and password must be minimal 3 character long.");
		return;
	}
	mainAjax("accact=signin&username="+username+'&pass='+pass, studio_parse);
}

function gs_dosignout()
{
	mainAjax("accact=signout", studio_parse);	
}

function gs_signin(dat)
{
	if(!dat.result) // bad results 
	{
		alert(dat.message);
		return;
	}
	
	switch(dat.action) // dialog hiding and moore
	{
		case 'signout': 
		case 'newpass': 
		case 'signin': gs_signin_showdlg(); break; 
		case 'register': gs_hideregister(); break; 
	}

	var sigbut = document.getElementById('gs_signin');
	var ratse = document.getElementById('rate');
	var comse = document.getElementById('addcoment');
	
	if(dat.signed)
	{
		loggeduser = dat.username; 
		sigbut.innerHTML = dat.username;
		ratse.style.display='block';
		comse.style.display='block';
	}
	else
	{
		loggeduser = null;  
		sigbut.innerHTML = "Sign IN";
		ratse.style.display='none';
		comse.style.display='none';
	}
}