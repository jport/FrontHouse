$(document).ready(function(){


  $("#SignUp").click(function()
  {
    var checker ='';
    var a,b,c,d,e;
    a=$('#FirstName').val();
    b=$('#LastName').val();
    c=$('#State').val();
    d=$('#ZipCode').val();
    e=$('#StoreNumber').val();
    if(a == '' || b == '' || c == '' || d == ''|| e =='')
    {
      alert("Error: You are missing some info");
    }
    else
    {
			signup();

    }
  });




})


function signup(){

alert("hello!");





}
/*

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
	var password = sha1(document.getElementById("Password").value);
	//document.getElementById("loginResult").innerHTML = "";

	var jsonPayload = '{"login" : "' + login + '", "password" : "' + password + '"}';
	var url = urlBase + '/Login.' + extension;

	$.post(url, jsonPayload)
		.done(function(data){
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
		})
		.fail(function(data){
			alert("Error contacting API");
		});

	return false;
}
*/
