package com.example.mdo3.overhaul;

import java.io.Serializable;

/**
 * Created by Royal on 4/5/2018.
 */

public class Vehicle implements Serializable{
    private int id;
    private int idDriver;
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private float loadCapacity;
    private byte[] picture;

    // For retrieving a vehicle from the database
    public Vehicle(int Id, int IdDriver, String Make, String Model, int Year, String LicensePlate, float LoadCapacity, byte[] Picture)
    {
        this.id = Id;
        this.idDriver = IdDriver;
        this.make = Make;
        this.model = Model;
        this.year = Year;
        this.licensePlate = LicensePlate;
        this.loadCapacity = LoadCapacity;
        this.picture = Picture;
    }

    // For uploading a vehicle for the first time
    public Vehicle(int IdDriver, String Make, String Model, int Year, String LicensePlate, float LoadCapacity, byte[] Picture)
    {
        this.idDriver = IdDriver;
        this.make = Make;
        this.model = Model;
        this.year = Year;
        this.licensePlate = LicensePlate;
        this.loadCapacity = LoadCapacity;
        this.picture = Picture;
    }

    public int getId() {return this.id;}
    public int getIdDriver() {return this.idDriver;}
    public String getMake() {return this.make;}
    public String getModel() {return this.model;}
    public int getYear() {return this.year;}
    public String getLicensePlate() {return this.licensePlate;}
    public float getLoadCapacity() {return this.loadCapacity;}
    public byte[] getPicture() {return this.picture;}

    public void setMake(String make) { this.make = make; }
    public void setModel(String model) { this.model = model; }
    public void setYear(int year) { this.year = year; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public void setLoadCapacity(float loadCapacity) { this.loadCapacity = loadCapacity; }
    public void setPicture(byte[] picture) { this.picture = picture; }
}
