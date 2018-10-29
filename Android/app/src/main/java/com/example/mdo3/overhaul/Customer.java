package com.example.mdo3.overhaul;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Royal on 4/5/2018.
 */

public class Customer implements Serializable{

    private int id;
    private String userName;
    private String name;
    private String phoneNumber;
    private boolean isActive;
    private Timestamp dateRegistered;

    //credit card information
    private String ccNumber;
    private String ccMonth;
    private String ccYear;
    private String ccSCode;
    private String ccBilling;


    public Customer(int Id, String UserName, String Name, String PhoneNumber, Timestamp DateRegistered,
                    boolean IsActive, String ccNum, String ccBilling, String ccMonth, String ccYear, String ccSCode)
    {
        this.id = Id;
        this.userName = UserName;
        this.name = Name;
        this.phoneNumber = PhoneNumber;
        this.isActive = IsActive;
        this.dateRegistered = DateRegistered;

        this.ccNumber = ccNum;
        this.ccMonth = ccMonth;
        this.ccYear = ccYear;
        this.ccSCode = ccSCode;
        this.ccBilling = ccBilling;
    }

    public int getId() {return this.id;}
    public String getUserName() {return this.userName;}
    public Timestamp getDateRegistered() {return this.dateRegistered;}
    public boolean getIsActive() {return this.isActive;}
    public String getPhoneNumber() {return this.phoneNumber;}
    public String getName() {return this.name;}

    public String getccNumber() {return this.ccNumber; }
    public String getccMonth() {return this.ccMonth; }
    public String getCcYear() {return this.ccYear; }
    public String getCcSCode() {return this.ccSCode; }
    public String getCcBilling() {return this.ccBilling; }

    public void setCcNumber(String cc) {this.ccNumber = cc; }
    public void setCcMonth(String cc) {this.ccMonth = cc; }
    public void setCcYear(String cc) {this.ccYear = cc; }
    public void setCcSCode(String cc) {this.ccSCode = cc; }
    public void setCcBilling(String cc) {this.ccBilling = cc; }

    }
