package com.example.mdo3.overhaul;

import android.media.Image;

/**
 * Created by Royal on 3/26/2018.
 *
 * Deprecated but kept in case future change of individual items for a request
 */


public class Item {

    private int id;
    private int transactionId;
    private String name;
    private String description;
    private float weight;
    private float height;
    private float length;
    private float width;
    private byte[] picture;

    public Item(int IdItem, int IdTransaction, String Name, String Description,
                float Weight, float Height, float Length, float Width, byte[] Picture)
    {
        this.id = IdItem;
        this.transactionId = IdTransaction;
        this.name = Name;
        this.description = Description;
        this.weight = Weight;
        this.height = Height;
        this.length = Length;
        this.width = Width;
        this.picture = Picture;
    }

    public int getId() { return this.id; }
    public int getTransactionId() { return this.transactionId; }
    public String getName() { return this.name; }
    public String getDescription() { return this.description; }
    public float getWeight() { return this.weight; }
    public float getHeight() { return this.height; }
    public float getLength() { return this.length; }
    public float getWidth() { return this.width; }
    public byte[] getPicture() { return this.picture; }
}
