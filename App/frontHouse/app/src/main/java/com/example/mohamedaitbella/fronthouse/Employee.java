package com.example.mohamedaitbella.fronthouse;

public class Employee {

    int EmployeeID, StoreID, JobType, Status;
    String FirstName, Lastname, Email, Phone;

    Employee(int EmployeeID, int StoreID, int JobType, int Status, String FirstName, String LastName, String Email, String Phone){
        this.EmployeeID = EmployeeID;
        this.StoreID = StoreID;
        this.JobType = JobType;
        this.Status = Status;
        this.FirstName = FirstName;
        this.Lastname = LastName;
        this.Email = Email;
        this.Phone = Phone;
    }


}
