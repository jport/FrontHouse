$(document).ready(function() {
       //get all html elements by their id
        var FirstName = $('#FirstName');
        var LastName = $('#LastName');
        var userName = $('#UserName');
        var password = $('#password');
        var email = $('#email');
        var phone = $('#phone');
        var storeID = localStorage.getItem('StoreID');
        
        //on click execute the managers signup
        $('#SignUp').on('click', function () {
           
            //make the payload to send to the database
            var payLoad = {
                FirstName: FirstName.val(),
                LastName: LastName.val(),
                UserName: userName.val(),
                Password: password.val(),
                Email: email.val(),
                Phone : phone.val(),
                storeID : storeID,
                JobType: 0
            };
            var payLoadString = JSON.stringify(payLoad);
           
            //send the json payload using ajax
            $.ajax({
                type: 'POST',
                url: 'http://knightfinder.com/WEBAPI/CreateEmployee.aspx',
                data: payLoadString,
                success: function(newManager) {
                    location.href = "Homepage.html"
                    localStorage.setItem("EmployeeID", newManager.EmployeeID)
                    localStorage.setItem("JobType", newManager.JobType)
                },
                error: function() {
                    alert("error contacting the api \n error code: "+ error);
                }
            });
    
    
    });

});