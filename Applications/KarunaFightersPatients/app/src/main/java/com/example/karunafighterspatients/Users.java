package com.example.karunafighterspatients;

public class Users {
    String name, email, password, address, state, city, phone;

    Users(){}

    public Users(String name, String email, String password, String address, String state, String city, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.state = state;
        this.city = city;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }
}
