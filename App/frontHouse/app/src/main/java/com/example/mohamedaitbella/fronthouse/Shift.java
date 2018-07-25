package com.example.mohamedaitbella.fronthouse;

class GetSchedule{

    Shift[] schedules;
    String error;

    GetSchedule(Shift[] schedules){
        this.schedules = schedules;
    }

}


class Shift {

    int ScheduleID, EmployeeID;
    String EmpFirstName, EmpLastName, StartOfShift, EndOfShift;

    Shift(int ScheduleID, int EmployeeID, String EmpFirstName, String EmpLastName, String StartOfShift, String EndOfShift){
        this.ScheduleID = ScheduleID;
        this.EmployeeID = EmployeeID;
        this.EmpFirstName = EmpFirstName;
        this.EmpLastName = EmpLastName;
        this.StartOfShift = StartOfShift;
        this.EndOfShift = EndOfShift;
    }

}
