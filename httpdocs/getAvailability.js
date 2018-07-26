$(document).ready(function() {
    //var $orders = $('#orders');
    var $employeeID = $('#employeeID')
    var $Table = $('#availabilities')

    //on click execute the managers signup
     $('#getAvailability').on('click', function () {
        var jsonPayload = {
        employeeID : $employeeID.val(),
        }
         //send the json payload using ajax
         var payloadString = JSON.stringify(jsonPayload)
         alert(payloadString)
         $.ajax({
             type: 'POST',
             url: 'http://knightfinder.com/WEBAPI/GetAvailability.aspx',
             data: payloadString,
             success: function(data) {
                for(i = 0 ; i < 7; i++) {
                    if(i == 0){
                        $Table.append("<li> Monday " + data.days[i].StartTime.substring(11, 16) + " " + data.days[i].EndTime.substring(11,16) + "</li>" )
                    } else if (i == 1) {
                        $Table.append("<li> Tuesday " + data.days[i].StartTime.substring(11, 16) + " " + data.days[i].EndTime.substring(11,16) + "</li>")
                    }else if (i == 2) {
                        $Table.append("<li> Wednesday " + data.days[i].StartTime.substring(11, 16) + " " + data.days[i].EndTime.substring(11,16) + "</li>")
                    }
                    else if(i == 3){
                        $Table.append("<li> Thursday " + data.days[i].StartTime.substring(11, 16) + " " + data.days[i].EndTime.substring(11,16) + "</li>")
                    } else if ( i == 4) {
                        $Table.append("<li> Friday " + data.days[i].StartTime.substring(11, 16) + " " + data.days[i].EndTime.substring(11,16) + "</li>")
                    } else if (i == 5) {
                        $Table.append("<li> Saturday " + data.days[i].StartTime.substring(11, 16) + " " + data.days[i].EndTime.substring(11,16) + "</li>")
                    } else if (i == 6) {
                        $Table.append("<li> Sunday " + data.days[i].StartTime.substring(11, 16) + " " + data.days[i].EndTime.substring(11,16) + "</li>")
                    } else {
                        alert(data.error)
                    }
                    
                }
                
            },
             error: function() {
                 alert("error contacting the api \n error code: "+ error);
            }
         });
 
 
    });

});