$(document).ready(function() {
  document.getElementById('FirstName').value = localStorage.getItem("FirstName")
   document.getElementById('LastName').value = localStorage.getItem("LastName")
  document.getElementById('Email').value = localStorage.getItem("Email")
   document.getElementById('Phone').value = localStorage.getItem("Phone")
    var newPassword = $('#password')

    $("#SignUp").on('click', function () {
       
        var payload = {
            FirstName: $('#FirstName').val(),
            LastName: $('#LastName').val(),
            Phone: $('#Phone').val(),
            Email: $('#Email').val(),
            Password: newPassword.val(),
            EmployeeID: localStorage.getItem("EmployeeID"),
            JobType: localStorage.getItem("JobType"),
            Status: 0
                
        }
        
        var JsonPayloadString = JSON.stringify(payload)
      
        $.ajax({
            type: 'POST',
            url: 'http://knightfinder.com/WEBAPI/UpdateEmployee.aspx',
            data: JsonPayloadString,
            
            success: function(newEmployee) {
				localStorage.setItem("FirstName", $('#FirstName').val())
				localStorage.setItem("LastName", $('#LastName').val())
				localStorage.setItem("Email", $('#Email').val())
				localStorage.setItem("Phone", $('#Phone').val())
            },
            error: function() {
                alert("error saving employee \n error code: "+ error);
            }
        });


    });
    

});