package com.example.karunafightersdoctors;

public class ReportClass {

    Users users;
    Doctors doctors;
    Patients patients;

    ReportClass(){

    }

    public ReportClass(Users users, Doctors doctors, Patients patients) {
        this.users = users;
        this.doctors = doctors;
        this.patients = patients;
    }

    public Users getUsers() {
        return users;
    }

    public Doctors getDoctors() {
        return doctors;
    }

    public Patients getPatients() {
        return patients;
    }
}
