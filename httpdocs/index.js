
var urlBase = 'http://knightfinder.com/WEBAPI';
var extension = "aspx";

var userId = 0;
var firstName = "";
var lastName = "";

function doLogin()
{

	userId = 0;
	firstName = "";
	lastName = "";

	var login = document.getElementById("UserName").value;
	var password = document.getElementById("Password").value;
	//document.getElementById("loginResult").innerHTML = "";

	var jsonPayload = '{"login" : "' + login + '", "password" : "' + password + '"}';
	var url = urlBase + '/Login.' + extension;
alert(jsonPayload);
	$.post(url, jsonPayload)
		.done(function(data){
			userId = data.EmployeeID;
      alert(userID);
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
		})
		.fail(function(error){
			alert("Error:" + eval(error));
		});

	return false;
}
