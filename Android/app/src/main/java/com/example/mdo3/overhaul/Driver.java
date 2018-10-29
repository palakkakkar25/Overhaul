package com.example.mdo3.overhaul;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Royal on 3/27/2018.
 */

public class Driver implements Serializable{

    private int id;
    private String userName;
    private String name;
    private String phoneNumber;
    private String driverLicenseNumber;
    private byte[] picture;
    private Timestamp dateRegistered;
    private boolean isActive;
    private float avgRating;
    private int numRating;
    private Vehicle vehicle;

    public Driver(int Id, String UserName, String Name, String PhoneNumber, String DriverLicenseNumber, byte[] Picture, Timestamp DateRegistered, boolean IsActive, float AvgRating, int NumRating, Vehicle vehicle)
    {
        this.id = Id;
        this.userName = UserName;
        this.name = Name;
        this.phoneNumber = PhoneNumber;
        this.picture = Picture;
        this.avgRating = AvgRating;
        this.numRating = NumRating;
        this.driverLicenseNumber = DriverLicenseNumber;
        this.dateRegistered = DateRegistered;
        this.isActive = IsActive;
        this.vehicle = vehicle;
    }

    public int getId() {return this.id;}
    public String getUserName() {return this.userName;}
    public String getName() {return this.name;}
    public String getPhoneNumber() {return this.phoneNumber;}
    public String getDriverLicenseNumber() {return this.driverLicenseNumber;}
    public Timestamp getDateRegistered() {return this.dateRegistered;}
    public float getAvgRating() {return this.avgRating;}
    public int getNumRating() {return this.numRating;}
    public Vehicle getVehicle() {return this.vehicle;}
    public byte[] getPicture() {return this.picture;}

    public void setUserName(String userName) { this.userName = userName; }
    public void setName(String name) { this.name = name; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setDriverLicenseNumber(String driverLicenseNumber) { this.driverLicenseNumber = driverLicenseNumber; }
    public void setDateRegistered() { this.dateRegistered = new Timestamp(System.currentTimeMillis()); }
    public void setAvgRating(float avgRating) { this.avgRating = avgRating; }
    public void setNumRating(int numRating) { this.numRating = numRating; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public void setPicture(byte[] picture) { this.picture = picture; }

}
