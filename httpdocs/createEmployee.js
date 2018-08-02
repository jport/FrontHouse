$(document).ready(function() {
       //get all html elements by their id
        var FirstName = $('#FirstName');
        var LastName = $('#LastName');
        var userName = $('#UserName');
        var password = $('#password');
        var email = $('#email');
        var phone = $('#phone');
        var storeID = localStorage.getItem('StoreID');
        var click=0;

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
                JobType: 1
            };
            var payLoadString = JSON.stringify(payLoad);

            //send the json payload using ajax
            $.ajax({
                type: 'POST',
                url: 'http://knightfinder.com/WEBAPI/CreateEmployee.aspx',
                data: payLoadString,
                success: function(newManager) {
                  $('#FirstName').val("");
                  $('#LastName').val("");
                  $('#UserName').val("");
                  $('#password').val("");
                  $('#email').val("");
                  $('#phone').val("");
                  onclick();

                    //location.href = "createEmployee.html"

                },
                error: function() {
                    alert("error contacting the api \n error code: "+ error);
                }
            });


    });

});
var click=0;
function onclick(){
  click+=1;
  var message="";
  if(clicks==1){
  message="You added an employee!";
    }
  else if (clicks>=1) {
  message="You added another employee!";
    }
    document.getElementById("message").innerHTML=message;
};
//code i referenced https://stackoverflow.com/questions/33138274/change-text-to-everytime-i-click
