var urlBase = 'http://knightfinder.com/WEBAPI';
var extension = "aspx";

var userId = 0;
var firstName = "";
var lastName = "";

function doLogin()
{   alert("before something fucks up");
	userId = 0;
	firstName = "";
	lastName = "";

	var login = document.getElementById("UserName").value;
	var password = document.getElementById("Password").value;
	alert(login);
	alert(password);
	//document.getElementById("loginResult").innerHTML = "";

	var jsonPayload = '{"login" : "' + login + '", "password" : "' + password + '"}';
	var url = urlBase + '/Login.' + extension;
	alert(jsonPayload);
	$.ajax({
		type:'POST',
		url:  url,
		data: jsonPayload,
		success: function(data) {
			userId = data.EmployeeID;
			if(userId < 1)
			{
				alert("User/Password combination incorrect");
				//document.getElementById("loginResult").innerHTML = " User/Password combination incorrect";
				return;
			}
			else
			{
				localStorage.setItem("EmployeeID", userId);
				location.href = "homepage.html";
			}

		},
		error: function(){
			alert("Again, error contacting the API");
		}
	});

	return false;
}
