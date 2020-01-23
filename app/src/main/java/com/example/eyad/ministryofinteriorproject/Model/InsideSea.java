package com.example.eyad.ministryofinteriorproject.Model;

import java.util.List;

public class InsideSea{

    private String userName;
    private String userGovernorate;
    private String userRank;
    private String plateNumberShip;
    private String idShip;
    private String ownerShip;
    private String governorateShip;
    private List<Fisher> fishers;
    private String date;
    private String time;


    public InsideSea() {
    }

    public InsideSea(String userName, String userGovernorate, String userRank, String plateNumberShip, String idShip, String ownerShip, String governorateShip, List<Fisher> fishers, String date, String time) {
        this.userName = userName;
        this.userGovernorate = userGovernorate;
        this.userRank = userRank;
        this.plateNumberShip = plateNumberShip;
        this.idShip = idShip;
        this.ownerShip = ownerShip;
        this.governorateShip = governorateShip;
        this.fishers = fishers;
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getGovernorateShip() {
        return governorateShip;
    }

    public void setGovernorateShip(String governorateShip) {
        this.governorateShip = governorateShip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGovernorate() {
        return userGovernorate;
    }

    public void setUserGovernorate(String userGovernorate) {
        this.userGovernorate = userGovernorate;
    }

    public String getUserRank() {
        return userRank;
    }

    public void setUserRank(String userRank) {
        this.userRank = userRank;
    }

    public String getPlateNumberShip() {
        return plateNumberShip;
    }

    public void setPlateNumberShip(String plateNumberShip) {
        this.plateNumberShip = plateNumberShip;
    }

    public String getIdShip() {
        return idShip;
    }

    public void setIdShip(String idShip) {
        this.idShip = idShip;
    }

    public String getOwnerShip() {
        return ownerShip;
    }

    public void setOwnerShip(String ownerShip) {
        this.ownerShip = ownerShip;
    }

    public List<Fisher> getFishers() {
        return fishers;
    }

    public void setFishers(List<Fisher> fishers) {
        this.fishers = fishers;
    }
}
