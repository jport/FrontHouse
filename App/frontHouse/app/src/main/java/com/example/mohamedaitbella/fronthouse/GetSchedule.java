package com.example.mohamedaitbella.fronthouse;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class GetSchedule{

    Shift[] schedules;
    String error;

    GetSchedule(Shift[] schedules){
        this.schedules = schedules;
    }

}


class Shift implements Comparable<Shift> {

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

    @Override
    public int compareTo(@NonNull Shift other) {
        return StartTime.substring(0,10).compareTo(other.StartTime.substring(0,10));
    }
}
