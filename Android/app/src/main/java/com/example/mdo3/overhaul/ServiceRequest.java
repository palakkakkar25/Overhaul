package com.example.mdo3.overhaul;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by Royal on 3/26/2018.
 */

public class ServiceRequest implements Serializable{

    private int id;
    private int idCustomer;
    private Integer idDriverWhoCompleted;
    private String title;
    private String description;
    private float weight;
    private Timestamp datePosted;
    private Timestamp dateClosed;
    private float price;
    private boolean loadHelp;
    private boolean unloadHelp;
    private byte[] picture;
    private boolean isCompleted;
    private boolean inProgress;

    private String startLocation;
    private String endLocation;

    public ServiceRequest(int Id, int IdCustomer, Integer IdDriverWhoCompleted, String Title, String Description, float Weight, Timestamp DatePosted, Timestamp DateClosed,
                       float Price,  boolean LoadHelp, boolean UnloadHelp, byte[] Picture, boolean IsCompleted, boolean InProgress, String StartLocation, String EndLocation)
    {
        this.id = Id;
        this.idCustomer = IdCustomer;
        this.idDriverWhoCompleted = IdDriverWhoCompleted;
        this.title = Title;
        this.description = Description;
        this.weight = Weight;
        this.datePosted = DatePosted;
        this.isCompleted = IsCompleted;
        this.price = Price;
        this.loadHelp = LoadHelp;
        this.unloadHelp = UnloadHelp;
        this.picture = Picture;
        this.isCompleted = IsCompleted;
        this.inProgress = InProgress;
        this.startLocation = StartLocation;
        this.endLocation = EndLocation;
    }

    public ServiceRequest(int IdCustomer, String Title, String Description, Timestamp DatePosted,
                          boolean loadHelp, boolean unloadHelp, String pickupLocation,
                          String destination, float weight, float price)
    {
        this.idCustomer = IdCustomer;
        this.title = Title;
        this.description = Description;
        this.datePosted = DatePosted;
        this.startLocation = pickupLocation;
        this.endLocation = destination;
        this.loadHelp = loadHelp;
        this.unloadHelp = unloadHelp;
        this.weight = weight;
        this.price = price;
    }

    public int getId() { return this.id; }
    public int getIdCustomer() { return this.idCustomer; }
    public int getIdDriverWhoCompleted() { return  this.idDriverWhoCompleted;}
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public float getWeight() { return this.weight; }
    public float getPrice() { return this.price; }
    public Timestamp getDatePosted() { return this.datePosted; }
    public Timestamp getDateClosed() { return this.dateClosed; }
    public boolean needLoadHelp() { return this.loadHelp; }
    public boolean needUnloadHelp() { return this.unloadHelp; }
    public byte[] getPicture() {return this.picture; }
    public boolean isCompleted() { return this.isCompleted; }
    public boolean isInProgress() {return this.inProgress;}
    public String getStartLocation() { return this.startLocation; }
    public String getEndLocation() { return this.endLocation; }



}
