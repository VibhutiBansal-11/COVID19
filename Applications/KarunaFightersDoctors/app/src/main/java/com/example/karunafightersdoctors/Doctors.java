package com.example.karunafightersdoctors;

public class Doctors {

    String doctName, docSpec, docId, docEmail;
    Users users;

    Doctors(){

    }

    public String getDoctName() {
        return doctName;
    }

    public String getDocSpec() {
        return docSpec;
    }

    public String getDocId() {
        return docId;
    }

    public String getDocEmail() {
        return docEmail;
    }

    public Users getUsers() {
        return users;
    }

    public Doctors(String doctName, String docSpec, String docId, String docEmail) {
        this.doctName = doctName;
        this.docSpec = docSpec;
        this.docId = docId;
        this.docEmail = docEmail;
    }
}
