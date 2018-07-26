package com.example.mohamedaitbella.fronthouse;

import com.google.gson.annotations.SerializedName;

public class GetSchedule{

    Shift[] schedules;
    String error;

    GetSchedule(Shift[] schedules){
        this.schedules = schedules;
    }

}


class Shift {

    int ScheduleID, EmployeeID;
    String EmpFirstName, EmpLastName;
    @SerializedName("StartOfShift")
    String StartTime;
    @SerializedName("EndOfShift")
    String EndTime;

    Shift(int ScheduleID, int EmployeeID, String EmpFirstName, String EmpLastName, String StartOfShift, String EndOfShift){
        this.ScheduleID = ScheduleID;
        this.EmployeeID = EmployeeID;
        this.EmpFirstName = EmpFirstName;
        this.EmpLastName = EmpLastName;
        StartTime = StartOfShift;
        EndTime = EndOfShift;
    }

}
