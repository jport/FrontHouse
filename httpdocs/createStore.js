$(document).ready(function() {
       //get all html elements by their id
        var name = $('#StoreName');
        var number = $('#StoreNumber');
        var address = $('#StoreAddr');
        var state = $('#State');
        var city = $('#City');
        var zip = $('#Zip');
        //on click execute the stores signup
        $('#SignUp').on('click', function () {
          
            //make the payload to send to the database
            var payLoad = {
                StoreName: name.val(),
                StoreNumber: number.val(),
                Address: address.val(),
                State: state.val(),
                City: city.val(),
                Zip : zip.val(),
            };
            var payLoadString = JSON.stringify(payLoad);
    
            //send the json payload using ajax
            $.ajax({
                type: 'POST',
                url: 'http://knightfinder.com/WEBAPI/CreateStore.aspx',
                data: payLoadString,
                success: function(newStore) {
                    alert("Store : " + newStore.StoreID + " successfully Added to database");
                    localStorage.setItem("StoreID", newStore.StoreID);
                    location.href("makeManager.html");
                },
                error: function() {
                    alert("error contacting the api \n error code: "+ error);
                }
            });
    
    
    });

});