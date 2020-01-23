package com.example.eyad.ministryofinteriorproject.Model;

public class Ship {

    private String plateNumber;
    private String id;
    private String governorate;
    private String owner;
    private String type;

    public Ship(String plateNumber, String id, String governorate, String owner, String type) {
        this.plateNumber = plateNumber;
        this.id = id;
        this.governorate = governorate;
        this.owner = owner;
        this.type = type;
    }


    public Ship(){}

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGovernorate() {
        return governorate;
    }

    public void setGovernorate(String governorate) {
        this.governorate = governorate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
