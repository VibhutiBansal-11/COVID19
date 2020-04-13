package com.example.karunafightersdoctors;

public class Users {
    String nameOFHosp, nameOfPerson, designationOfPErson, email, phoneNum, addre, city, state, pass;

    public Users(){

    }

    public Users(String nameOFHosp, String nameOfPerson, String designationOfPErson, String email, String phoneNum, String addre, String city, String state, String pass) {
        this.nameOFHosp = nameOFHosp;
        this.nameOfPerson = nameOfPerson;
        this.designationOfPErson = designationOfPErson;
        this.email = email;
        this.phoneNum = phoneNum;
        this.addre = addre;
        this.city = city;
        this.state = state;
        this.pass = pass;
    }

    public String getNameOFHosp() {
        return nameOFHosp;
    }

    public String getNameOfPerson() {
        return nameOfPerson;
    }

    public String getDesignationOfPErson() {
        return designationOfPErson;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getAddre() {
        return addre;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPass() {
        return pass;
    }
}
