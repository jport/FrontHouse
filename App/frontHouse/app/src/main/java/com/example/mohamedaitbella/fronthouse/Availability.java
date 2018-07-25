package com.example.mohamedaitbella.fronthouse;

public class Availability {

    int AvailabilityID, Day;
    String StartTime, EndTime;

    Availability(int AvailabilityID, int Day, String StartTime, String EndTime){
        this.AvailabilityID = AvailabilityID;
        this.Day = Day;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
    }

}
