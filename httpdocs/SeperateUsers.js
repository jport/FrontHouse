

var check =localStorage.getItem("JobType");

//localStorage.setItem("JobType", 1);
//Hide MANAGER
if (check!=0)
{
document.getElementById("MakeSchedule").style.display="none";
document.getElementById("EmployeeBar").style.display="none";


}
else if (check==0) {

  document.getElementById("Availability").style.display="none";

}
