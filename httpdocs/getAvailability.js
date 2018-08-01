$(document).ready(function() {
    //var $orders = $('#orders');
    //7/27//Miguel
    var $employeeID = localStorage.getItem("EmployeeID");

    //on click execute the managers signup
        var jsonPayload = {
        employeeID : $employeeID,
        }
         //send the json payload using ajax
         var payloadString = JSON.stringify(jsonPayload)

         $.ajax({
             type: 'POST',
             url: 'http://knightfinder.com/WEBAPI/GetAvailability.aspx',
             data: payloadString,
             success: function(data) {
                 var table = document.getElementById("AvailabilityTable");

                for(i = 0 ; i < data.days.length; i++) {
                   var row = table.insertRow(table.rows.length);
                   var curCell = row.insertCell(0);
                   curItem = document.createElement('text');
                   if(i==0){
                   curItem.innerHTML = "Monday"
                 } else if (i==1) {
                    curItem.innerHTML = "Tuesday"
                 } else if (i==2) {
                    curItem.innerHTML = "Wednesday"
                 } else if (i==3) {
                    curItem.innerHTML = "Thursday"
                 } else if (i==4) {
                    curItem.innerHTML = "Friday"
                 } else if (i==5) {
                    curItem.innerHTML = "Saturday"
                 } else {
                    curItem.innerHTML = "Sunday"
                 }
                 curCell.appendChild(curItem);


                 var curCell = row.insertCell(1);
                 curItem = document.createElement('text');
                 curItem.style.textAlign = "center";
                 curItem.innerHTML=data.days[i].StartTime.substring(11,16);

                 curCell.appendChild(curItem);

                 var curCell = row.insertCell(2);
                 curItem = document.createElement('text');
                 curItem.innerHTML=data.days[i].EndTime.substring(11,16);
                 curCell.appendChild(curItem);

                }

            },
             error: function() {
                 alert("error contacting the api \n error code: "+ error);
            }
         });




});
