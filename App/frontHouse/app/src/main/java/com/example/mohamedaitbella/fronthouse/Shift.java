package com.example.mohamedaitbella.fronthouse;

class GetSchedule{

    Shift[] schedules;
    String error;

    GetSchedule(Shift[] schedules){
        this.schedules = schedules;
    }

}


class Shift {

    int ShiftID, EmployeeID;
    String EmpFirstName, EmpLastName, StartOfShift, EndOfShift;

    Shift(int ScheduleID, int EmployeeID, String StartOfShift, String EndOfShift){
        this.ShiftID = ShiftID;
        this.EmployeeID = EmployeeID;
        this.StartOfShift = StartOfShift;
        this.EndOfShift = EndOfShift;
    }

}
