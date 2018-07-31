package com.example.mohamedaitbella.fronthouse;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

class Shift implements Comparable<Shift> {

    int ScheduleID = -1, EmployeeID, ShiftStatus = -1;
    String EmpFirstName, EmpLastName;
    @SerializedName("StartOfShift")
    String StartTime;
    @SerializedName("EndOfShift")
    String EndTime;
    String Name;
    String shift = "NOT SET YET";

    Shift(int ScheduleID, int EmployeeID, int ShiftStatus, String EmpFirstName, String EmpLastName, String StartOfShift, String EndOfShift){
        this.ScheduleID = ScheduleID;
        this.EmployeeID = EmployeeID;
        this.EmpFirstName = EmpFirstName;
        this.EmpLastName = EmpLastName;
        StartTime = StartOfShift;
        EndTime = EndOfShift;
        this.ShiftStatus = ShiftStatus;
        Name = EmpFirstName + " " +EmpLastName;
    }

    @Override
    public int compareTo(@NonNull Shift other) {
        return StartTime.substring(0,10).compareTo(other.StartTime.substring(0,10));
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
