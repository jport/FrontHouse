
//Table Table
var date = new Date();
var months = ["January","February","March","April","May","June","July","August","September","October","November","December"];
document.getElementById("TableHead").innerHTML = months[date.getMonth()]+ " "+date.getFullYear()+"<br>"+"Week of "+ThisWeek(0)+"-"+ThisWeek(6);
//var days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
//var hey =date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+"-12:00:00:0000";
//alert(hey);
var mon,tue,wed,thurs,fri,sat,sun;
var monWidth,tueWidth,wedWidth,thursWidth,friWidth,satWidth,sunWidth;
var day =date.getDay();
var damn=date.getDate();
//sunday case


function ThisWeek(a){
  var salt = new Date();
  var ThisDay = salt.getDay();
  var ThisDate = salt.getDate();
  var real=0;
  var space=0;
  if(ThisDay>=a){
    space=ThisDay-a;
    real=ThisDate-space;
  }
  if (ThisDay<a) {
    space=a-ThisDay;
    real=ThisDate+space;
  }
  return real;
}



$(document).ready(function(){
    //Table Table

var date = new Date();
var months = ["January","February","March","April","May","June","July","August","September","October","November","December"];
document.getElementById("TableHead").innerHTML = months[date.getMonth()]+ " "+date.getFullYear()+"<br>"+"Week of "+ThisWeek(0)+"-"+ThisWeek(6);
//var days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
var hey = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+"-12:00:00:0000";

var mon,tue,wed,thurs,fri,sat,sun;
var monWidth,tueWidth,wedWidth,thursWidth,friWidth,satWidth,sunWidth;
var day =date.getDay();
var damn=date.getDate();


function ThisWeek(a){
  var salt = new Date();
  var ThisDay = salt.getDay();
  var ThisDate = salt.getDate();
  var real=0;
  var space=0;

if(ThisDay>=a){
    space=ThisDay-a;
    real=ThisDate-space;
  }
  if (ThisDay<a) {
    space=a-ThisDay;
    real=ThisDate+space;
  }
  return real;
}

var jsonPayLoad = {
    StoreID: localStorage.getItem("StoreID")
}

var payloadString = JSON.stringify(jsonPayLoad)


$.ajax({
    type: 'POST',
    url: 'http://knightfinder.com/WEBAPI/GetSchedule.aspx',
    data: payloadString,

    success:function(data) {
        //alert(data.error)
        var table = document.getElementById("OurTable");

        for(i = 0; i < data.schedules.length; i++ ) {
           var row = table.insertRow(table.rows.length);
           var curCell = row.insertCell(0);
           curItem = document.createElement('text');
           curItem.innerHTML = data.schedules[i].EmpFirstName + data.schedules[i].EmpLastName;
           curCell.appendChild(curItem);

           curCell = row.insertCell(1);
           curItem = document.createElement('text');
           curItem.innerHTML = data.schedules[i].StartOfShift.substring(11, 16) + "-" + data.schedules[i].EndOfShift.substring(11,16);
           curCell.appendChild(curItem);

           curCell = row.insertCell(2);
           curItem = document.createElement('text');
           curItem.innerHTML = data.schedules[i].StartOfShift.substring(11, 16) + "-" + data.schedules[i].EndOfShift.substring(11,16);
           curCell.appendChild(curItem);

           curCell = row.insertCell(3);
           curItem = document.createElement('text');
           curItem.innerHTML = data.schedules[i].StartOfShift.substring(11, 16) + "-" + data.schedules[i].EndOfShift.substring(11,16);
           curCell.appendChild(curItem);

           curCell = row.insertCell(4);
           curItem = document.createElement('text');
           curItem.innerHTML = data.schedules[i].StartOfShift.substring(11, 16) + "-" + data.schedules[i].EndOfShift.substring(11,16);
           curCell.appendChild(curItem);

           curCell = row.insertCell(5);
           curItem = document.createElement('text');
           curItem.innerHTML = data.schedules[i].StartOfShift.substring(11, 16) + "-" + data.schedules[i].EndOfShift.substring(11,16);
           curCell.appendChild(curItem);

           curCell = row.insertCell(6);
           curItem = document.createElement('text');
           curItem.innerHTML = data.schedules[i].StartOfShift.substring(11, 16) + "-" + data.schedules[i].EndOfShift.substring(11,16);
           curCell.appendChild(curItem);

           curCell = row.insertCell(7);
           curItem = document.createElement('text');
           curItem.innerHTML = data.schedules[i].StartOfShift.substring(11, 16) + "-" + data.schedules[i].EndOfShift.substring(11,16);
           curCell.appendChild(curItem);


        }
    },

    error: function(error) {
        alert("error contacting the api")
    }


})


/*var row = table.insertRow(table.rows.length);
var curCell = row.insertCell(0);
curItem = document.createElement('text');
curItem.setAttribute("colspan","2");
curItem.innerHTML = "John";
curCell.appendChild(curItem);

var curCell = row.insertCell(1);
curItem = document.createElement('text');
curItem.innerHTML = "9:00AM-3:00PM";
curCell.appendChild(curItem);

var curCell = row.insertCell(2);
curItem = document.createElement('text');
curItem.innerHTML = "9:00AM-3:00PM";
curCell.appendChild(curItem);

document.getElementById("Tuesday").innerHTML="Tuesday "+ ThisWeek(2);
document.getElementById("Wednesday").innerHTML="Wednesday "+ ThisWeek(3);
document.getElementById("Thursday").innerHTML="Thursday "+ ThisWeek(4);
document.getElementById("Friday").innerHTML="Friday "+ ThisWeek(5);
document.getElementById("Saturday").innerHTML= "Saturday "+ ThisWeek(6);
document.getElementById("Sunday").innerHTML="Sunday " +  ThisWeek(0);
document.getElementById("Monday").innerHTML="Monday "+ ThisWeek(1);
*/
})
/*
/*
alert("made it here");
var tue =(date.getDate());
if(tue>31){
  tue=tue-31;
}
var wed =(date.getDate()+8);
if(wed>31){
  wed=wed-31;
}
var thurs =(date.getDate()+9);
if(thurs>31){
  thurs=thurs-31;
}
var fri=(date.getDate()+10);
if(fri>31){
  fri=fri-31;
}
var sat =(date.getDate()+11);
if(sat>31){
  sat=sat-31;
}
var sun =(date.getDate()+1);
if(sun>31){
  sun=sun-31;

}
var mon =(date.getDate()-1);
if(mon>31){
  mon=mon-31;
}
*/
document.getElementById("Tuesday").innerHTML="Tuesday "+ ThisWeek(2);
document.getElementById("Wednesday").innerHTML="Wednesday "+ ThisWeek(3);
document.getElementById("Thursday").innerHTML="Thursday "+ ThisWeek(4);
document.getElementById("Friday").innerHTML="Friday "+ ThisWeek(5);
document.getElementById("Saturday").innerHTML= "Saturday "+ ThisWeek(6);
document.getElementById("Sunday").innerHTML="Sunday " +  ThisWeek(0);
document.getElementById("Monday").innerHTML="Monday "+ ThisWeek(1);
