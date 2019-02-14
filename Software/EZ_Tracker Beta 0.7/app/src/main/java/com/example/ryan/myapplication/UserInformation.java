package com.example.ryan.myapplication;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class UserInformation {

    public String name;
    public String height;
    public String weight;
    public String dob;

    public UserInformation() {

    }

    public UserInformation(String name, String height, String weight, String dob){
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}

