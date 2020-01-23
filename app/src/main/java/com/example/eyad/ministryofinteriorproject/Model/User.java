package com.example.eyad.ministryofinteriorproject.Model;

public class User {

    private String user_number; //root
    private String id;
    private String password;
    private String name;
    private String governorate;
    private String rank;
    private String financialNumber;
    private String state;


    public User(String user_number, String id, String password, String name, String governorate, String rank, String financialNumber, String state) {
        this.user_number = user_number;
        this.id = id;
        this.password = password;
        this.name = name;
        this.governorate = governorate;
        this.rank = rank;
        this.financialNumber = financialNumber;
        this.state = state;
    }

    public User(){

    }

    public String getFinancialNumber() {
        return financialNumber;
    }

    public void setFinancialNumber(String financialNumber) {
        this.financialNumber = financialNumber;
    }

    public String getGovernorate() {
        return governorate;
    }

    public void setGovernorate(String governorate) {
        this.governorate = governorate;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
