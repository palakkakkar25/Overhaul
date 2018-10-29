package com.example.mdo3.overhaul;

/**
 * Created by Royal on 3/25/2018.
 * UserDetails Object
 */

public class UserDetails {

    private int userId;
    private String userName;
    public  String firstName;
    private String lastName;
    private String PhoneNumber;
    private byte[] Picture;
    private boolean isActive;

    public UserDetails(int id, String un, String fn, String ln, String pn, byte[] pic, boolean ia)
    {
        this.userId = id;
        this.userName = un;
        this.firstName = fn;
        this.lastName = ln;
        this.isActive = ia;
        this.PhoneNumber = pn;
        this.Picture = pic;
    }

    public int getUserId() {return this.userId;}
    public String getUserName() {return this.userName;}
    public String getFirstName() {return this.firstName;}
    public String getLastName() {return this.lastName;}
    public String getPhoneNumber() {return this.PhoneNumber;}
    public byte[] getPicture() {return this.Picture;}
    public boolean getActiveStatus() {return this.isActive;}


    public void printUser()
    {
        System.out.println(this.userId + "\n" + this.userName + "\n" + this.firstName + "\n" + this.lastName + "\n" + this.isActive + "\n");
    }

}
