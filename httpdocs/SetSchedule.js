var today = new Date();
var startOfWeek = new Date(today.setDate(today.getDate()-today.getDay()));

function tables(tableid, day){
	var table=tableid;
	var idtag;
	var curDate = new Date(startOfWeek.getFullYear(), startOfWeek.getMonth(), startOfWeek.getDate());
	//assign idtag based on the day
	if(day == "Sunday") {
		idtag = "su";
	} else if (day == "Monday") {
		idtag = "mo";
		curDate.setDate(curDate.getDate()+1);
	} else if (day == "Tuesday"){
		idtag = "tu";
		curDate.setDate(curDate.getDate()+2);
	}else if (day == "Wednesday"){
		idtag = "we";
		curDate.setDate(curDate.getDate()+3);
	}else if (day == "Thursday"){
		idtag = "th";
		curDate.setDate(curDate.getDate()+4);
	}else if (day == "Friday"){
		idtag = "fr";
		curDate.setDate(curDate.getDate()+5);
	}else if (day == "Saturday"){
		idtag = "sa";
		curDate.setDate(curDate.getDate()+6);
	}

	//var row = table.insertRow(table.rows.length);
	for (var i = 0; i < 5; i++) {
		//StartTime
		var row = table.insertRow(table.rows.length);
		curCell = row.insertCell(0);
		var curItem = document.createElement("select");
		curItem.id="StartTime"+idtag+i;
		curItem.className="form-control";

		for (var j = 0; j <= 23; j++) {
			var option= document.createElement("option")
			option.text= j+":00";
			curItem.add(option,curItem[j]);
			//curItem.appendChild.(option);
		}

		curCell.appendChild(curItem);
		setOnChange(curItem, idtag+""+i, curDate);

		//curItem.document.getElementsByTagName('select')[0].options[0].innerHTML;
		//curItem.innerHTML = document.getElementById("example").options[0];
		//EndTime
		curCell = row.insertCell(1);
		curItem = document.createElement('select');
		curItem.id="EndTime"+idtag+i;
		curItem.className="form-control";

		for (var j = 0; j <= 23; j++) {
			var option= document.createElement("option");
			if(j > 0)
				option.text = j+":59";
			else
				option.text = j+":00";
			curItem.add(option,curItem[j]);
			//curItem.appendChild.(option);
		}

		curCell.appendChild(curItem);
		setOnChange(curItem, idtag+""+i, curDate);

		curCell = row.insertCell(2);
		curItem = document.createElement('select');
		curItem.className = "form-control";

		var option = document.createElement("option");
		curItem.id="Employee"+idtag+i;
		option.text = "";
		curItem.add(option,curItem[j]);
		curCell.appendChild(curItem);

	}
}
//alert(document.getElementById("test1").innerHTML);
//alert(document.getElementById("test1").innerHTML);

// make all seven tables for reading the stuff
tables(document.getElementById("SundayTable"), "Sunday");
tables(document.getElementById("MondayTable"), "Monday");
tables(document.getElementById("TuesdayTable"), "Tuesday");
tables(document.getElementById("WednesdayTable"), "Wednesday");
tables(document.getElementById("ThursdayTable"), "Thursday");
tables(document.getElementById("FridayTable"), "Friday");
tables(document.getElementById("SaturdayTable"), "Saturday");

var startcheck='0:00';
var date = new Date();
var hey = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" 12:00:00:0000";
var ids = ["su", "mo", "tu", "we", "th", "fr", "sa"];

$("#Submit").on('click', function() {
	payload = {StoreID: localStorage.getItem("StoreID"), schedules:[]};


	for(j = 0; j < ids.length; j++)
	{
		id = ids[j];
		curDate = new Date(startOfWeek.getFullYear(), startOfWeek.getMonth(), startOfWeek.getDate());
		curDate.setDate(curDate.getDate()+j);
		for(i = 0; i < 5; i++)
		{
			startVal = $("#StartTime"+id+i).val();
			endVal = $("#EndTime"+id+i).val();
			val = $('#Employee'+id+i).val();
			if(startVal == startcheck || endVal == startcheck) continue;

			mini = {
				EmployeeID: val,
				StartOfShift: curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+curDate.getDate()+ " " + startVal + ":00",
				EndOfShift: curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+curDate.getDate()+" "+ endVal + ":00"
			};

			payload.schedules.push(mini);
		}
	}

	payloadString = JSON.stringify(payload);
	alert(payloadString);
	$.ajax({
		type: 'POST',
		url: 'http://knightfinder.com/WEBAPI/SetSchedule.aspx',
		data: payloadString,
		success: function(data) {
			alert("test: " + data.error);
		}
	});
});

//reference the sunday table

/*$("#getTimes").on('click', function() {
	for(i = 0 ; i <= 4; i++) {
		if($("#StartTimesu"+i).val() != startcheck || $("#EndTimesu"+i).val() != startcheck) {
			//alert(date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+"-"+ $("#StartTimesu"+i).val()+ ":00:0000");
			var payload = {
				StoreID: (localStorage.getItem("StoreID")),
				StartDate: date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+ " " + $("#StartTimesu"+i).val() + ":00",
				EndDate: date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+ $("#EndTimesu"+i).val() + ":00"
			}

			doTheThing(i, JSON.stringify(payload));

		}
	}
});*/

/*
function doTheThing(i, payloadString) {
	$.ajax({
		type: 'POST',
		url: 'http://knightfinder.com/WEBAPI/GetEmployeesAvailBtwn.aspx',
		data: payloadString,
		success: function(Employees, msg, xhr) {
			$('#Employeesu'+i).children().remove().end().append("<option></option>");
			for(j = 0 ; j < Employees.employees.length; j++) {
				alert(i);
				var curItem = document.getElementById("Employeesu"+i);

				var option = document.createElement("option");
				option.text =  Employees.employees[j].FirstName + " " + Employees.employees[j].LastName;
				curItem.add(option, curItem[j]);
			}
		}
	});
}*/

function setOnChange(item, id, curDate) {
	item.onchange = function()
	{
		startVal = $("#StartTime"+id).val();
		endVal = $("#EndTime"+id).val();

		if(startVal == startcheck || endVal == startcheck) return;

		var payload = {
			StoreID: (localStorage.getItem("StoreID")),
			StartDate: curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+curDate.getDate()+ " " + startVal + ":00",
			EndDate: curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+curDate.getDate()+" "+ endVal + ":00"
		}

		payloadString = JSON.stringify(payload);
		$.ajax({
			type: 'POST',
			url: 'http://knightfinder.com/WEBAPI/GetEmployeesAvailBtwn.aspx',
			data: payloadString,
			success: function(Employees, msg, xhr) {
				$('#Employee'+id).children().remove().end().append("<option></option>");
				for(j = 0 ; j < Employees.employees.length; j++) {
					var curItem = document.getElementById("Employee"+id);
					var option = document.createElement("option");
					option.text =  Employees.employees[j].FirstName + " " + Employees.employees[j].LastName;
					option.value = Employees.employees[j].EmployeeID;
					curItem.add(option, curItem[j]);
				}
			}
		});
	}
}
