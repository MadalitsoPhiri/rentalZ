package com.example.rentalapartmentsfinder.models;

import java.io.Serializable;

public class property implements Serializable {
    private String id;
    private String reporterName;
    private String bedrooms;
    private String date;
    private String notes;
    private String propertyType;
    private String monthlyRent;
    private String furnitureType;

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(String monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public String getFurnitureType() {
        return furnitureType;
    }

    public void setFurnitureType(String furnitureType) {
        this.furnitureType = furnitureType;
    }

    public property(String id,String reporterName, String bedrooms, String date, String notes, String propertyType, String monthlyRent, String furnitureType){
        this.reporterName = reporterName;
        this.bedrooms = bedrooms;
        this.date = date;
        this.notes = notes;
        this.propertyType = propertyType;
        this.monthlyRent = monthlyRent;
        this.furnitureType = furnitureType;
        this.id = id;

    }


    public String getBedrooms() {
        return bedrooms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

}
