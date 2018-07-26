var urlBase = '/WEBAPI';
var extension = "aspx";

var userId = 0;
var firstName = "";
var lastName = "";

function doLogin()
{

	userId = 0;
    var $login = $('#UserName');
    var $password = $('#Password');
    
    var jsonPayload = {
        login: $login.val(),
        password: $password.val(),
    }

    var url = urlBase + '/Login.' + extension;
    var payloadString = JSON.stringify(jsonPayload);

	$.ajax({
        type: 'POST',
        url: url,
        data: payloadString,

        success: function(data) {
            userId = data.EmployeeID;
           

            if(userId < 1)
            {
                alert("UserName/Password combination is incorrect")
                return;
            }
            else{
                localStorage.setItem("EmployeeID", userId);
                localStorage.setItem("StoreID", data.StoreID);
                localStorage.setItem("JobType", data.JobType);
                location.href = "homepage.html";
            }
        },

        error: function(error) {
            alert("Error contacting api: " + error);
        }
        
    
    });
    return false;
}