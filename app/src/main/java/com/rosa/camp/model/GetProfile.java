package com.rosa.camp.model;

import com.google.gson.annotations.SerializedName;

public class GetProfile {

    @SerializedName("Name")
    private String name;
    private  String family;
    private String phone;
    private String email;
    private String password;

    public  GetProfile(String name, String family, String phone,String email,String password){
        this.name=name;
        this.family=family;
        this.phone=phone;
        this.email=email;
        this.password=password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
