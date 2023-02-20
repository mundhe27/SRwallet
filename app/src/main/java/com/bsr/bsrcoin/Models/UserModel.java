package com.bsr.bsrcoin.Models;

public class UserModel {
    private final String id;
    private final String name;
    private final String email;
    private final String phone;
    private final String address;
    private final String dob;
    private final String occupation;
    private final String agent;
    private final String aadhar;
    private final String income;

    public UserModel(String id, String name, String email, String phone, String address, String dob, String occupation, String agent, String aadhar, String income) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.occupation = occupation;
        this.agent = agent;
        this.aadhar = aadhar;
        this.income = income;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getAgent() {
        return agent;
    }

    public String getAadhar() {
        return aadhar;
    }

    public String getIncome() {
        return income;
    }
}
