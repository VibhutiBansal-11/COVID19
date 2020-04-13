package com.example.karunafightersdoctors;

import android.net.Uri;

import java.io.File;

public class Patients {

    String patientName, patientId,id;
    int res;

    Patients(){

    }

    public Patients(String patientName, String patientId, String id, int res) {
        this.patientName = patientName;
        this.patientId = patientId;
        this.id = id;
        this.res = res;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getId() {
        return id;
    }

    public int getRes() {
        return res;
    }
}
