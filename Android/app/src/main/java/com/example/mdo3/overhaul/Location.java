package com.example.mdo3.overhaul;

import java.io.Serializable;

/**
 * Created by Royal on 3/26/2018.
 */

public class Location implements Serializable{

    private int id;
    private int transactionId;
    private String startAddress;
    private String endAddress;
    private boolean isCompleted;

    public Location(int IdLocation, int IdTransaction, String StartAddress, String EndAddress, boolean IsCompleted)
    {
        this.id = IdLocation;
        this.transactionId = IdTransaction;
        this.startAddress = StartAddress;
        this.endAddress = EndAddress;
        this.isCompleted = IsCompleted;
    }

    public int getId() { return this.id; }
    public int getTransactionId() { return this.transactionId; }
    public String getStartAddress() { return this.startAddress; }
    public String getEndAddress() { return this.endAddress; }
    public boolean isCompleted(){ return this.isCompleted; }

}
