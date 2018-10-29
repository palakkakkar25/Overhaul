package com.example.mdo3.overhaul;
import java.sql.*;
import java.sql.PreparedStatement;
import android.os.AsyncTask;
import android.os.SystemClock;
import java.util.concurrent.TimeUnit;

import java.util.*;

/**
 * Created by Brandon Royal on 3/23/2018.
 * Data Access Class
 * Currently all insert, edits, deletes, and queries but should split it up
 */

public class DataAccess {

    final private String dbHost = "overhauldb.cweh7b1mnc5s.us-east-1.rds.amazonaws.com";
    final private String dbUserName = "OverHaul";
    final private String dbPassWord = "overhaul123";
    final private String dbName = "OverHaul_Main";
    final private String dbClass = "net.sourceforge.jtds.jdbc.Driver";

    //****ENUMS FOR EVENT DO NOT CHANGE****
    final private int eventCustomerSubmitedRequest = 1;
    final private int eventDriverAcceptedRequest = 2;
    final private int eventDriverArrives = 3;
    final private int eventTripEnds = 4;

    public DataAccess()
    {
        //No params needed currently
    }

    //General connect to database method to be used in all data calls
    private Connection ConnectToDB()
    {
        Connection connection = null;
        String ConnURL = null;
        try {
            Class.forName(this.dbClass);
            ConnURL = "jdbc:jtds:sqlserver://" + this.dbHost + ";"
                    + "databaseName=" + this.dbName + ";user=" + this.dbUserName + ";password="
                    + this.dbPassWord + ";";
            connection = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            System.out.println(se);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }

        return connection;
    }

    public Customer checkCustomerLogin(String UserName, String PassWord)
    {
        try{
            checkCustomerLoginAsync cl =  new checkCustomerLoginAsync(UserName, PassWord);
            return cl.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class checkCustomerLoginAsync extends AsyncTask<Void, Void, Customer>
    {
        String userName;
        String passWord;
        public checkCustomerLoginAsync(String UserName, String PassWord)
        {
            this.userName = UserName;
            this.passWord = PassWord;
        }

        @Override
        protected Customer doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "SELECT id, UserName, Name, PhoneNumber, DateRegistered, isActive FROM CustomerInfo WHERE UserName = ? AND PassWord = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, this.userName);
                pstmt.setString(2, this.passWord);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next() ) {
                    int UserId = rs.getInt("id");
                    String UserName = rs.getString("UserName");
                    String Name = rs.getString("Name");
                    String PhoneNumber = rs.getString("PhoneNumber");
                    Timestamp DateRegistered = rs.getTimestamp("DateRegistered");
                    boolean isActive = rs.getBoolean("isActive");

                    //Query Customer Credit Card
                    String query2 = "SELECT CardNumber, BillingAddress, ExpirationMonth, ExpirationYear, CVV, BillingName FROM CustomerPaymentInfo WHERE id_Customer = ?";
                    pstmt = conn.prepareStatement(query2);
                    pstmt.setString(1, String.valueOf(UserId));
                    rs = pstmt.executeQuery();

                    if(rs.next())
                    {
                        String ccNum = rs.getString("CardNumber");
                        String ccBilling = rs.getString("BillingAddress");
                        String ccExpMonth = rs.getString("ExpirationMonth");
                        String ccExpYear = rs.getString("ExpirationYear");
                        String ccCCV = rs.getString("CVV");

                        Customer customer = new Customer(UserId, UserName,
                                Name, PhoneNumber,
                                DateRegistered, isActive,
                                ccNum, ccBilling,
                                ccExpMonth, ccExpYear,
                                ccCCV);
                        return customer;
                    }
                }

                conn.close();


            } catch (Exception e) {System.out.println("Error Logging In: " + e.toString());}
            return null;
        }
    }

    public Driver checkDriverLogin(String UserName, String PassWord)
    {
        try{
            checkDriverLoginAsync dl =  new checkDriverLoginAsync(UserName, PassWord);
            return dl.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class checkDriverLoginAsync extends AsyncTask<Void, Void, Driver>
    {
        String userName;
        String passWord;
        public checkDriverLoginAsync(String UserName, String PassWord)
        {
            this.userName = UserName;
            this.passWord = PassWord;
        }

        @Override
        protected Driver doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "SELECT id, UserName, Name, PhoneNumber, DriverLicenseNumber, Picture, DateRegistered, isActive, AverageRating, NumberRatings FROM DriverInfo WHERE UserName = ? AND PassWord = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, this.userName);
                pstmt.setString(2, this.passWord);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next() ) {
                    int id = rs.getInt("id");
                    String UserName = rs.getString("UserName");
                    String Name = rs.getString("Name");
                    String PhoneNumber = rs.getString("PhoneNumber");
                    String DriverLicenseNumber = rs.getString("DriverLicenseNumber");
                    byte[] Picture = rs.getBytes("Picture");
                    Timestamp DateRegistered = rs.getTimestamp("DateRegistered");
                    boolean isActive = rs.getBoolean("isActive");
                    float AvgRating = rs.getFloat("AverageRating");
                    int NumRating = rs.getInt("NumberRatings");

                    Vehicle vehicle = null;
                    query = "SELECT id, CarMake, CarModel, CarYear, LicensePlateNumber, LoadCapacity, Picture FROM Vehicle WHERE id_Driver = ?";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, id);
                    rs = pstmt.executeQuery();

                    if (rs.next() )
                    {
                        int idVehicle = rs.getInt("id");
                        String CarMake = rs.getString("CarMake");
                        String CarModel = rs.getString("CarModel");
                        int CarYear = rs.getInt("CarYear");
                        String LicensePlate = rs.getString("LicensePlateNumber");
                        float LoadCapacity = rs.getFloat("LoadCapacity");
                        byte[] CarPicture = rs.getBytes("Picture");
                        vehicle = new Vehicle(idVehicle, id, CarMake, CarModel, CarYear, LicensePlate, LoadCapacity, CarPicture);
                    }
                    Driver driver = new Driver(id, UserName, Name, PhoneNumber, DriverLicenseNumber, Picture, DateRegistered, isActive, AvgRating, NumRating, vehicle);
                    return driver;
                }

                conn.close();
            } catch (Exception e) {System.out.println("Error Logging In: " + e.toString());}
            return null;
        }
    }

    public Boolean insertCustomer(String UserName, String PassWord, String Name, String PhoneNumber, Timestamp DateRegistered, String CardNumber, String BillingAddress,
                                    String ExpMonth, String ExpYear, String CVV, String BillingName)
    {
        try{
            insertCustomerAsync ic =  new insertCustomerAsync(UserName, PassWord, Name, PhoneNumber, DateRegistered, CardNumber, BillingAddress, ExpMonth, ExpYear, CVV, BillingName);
            return ic.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class insertCustomerAsync extends AsyncTask<Void, Void, Boolean>
    {
        private String userName;
        private String passWord;
        private String name;
        private String phoneNumber;
        private Timestamp dateRegistered;
        private String cardNumber;
        private String billingAddress;
        private String expMonth;
        private String expYear;
        private String CVV;
        private String billingName;

        public insertCustomerAsync(String UserName, String PassWord, String Name, String PhoneNumber, Timestamp DateRegistered, String CardNumber, String BillingAddress,
                                   String ExpMonth, String ExpYear, String CVV, String BillingName)
        {
            this.userName = UserName; this.passWord = PassWord; this.name = Name; this.phoneNumber = PhoneNumber; this.dateRegistered = DateRegistered;
            this.cardNumber = CardNumber; this.billingAddress = BillingAddress; this.expMonth = ExpMonth; this.expYear = ExpYear; this.CVV = CVV; this.billingName = BillingName;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "EXEC dbo.usp_InsertCustomer @UserName = ?, @PassWord = ?, @Name = ?, @PhoneNumber = ?, @DateRegistered = ?, " +
                                " @CardNumber = ?, @BillingAddress = ?, @ExpirationMonth = ?, @ExpirationYear = ?, @CVV = ?, @BillingName = ? ";

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, this.userName);
                pstmt.setString(2, this.passWord);
                pstmt.setString(3, this.name);
                pstmt.setString(4, this.phoneNumber);
                pstmt.setTimestamp(5, this.dateRegistered);
                pstmt.setString(6, this.cardNumber);
                pstmt.setString(7, this.billingAddress);
                pstmt.setString(8, this.expMonth);
                pstmt.setString(9, this.expYear);
                pstmt.setString(10, this.CVV);
                pstmt.setString(11, this.billingName);
                int result = pstmt.executeUpdate();
                conn.close();

                if(result == 1)
                    return true;


            } catch (Exception e) {System.out.println("Error Adding User: " + e.toString());}
            return false;
        }

    }

    public Boolean insertDriver(String UserName, String PassWord, String Name, String PhoneNumber, String DriverLicenseNumber, Timestamp DateRegistered,
                                    String CarMake, String CarModel, int CarYear, String LicensePlateNumber, float LoadCapacity,
                                    String BankAccountNumber, String RoutingNumber, String BillingName)
    {
        try{
            insertDriverAsync id =  new insertDriverAsync(UserName, PassWord, Name, PhoneNumber,DriverLicenseNumber, DateRegistered, CarMake, CarModel,
                                        CarYear, LicensePlateNumber, LoadCapacity, BankAccountNumber, RoutingNumber, BillingName);
            return id.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class insertDriverAsync extends AsyncTask<Void, Void, Boolean>
    {
        private String userName;
        private String passWord;
        private String name;
        private String phoneNumber;
        private String driverLicenseNumber;
        private Timestamp dateRegistered;
        private String make;
        private String model;
        private int year;
        private String licensePlate;
        private float loadCapacity;
        private String bankAccountNumber;
        private String routingNumber;
        private String billingName;

        public insertDriverAsync(String UserName, String PassWord, String Name, String PhoneNumber, String DriverLicenseNumber, Timestamp DateRegistered,
                                 String CarMake, String CarModel, int CarYear, String LicensePlateNumber, float LoadCapacity,
                                 String BankAccountNumber, String RoutingNumber, String BillingName)
        {
            this.userName = UserName; this.passWord = PassWord; this.name = Name; this.phoneNumber = PhoneNumber; this.driverLicenseNumber = DriverLicenseNumber; this.dateRegistered = DateRegistered;
            this.make = CarMake; this.model = CarModel; this.year = CarYear; this.licensePlate = LicensePlateNumber; this.loadCapacity = LoadCapacity; this.bankAccountNumber = BankAccountNumber; this.routingNumber = RoutingNumber;
            this.billingName = BillingName;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "EXEC dbo.usp_InsertDriver @UserName = ?, @PassWord = ?, @Name = ?, @PhoneNumber = ?, @DriverLicenseNumber = ?, @DateRegistered = ?, " +
                        " @CarMake = ?, @CarModel = ?, @CarYear = ?, @LicensePlateNumber = ?, @LoadCapacity = ?, @BankAccountNumber = ?, @RoutingNumber = ?, @BillingName = ? ";

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, this.userName);
                pstmt.setString(2, this.passWord);
                pstmt.setString(3, this.name);
                pstmt.setString(4, this.phoneNumber);
                pstmt.setString(5, this.driverLicenseNumber);
                pstmt.setTimestamp(6, this.dateRegistered);
                pstmt.setString(7, this.make);
                pstmt.setString(8, this.model);
                pstmt.setInt(9, this.year);
                pstmt.setString(10, this.licensePlate);
                pstmt.setFloat(11, this.loadCapacity);
                pstmt.setString(12, this.bankAccountNumber);
                pstmt.setString(13, this.routingNumber);
                pstmt.setString(14, this.billingName);
                int result = pstmt.executeUpdate();
                conn.close();
                if(result == 1)
                    return true;

                else
                {
                    System.out.println("Problem adding driver");
                    return false;
                }


            } catch (Exception e) {System.out.println("Error Adding User: " + e.toString());}
            return false;
        }
    }

    public int insertServiceRequest(int idCustomer, String Title, String Description, float Weight, Timestamp DatePosted, float Price,
                                             boolean LoadHelp, boolean UnloadHelp, byte[] Picture, String StartAddress, String EndAddress)
    {
        try{
            insertServiceRequestAsync isr =  new insertServiceRequestAsync(idCustomer, Title, Description, Weight, DatePosted, Price, LoadHelp, UnloadHelp,
                                                    Picture, StartAddress, EndAddress);
            return isr.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return -1;
    }

    private class insertServiceRequestAsync extends AsyncTask<Void, Void, Integer>
    {

        private int idCustomer; private String title; private String description; private float weight; private Timestamp datePosted;
        private float price; private boolean loadHelp; private boolean unloadHelp; private byte[] picture; private String startAddress;
        private String endAddress;

        public insertServiceRequestAsync(int idCustomer, String Title, String Description, float Weight, Timestamp DatePosted, float Price,
                                         boolean LoadHelp, boolean UnloadHelp, byte[] Picture, String StartAddress, String EndAddress)
        {
            this.idCustomer = idCustomer; this.title = Title; this.description = Description; this.weight = Weight; this.datePosted = DatePosted;
            this.price = Price; this.loadHelp = LoadHelp; this.unloadHelp = UnloadHelp; this.picture = Picture; this.startAddress = StartAddress;
            this.endAddress = EndAddress;
        }

        @Override
        protected Integer doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "EXEC dbo.usp_InsertServiceRequest @idCustomer = ?, @Title = ?, @Description = ?, @TotalWeight = ?, @DatePosted = ?," +
                                    " @Price = ?, @LoadHelp = ?, @UnloadHelp = ?, @Picture = ?, @StartLocation = ?, @EndLocation = ?, @idServiceRequest = ? ";
                CallableStatement cs = conn.prepareCall(query);

                //PreparedStatement pstmt = conn.prepareStatement(query);
                cs.setInt(1, this.idCustomer);
                cs.setString(2, this.title);
                cs.setString(3, this.description);
                cs.setFloat(4, this.weight);
                cs.setTimestamp(5, this.datePosted);
                cs.setFloat(6, this.price);
                cs.setBoolean(7, this.loadHelp);
                cs.setBoolean(8, this.unloadHelp);
                cs.setBytes(9, this.picture);
                cs.setString(10, this.startAddress);
                cs.setString(11, this.endAddress);
                cs.registerOutParameter(12, Types.INTEGER);
                cs.executeUpdate();
                int idServiceRequest = cs.getInt(12);
                return idServiceRequest;


            } catch (Exception e) {System.out.println("Error Adding Transaction: " + e.toString());}
            return -1;
        }
    }

    public Boolean insertDriverRating(int idDriver, int idCustomerWhoRated, int idServiceRequest, int rating, String comment, float newDriverAvg, int newDriverCount)
    {
        try{
            insertDriverRatingAsync idr =  new insertDriverRatingAsync(idDriver, idCustomerWhoRated, idServiceRequest, rating, comment, newDriverAvg, newDriverCount);
            return idr.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class insertDriverRatingAsync extends AsyncTask<Void, Void, Boolean>
    {
        private int idDriver;
        private int idCustomerWhoRated;
        private int idServiceRequest;
        private int rating;
        private String comment;
        private float newDriverAvg;
        private int newDriverCount;

        public insertDriverRatingAsync(int idDriver, int idCustomerWhoRated, int idServiceRequest, int rating, String comment, float newDriverAvg, int newDriverCount)
        {
            this.idDriver = idDriver;
            this.idCustomerWhoRated = idCustomerWhoRated;
            this.idServiceRequest = idServiceRequest;
            this.rating = rating;
            this.comment = comment;
            this.newDriverAvg = newDriverAvg;
            this.newDriverCount = newDriverCount;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "EXEC dbo.usp_InsertDriverRating @idDriver = ?, @idCustomerWhoRated = ?, @idServiceRequest = ?, @Rating = ?, @Comment = ?, @NewDriverAverage = ?, " +
                        "@NewDriverCount = ?";

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, this.idDriver);
                pstmt.setInt(2, this.idCustomerWhoRated);
                pstmt.setInt(3, this.idServiceRequest);
                pstmt.setInt(4, this.rating);
                pstmt.setString(5, this.comment);
                pstmt.setFloat(6, this.newDriverAvg);
                pstmt.setInt(7, this.newDriverCount);
                int result = pstmt.executeUpdate();
                conn.close();

                if(result == 1)
                    return true;


            } catch (Exception e) {System.out.println("Error Adding Driver Rating: " + e.toString());}
            return false;
        }

    }

    public ArrayList<ServiceRequest> getServiceRequests()
    {
        try{
            getServiceRequestsAsync gsr =  new getServiceRequestsAsync();
            return gsr.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class getServiceRequestsAsync extends AsyncTask<Void, Void, ArrayList<ServiceRequest>>
    {

        public getServiceRequestsAsync()
        {

        }

        @Override
        protected ArrayList<ServiceRequest> doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query =
                        "SELECT ServiceRequest.id, id_Customer, id_DriverWhoCompleted, Title, Description, TotalWeight, DatePosted, DateClosed, Price, LoadHelp, " +
                                "UnloadHelp, isCompleted, inProgress, StartLocation, EndLocation " +
                        "FROM ServiceRequest LEFT JOIN Location on Location.id_ServiceRequest = ServiceRequest.id";

                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                ArrayList<ServiceRequest> ServiceRequests = new ArrayList<ServiceRequest>();


                while(rs.next())
                {
                    int id = rs.getInt("id");
                    int idCustomer = rs.getInt("id_Customer");
                    Integer idDriverWhoCompleted = rs.getInt("id_DriverWhoCompleted");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    float weight = rs.getFloat("TotalWeight");
                    Timestamp datePosted = rs.getTimestamp("DatePosted");
                    Timestamp dateClosed = rs.getTimestamp("DateClosed");
                    float price = rs.getFloat("Price");
                    boolean loadHelp = rs.getBoolean("LoadHelp");
                    boolean unloadHelp = rs.getBoolean("UnloadHelp");
                    byte[] picture = rs.getBytes("Picture");
                    boolean isCompleted = rs.getBoolean("isCompleted");
                    boolean inProgress = rs.getBoolean("inProgress");
                    String startLocation = rs.getString("StartLocation");
                    String endLocation = rs.getString("EndLocation");

                    ServiceRequest sr = new ServiceRequest(id, idCustomer, idDriverWhoCompleted, title, description, weight, datePosted, dateClosed, price,
                                                                loadHelp, unloadHelp, picture, isCompleted, inProgress, startLocation, endLocation);
                    ServiceRequests.add(sr);
                }

                conn.close();
                return ServiceRequests;


            } catch (Exception e) {System.out.println("Error Adding Driver Rating: " + e.toString());}
            return null;
        }
    }

    public Queue<Driver> getPossibleDrivers()
    {
        try{
            getPossibleDriversAsync gpd =  new getPossibleDriversAsync();
            return gpd.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class getPossibleDriversAsync extends AsyncTask<Void, Void, Queue<Driver>>
    {

        public getPossibleDriversAsync()
        {

        }

        @Override
        protected Queue<Driver> doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query =

                        "SELECT DriverInfo.id, DriverInfo.UserName, DriverInfo.Name, DriverInfo.PhoneNumber, DriverInfo.DriverLicenseNumber, DriverInfo.Picture, " +
                                "DriverInfo.DateRegistered, DriverInfo.IsActive, DriverInfo.AverageRating, DriverInfo.NumberRatings, Vehicle.id [vehicleId], Vehicle.CarMake, Vehicle.CarModel, " +
                                "Vehicle.CarYear, Vehicle.LicensePlateNumber, Vehicle.LoadCapacity, Vehicle.Picture " +
                                "FROM DriverInfo " +
                                "LEFT JOIN Vehicle on Vehicle.id_Driver = DriverInfo.id " +
                                "WHERE DriverInfo.IsActive = 1";


                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                Queue<Driver> DriverQueue = new LinkedList<Driver>();


                while(rs.next())
                {
                    int id = rs.getInt("id");
                    String userName = rs.getString("UserName");
                    String name = rs.getString("Name");
                    String phoneNumber = rs.getString("PhoneNumber");
                    String driverLicenseNumber = rs.getString("DriverLicenseNumber");
                    byte[] picture = rs.getBytes("Picture");
                    Timestamp dateRegistered = rs.getTimestamp("DateRegistered");
                    boolean isActive = rs.getBoolean("IsActive");
                    float averageRating = rs.getFloat("AverageRating");
                    int numberRatings = rs.getInt("NumberRatings");
                    int vehicleId = rs.getInt("vehicleId");
                    String carMake = rs.getString("CarMake");
                    String carModel = rs.getString("CarModel");
                    int carYear = rs.getInt("CarYear");
                    String licensePlateNumber  = rs.getString("LicensePlateNumber");
                    float loadCapacity = rs.getFloat("LoadCapacity");
                    byte[] carPicture = rs.getBytes("Picture");

                    Vehicle vh = new Vehicle(vehicleId, id, carMake, carModel, carYear, licensePlateNumber, loadCapacity, carPicture);

                    Driver dr = new Driver(id, userName, name, phoneNumber, driverLicenseNumber, picture, dateRegistered, isActive, averageRating,
                            numberRatings, vh);
                    DriverQueue.add(dr);
                }

                conn.close();
                return DriverQueue;


            } catch (Exception e) {System.out.println("Error Retrieving Drivers: " + e.toString());}
            return null;
        }
    }


    public Boolean updateDriverMainInfo(int IdDriver, String Name, String PhoneNumber, String DriverLicenseNumber, Timestamp DateRegistered, boolean IsActive)
    {
        try{
            updateDriverMainInfoAsync id =  new updateDriverMainInfoAsync(IdDriver, Name, PhoneNumber,DriverLicenseNumber, DateRegistered, IsActive);
            return id.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class updateDriverMainInfoAsync extends AsyncTask<Void, Void, Boolean>
    {
        private int idDriver;
        private String name;
        private String phoneNumber;
        private String driverLicenseNumber;
        private Timestamp dateRegistered;
        private boolean isActive;

        public updateDriverMainInfoAsync (int IdDriver, String Name, String PhoneNumber, String DriverLicenseNumber, Timestamp DateRegistered, boolean IsActive)
        {
            this.idDriver = IdDriver; this.name = Name; this.phoneNumber = PhoneNumber; this.driverLicenseNumber = DriverLicenseNumber; this.dateRegistered = DateRegistered;
            this.isActive = IsActive;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "EXEC dbo.usp_UpdateDriverMainInfo @IdDriver = ?, @Name = ?, @PhoneNumber = ?, @DriverLicenseNumber = ?, @DateRegistered = ?, @IsActive = ? ";

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, this.idDriver);
                pstmt.setString(2, this.name);
                pstmt.setString(3, this.phoneNumber);
                pstmt.setString(4, this.driverLicenseNumber);
                pstmt.setTimestamp(5, this.dateRegistered);
                pstmt.setBoolean(6, this.isActive);

                int result = pstmt.executeUpdate();
                conn.close();
                if(result == 1)
                    return true;
                else
                {
                    System.out.println("Problem updating driver info");
                    return false;
                }

            } catch (Exception e) {System.out.println("Error Updating info: " + e.toString());}
            return false;
        }
    }

    public Driver checkIfDriverAccepted(int idServiceRequest, int idCustomer)
    {
        try{
            checkIfDriverAcceptedAsync wfa =  new checkIfDriverAcceptedAsync(idServiceRequest, idCustomer);
            return wfa.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class checkIfDriverAcceptedAsync extends AsyncTask<Void, Void, Driver>
    {
        private int idServiceRequest;
        private int idCustomer;
        final private int eventDriverAcceptedRequest = 2;

        public checkIfDriverAcceptedAsync (int IdServiceRequest, int IdCustomer)
        {
            this.idServiceRequest = IdServiceRequest;
            this.idCustomer = IdCustomer;
        }

        @Override
        protected Driver doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                int idDriver = -1;
                int idEventLog = -1;


                String query = "SELECT id, idDriver FROM EventLog where idCustomer = ? and idServiceRequest = ? and idEvent = ? and isActive = 1 ";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, this.idCustomer);
                pstmt.setInt(2, this.idServiceRequest);
                pstmt.setInt(3, this.eventDriverAcceptedRequest);

                ResultSet rss = pstmt.executeQuery();
                if(rss.next())
                {
                    idDriver = rss.getInt("idDriver");
                    idEventLog = rss.getInt("id");
                }

                if(idDriver == -1 || idEventLog == -1)
                {
                    conn.close();
                    return null;
                }


                //Update the Event to not active since it was used
                String query2 = "Update EventLog SET isActive = 0 where id = ? ";
                PreparedStatement pstmt2 = conn.prepareStatement(query2);
                pstmt2.setInt(1, idEventLog);
                int result = pstmt2.executeUpdate();

                //get the Driver profile for the customer to see
                String query3 = "SELECT id, UserName, Name, PhoneNumber, DriverLicenseNumber, Picture, DateRegistered, isActive, AverageRating, NumberRatings FROM DriverInfo WHERE DriverInfo.id = ? ";
                PreparedStatement pstmt3 = conn.prepareStatement(query3);
                pstmt3.setInt(1, idDriver);
                ResultSet rs = pstmt3.executeQuery();
                if (rs.next() ) {
                    int id = rs.getInt("id");
                    String UserName = rs.getString("UserName");
                    String Name = rs.getString("Name");
                    String PhoneNumber = rs.getString("PhoneNumber");
                    String DriverLicenseNumber = rs.getString("DriverLicenseNumber");
                    byte[] Picture = rs.getBytes("Picture");
                    Timestamp DateRegistered = rs.getTimestamp("DateRegistered");
                    boolean isActive = rs.getBoolean("isActive");
                    float AvgRating = rs.getFloat("AverageRating");
                    int NumRating = rs.getInt("NumberRatings");

                    Vehicle vehicle = null;
                    query3 = "SELECT id, CarMake, CarModel, CarYear, LicensePlateNumber, LoadCapacity, Picture FROM Vehicle WHERE id_Driver = ?";
                    pstmt3 = conn.prepareStatement(query3);
                    pstmt3.setInt(1, id);
                    rs = pstmt3.executeQuery();

                    if (rs.next()) {
                        int idVehicle = rs.getInt("id");
                        String CarMake = rs.getString("CarMake");
                        String CarModel = rs.getString("CarModel");
                        int CarYear = rs.getInt("CarYear");
                        String LicensePlate = rs.getString("LicensePlateNumber");
                        float LoadCapacity = rs.getFloat("LoadCapacity");
                        byte[] CarPicture = rs.getBytes("Picture");
                        vehicle = new Vehicle(idVehicle, id, CarMake, CarModel, CarYear, LicensePlate, LoadCapacity, CarPicture);
                    }
                    Driver driver = new Driver(id, UserName, Name, PhoneNumber, DriverLicenseNumber, Picture, DateRegistered, isActive, AvgRating, NumRating, vehicle);
                    conn.close();
                    return driver;
                }
            } catch (Exception e) {System.out.println("Error With Event Log: " + e.toString());}
            return null;
        }
    }

    public ServiceRequest waitForRequest(int idDriver)
    {
        try{
            waitForRequestAsync wfr =  new waitForRequestAsync(idDriver);
            return wfr.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class waitForRequestAsync extends AsyncTask<Void, Void, ServiceRequest>
    {
        private int idDriver;
        final private int eventCustomerSubmittedRequest = 1;
        final private int eventDriverAcceptedRequest = 2;


        public waitForRequestAsync (int IdDriver)
        {
            this.idDriver = IdDriver;
        }

        @Override
        protected ServiceRequest doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                final int MaxTimeInMilliSeconds = 10000;
                double ellapsedTime = 0.0;
                long startTime = SystemClock.elapsedRealtime();
                long endTime = SystemClock.elapsedRealtime();
                int count = 0;

                int idServiceRequest = -1;
                int idEventLog = -1;
                while((endTime - startTime) < MaxTimeInMilliSeconds && count < 20)
                {
                    count++;

                    String query = "SELECT id, idServiceRequest FROM EventLog where idDriver = ? and idEvent = ? and isActive = 1 ";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, this.idDriver);
                    pstmt.setInt(2, this.eventCustomerSubmittedRequest);

                    ResultSet rs= pstmt.executeQuery();
                    if(rs.next())
                    {
                        idServiceRequest = rs.getInt("idServiceRequest");
                        idEventLog = rs.getInt("id");
                        break;
                    }

                    endTime = SystemClock.elapsedRealtime();
                }
                if(idServiceRequest == -1 || idEventLog == -1)
                {
                    conn.close();
                    return null;
                }

                //Update the Event to not active since it was used
                String query2 = "Update EventLog SET isActive = 0 where id = ? ";
                PreparedStatement pstmt2 = conn.prepareStatement(query2);
                pstmt2.setInt(1, idEventLog);
                int result = pstmt2.executeUpdate();

                //get the ServiceRequest for the driver to see
                String query3 =
                        "SELECT ServiceRequest.id, id_Customer, id_DriverWhoCompleted, Title, Description, TotalWeight, DatePosted, DateClosed, Price, LoadHelp, " +
                                " UnloadHelp, isCompleted, inProgress, StartLocation, EndLocation " +
                                " FROM ServiceRequest LEFT JOIN Location on Location.id_ServiceRequest = ServiceRequest.id " +
                                " WHERE ServiceRequest.id = ? ";

                PreparedStatement pstmt3 = conn.prepareStatement(query3);
                pstmt3.setInt(1, idServiceRequest);
                ResultSet rs = pstmt3.executeQuery();
                ServiceRequest sr = null;
                while(rs.next())
                {
                    int id = rs.getInt("id");
                    int idCustomer = rs.getInt("id_Customer");
                    Integer idDriverWhoCompleted = rs.getInt("id_DriverWhoCompleted");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    float weight = rs.getFloat("TotalWeight");
                    Timestamp datePosted = rs.getTimestamp("DatePosted");
                    Timestamp dateClosed = rs.getTimestamp("DateClosed");
                    float price = rs.getFloat("Price");
                    boolean loadHelp = rs.getBoolean("LoadHelp");
                    boolean unloadHelp = rs.getBoolean("UnloadHelp");
                    byte[] picture = null;
                    boolean isCompleted = rs.getBoolean("isCompleted");
                    boolean inProgress = rs.getBoolean("inProgress");
                    String startLocation = rs.getString("StartLocation");
                    String endLocation = rs.getString("EndLocation");

                    sr = new ServiceRequest(id, idCustomer, idDriverWhoCompleted, title, description, weight, datePosted, dateClosed, price,
                            loadHelp, unloadHelp, picture, isCompleted, inProgress, startLocation, endLocation);
                }

                //insert new event into event log saying that the driver accepted the service request
                //right now the driver will auto accept the service request
                String query4 = "INSERT INTO EventLog (idServiceRequest, idCustomer, idDriver, idEvent, Description, isActive) " +
                        " VALUES (?, ?, ?, ?, ?, ?)" ;
                PreparedStatement pstmt4 = conn.prepareStatement(query4);
                pstmt4.setInt(1, idServiceRequest);
                pstmt4.setInt(2, sr.getIdCustomer());
                pstmt4.setInt(3, this.idDriver);
                pstmt4.setInt(4, this.eventDriverAcceptedRequest);
                pstmt4.setString(5, "Driver Accepted the Request");
                pstmt4.setBoolean(6, true);
                int result4 = pstmt4.executeUpdate();

                conn.close();
                return sr;

            } catch (Exception e) {System.out.println("Error With Event Log: " + e.toString());}
            return null;
        }
    }

    public Boolean insertEventLogServiceRequest(int IdCustomer, int IdServiceRequest, int IdDriver)
    {
        try{
            insertEventLogServiceRequestAsync ielsr =  new insertEventLogServiceRequestAsync(IdCustomer, IdServiceRequest, IdDriver);
            return ielsr.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return false;
    }

    private class insertEventLogServiceRequestAsync extends AsyncTask<Void, Void, Boolean>
    {
        private int idCustomer;
        private int idServiceRequest;
        private int idDriver;
        final private int eventCustomerSubmittedRequest = 1;



        public insertEventLogServiceRequestAsync (int IdCustomer, int IdServiceRequest, int IdDriver)
        {
            this.idDriver = IdDriver;
            this.idCustomer = IdCustomer;
            this.idServiceRequest = IdServiceRequest;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "INSERT INTO EventLog (idServiceRequest, idCustomer, idDriver, idEvent, Description, isActive) " +
                        " VALUES (?, ?, ?, ?, ?, ?)" ;
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, this.idServiceRequest);
                pstmt.setInt(2, this.idCustomer);
                pstmt.setInt(3, this.idDriver);
                pstmt.setInt(4, this.eventCustomerSubmittedRequest);
                pstmt.setString(5, "Customer Submitted A Service Request");
                pstmt.setBoolean(6, true);
                int result = pstmt.executeUpdate();


                conn.close();
                return true;

            } catch (Exception e) {System.out.println("Error With Event Log: " + e.toString());}
            return false;
        }
    }

    public Boolean waitForDriverArrival(int IdServiceRequest, int IdDriver, int IdCustomer)
    {
        try{
            waitForDriverArrivalAsync wfda =  new waitForDriverArrivalAsync(IdCustomer, IdServiceRequest, IdDriver);
            return wfda.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return false;
    }

    private class waitForDriverArrivalAsync extends AsyncTask<Void, Void, Boolean>
    {
        private int idServiceRequest;
        private int idCustomer;
        private int idDriver;
        final private int eventDriverArrived = 3;

        public waitForDriverArrivalAsync (int IdCustomer, int IdServiceRequest, int IdDriver)
        {
            this.idServiceRequest = IdServiceRequest;
            this.idCustomer = IdCustomer;
            this.idDriver = IdDriver;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                final int MaxTimeInMilliSeconds = 30000;
                double ellapsedTime = 0.0;
                long startTime = SystemClock.elapsedRealtime();
                long endTime = SystemClock.elapsedRealtime();
                int count = 0;
                int idEventLog = -1;
                while((endTime - startTime) < MaxTimeInMilliSeconds && count < 20)
                {
                    count++;

                    String query = "SELECT id FROM EventLog where idCustomer = ? and idServiceRequest = ? and idEvent = ? and isActive = 1 ";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, this.idCustomer);
                    pstmt.setInt(2, this.idServiceRequest);
                    pstmt.setInt(3, this.eventDriverArrived);

                    ResultSet rs= pstmt.executeQuery();
                    if(rs.next())
                    {
                        idEventLog = rs.getInt("id");
                        break;
                    }

                    endTime = SystemClock.elapsedRealtime();
                    TimeUnit.SECONDS.sleep(1);
                }
                if(idEventLog == -1)
                {
                    conn.close();
                    return false;
                }


                //Update the Event to not active since it was used
                String query2 = "Update EventLog SET isActive = 0 where id = ? ";
                PreparedStatement pstmt2 = conn.prepareStatement(query2);
                pstmt2.setInt(1, idEventLog);
                int result = pstmt2.executeUpdate();

                return true;

            } catch (Exception e) {System.out.println("Error With Event Log: " + e.toString());}
            return null;
        }
    }

    public Boolean waitForTripEnd(int IdServiceRequest)
    {
        try{
            waitForTripEndAsync wfte =  new waitForTripEndAsync(IdServiceRequest);
            return wfte.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return false;
    }

    private class waitForTripEndAsync extends AsyncTask<Void, Void, Boolean>
    {
        private int idServiceRequest;
        final private int eventTripEnded = 4;

        public waitForTripEndAsync (int IdServiceRequest)
        {
            this.idServiceRequest = IdServiceRequest;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                final int MaxTimeInMilliSeconds = 30000;
                double ellapsedTime = 0.0;
                long startTime = SystemClock.elapsedRealtime();
                long endTime = SystemClock.elapsedRealtime();
                int count = 0;
                int idEventLog = -1;
                while((endTime - startTime) < MaxTimeInMilliSeconds && count < 20)
                {
                    count++;

                    String query = "SELECT id FROM EventLog where idServiceRequest = ? and idEvent = ? and isActive = 1 ";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(2, this.idServiceRequest);
                    pstmt.setInt(3, this.eventTripEnded);

                    ResultSet rs= pstmt.executeQuery();
                    if(rs.next())
                    {
                        idEventLog = rs.getInt("id");
                        break;
                    }

                    endTime = SystemClock.elapsedRealtime();
                    TimeUnit.SECONDS.sleep(1);
                }
                if(idEventLog == -1)
                {
                    conn.close();
                    return false;
                }


                //Update the Event to not active since it was used
                String query2 = "Update EventLog SET isActive = 0 where id = ? ";
                PreparedStatement pstmt2 = conn.prepareStatement(query2);
                pstmt2.setInt(1, idEventLog);
                int result = pstmt2.executeUpdate();

                return true;

            } catch (Exception e) {System.out.println("Error With Event Log: " + e.toString());}
            return null;
        }
    }

    public Boolean insertEventLogDriverArrived(int IdCustomer, int IdServiceRequest, int IdDriver)
    {
        try{
            insertEventLogDriverArrivedAsync ieda =  new insertEventLogDriverArrivedAsync(IdCustomer, IdServiceRequest, IdDriver);
            return ieda.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return false;
    }

    private class insertEventLogDriverArrivedAsync extends AsyncTask<Void, Void, Boolean>
    {
        private int idCustomer;
        private int idServiceRequest;
        private int idDriver;
        final private int eventDriverArrived = 3;



        public insertEventLogDriverArrivedAsync (int IdCustomer, int IdServiceRequest, int IdDriver)
        {
            this.idDriver = IdDriver;
            this.idCustomer = IdCustomer;
            this.idServiceRequest = IdServiceRequest;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "INSERT INTO EventLog (idServiceRequest, idCustomer, idDriver, idEvent, Description, isActive) " +
                        " VALUES (?, ?, ?, ?, ?, ?)" ;
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, this.idServiceRequest);
                pstmt.setInt(2, this.idCustomer);
                pstmt.setInt(3, this.idDriver);
                pstmt.setInt(4, this.eventDriverArrived);
                pstmt.setString(5, "Driver Has Arrived");
                pstmt.setBoolean(6, true);
                int result = pstmt.executeUpdate();


                conn.close();
                return true;

            } catch (Exception e) {System.out.println("Error With Event Log: " + e.toString());}
            return false;
        }
    }

    public Boolean insertEventLogTripEnded(int IdCustomer, int IdServiceRequest, int IdDriver)
    {
        try{
            insertEventLogTripEndedAsync iete =  new insertEventLogTripEndedAsync(IdCustomer, IdServiceRequest, IdDriver);
            return iete.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return false;
    }

    private class insertEventLogTripEndedAsync extends AsyncTask<Void, Void, Boolean>
    {
        private int idCustomer;
        private int idServiceRequest;
        private int idDriver;
        final private int eventTripEnded = 4;



        public insertEventLogTripEndedAsync (int IdCustomer, int IdServiceRequest, int IdDriver)
        {
            this.idDriver = IdDriver;
            this.idCustomer = IdCustomer;
            this.idServiceRequest = IdServiceRequest;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "INSERT INTO EventLog (idServiceRequest, idCustomer, idDriver, idEvent, Description, isActive) " +
                        " VALUES (?, ?, ?, ?, ?, ?)" ;
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, this.idServiceRequest);
                pstmt.setInt(2, this.idCustomer);
                pstmt.setInt(3, this.idDriver);
                pstmt.setInt(4, this.eventTripEnded);
                pstmt.setString(5, "Driver Has Arrived");
                pstmt.setBoolean(6, true);
                int result = pstmt.executeUpdate();


                conn.close();
                return true;

            } catch (Exception e) {System.out.println("Error With Event Log: " + e.toString());}
            return false;
        }
    }

    public Driver getDriverById(int idDriver)
    {
        try{
            getDriverByIdAsync gdbi =  new getDriverByIdAsync(idDriver);
            return gdbi.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class getDriverByIdAsync extends AsyncTask<Void, Void, Driver>
    {
        private int idDriver;
        public getDriverByIdAsync(int IdDriver)
        {
            this.idDriver = IdDriver;
        }

        @Override
        protected Driver doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "SELECT id, UserName, Name, PhoneNumber, DriverLicenseNumber, Picture, DateRegistered, IsActive, AverageRating, NumberRatings FROM DriverInfo WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, this.idDriver);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next() ) {
                    int id = rs.getInt("id");
                    String UserName = rs.getString("UserName");
                    String Name = rs.getString("Name");
                    String PhoneNumber = rs.getString("PhoneNumber");
                    String DriverLicenseNumber = rs.getString("DriverLicenseNumber");
                    byte[] Picture = rs.getBytes("Picture");
                    Timestamp DateRegistered = rs.getTimestamp("DateRegistered");
                    boolean isActive = rs.getBoolean("IActive");
                    float AvgRating = rs.getFloat("AverageRating");
                    int NumRating = rs.getInt("NumberRatings");

                    Vehicle vehicle = null;
                    query = "SELECT id, CarMake, CarModel, CarYear, LicensePlateNumber, LoadCapacity, Picture FROM Vehicle WHERE id_Driver = ?";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, id);
                    rs = pstmt.executeQuery();

                    if (rs.next() )
                    {
                        int idVehicle = rs.getInt("id");
                        String CarMake = rs.getString("CarMake");
                        String CarModel = rs.getString("CarModel");
                        int CarYear = rs.getInt("CarYear");
                        String LicensePlate = rs.getString("LicensePlateNumber");
                        float LoadCapacity = rs.getFloat("LoadCapacity");
                        byte[] CarPicture = rs.getBytes("Picture");
                        vehicle = new Vehicle(idVehicle, id, CarMake, CarModel, CarYear, LicensePlate, LoadCapacity, CarPicture);
                    }
                    Driver driver = new Driver(id, UserName, Name, PhoneNumber, DriverLicenseNumber, Picture, DateRegistered, isActive, AvgRating, NumRating, vehicle);
                    return driver;
                }

                conn.close();
            } catch (Exception e) {System.out.println("Error Getting Driver: " + e.toString());}
            return null;
        }
    }

    public Customer getCustomerById(int idCustomer)
    {
        try{
            getCustomerByIdAsync gcbi =  new getCustomerByIdAsync(idCustomer);
            return gcbi.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class getCustomerByIdAsync extends AsyncTask<Void, Void, Customer>
    {
        int idCustomer;
        public getCustomerByIdAsync(int IdCustomer)
        {
            this.idCustomer = IdCustomer;
        }

        @Override
        protected Customer doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "SELECT id, UserName, Name, PhoneNumber, DateRegistered, IsActive FROM CustomerInfo WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, this.idCustomer);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next() ) {
                    int UserId = rs.getInt("id");
                    String UserName = rs.getString("UserName");
                    String Name = rs.getString("Name");
                    String PhoneNumber = rs.getString("PhoneNumber");
                    Timestamp DateRegistered = rs.getTimestamp("DateRegistered");
                    boolean isActive = rs.getBoolean("IsActive");

                    //Query Customer Credit Card
                    String query2 = "SELECT CardNumber, BillingAddress, ExpirationMonth, ExpirationYear, CVV, BillingName FROM CustomerPaymentInfo WHERE id_Customer = ?";
                    pstmt = conn.prepareStatement(query2);
                    pstmt.setString(1, String.valueOf(UserId));
                    rs = pstmt.executeQuery();

                    if(rs.next())
                    {
                        String ccNum = rs.getString("CardNumber");
                        String ccBilling = rs.getString("BillingAddress");
                        String ccExpMonth = rs.getString("ExpirationMonth");
                        String ccExpYear = rs.getString("ExpirationYear");
                        String ccCCV = rs.getString("CVV");

                        Customer customer = new Customer(UserId, UserName,
                                Name, PhoneNumber,
                                DateRegistered, isActive,
                                ccNum, ccBilling,
                                ccExpMonth, ccExpYear,
                                ccCCV);
                        return customer;
                    }
                }

                conn.close();


            } catch (Exception e) {System.out.println("Error Getting Customer: " + e.toString());}
            return null;
        }
    }

    public ServiceRequest getServiceRequestById(int idServiceRequest)
    {
        try{
            getServiceRequestByIdAsync gsrbi =  new getServiceRequestByIdAsync(idServiceRequest);
            return gsrbi.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class getServiceRequestByIdAsync extends AsyncTask<Void, Void, ServiceRequest>
    {
        private int idServiceRequest;
        public getServiceRequestByIdAsync(int IdServiceRequest)
        {
            this.idServiceRequest = IdServiceRequest;
        }

        @Override
        protected ServiceRequest doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query =
                        "SELECT ServiceRequest.id, id_Customer, id_DriverWhoCompleted, Title, Description, TotalWeight, DatePosted, DateClosed, Price, LoadHelp, " +
                                "UnloadHelp, isCompleted, inProgress, StartLocation, EndLocation " +
                                "FROM ServiceRequest LEFT JOIN Location on Location.id_ServiceRequest = ServiceRequest.id " +
                                "WHERE ServiceRequest.id = ?";

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, this.idServiceRequest);
                ResultSet rs = pstmt.executeQuery();

                ServiceRequest sr = null;


                while(rs.next())
                {
                    int id = rs.getInt("id");
                    int idCustomer = rs.getInt("id_Customer");
                    Integer idDriverWhoCompleted = rs.getInt("id_DriverWhoCompleted");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    float weight = rs.getFloat("TotalWeight");
                    Timestamp datePosted = rs.getTimestamp("DatePosted");
                    Timestamp dateClosed = rs.getTimestamp("DateClosed");
                    float price = rs.getFloat("Price");
                    boolean loadHelp = rs.getBoolean("LoadHelp");
                    boolean unloadHelp = rs.getBoolean("UnloadHelp");
                    byte[] picture = rs.getBytes("Picture");
                    boolean isCompleted = rs.getBoolean("isCompleted");
                    boolean inProgress = rs.getBoolean("inProgress");
                    String startLocation = rs.getString("StartLocation");
                    String endLocation = rs.getString("EndLocation");

                    sr = new ServiceRequest(id, idCustomer, idDriverWhoCompleted, title, description, weight, datePosted, dateClosed, price,
                            loadHelp, unloadHelp, picture, isCompleted, inProgress, startLocation, endLocation);
                }

                conn.close();
                return sr;


            } catch (Exception e) {System.out.println("Error Getting Service Request: " + e.toString());}
            return null;
        }
    }

    public Boolean updateCustomerPaymentInfo(int idCustomer, String CardNumber, String ExpMonth, String ExpYear, String CVV)
    {
        try{
            updateCustomerPaymentInfoAsync ic =  new updateCustomerPaymentInfoAsync(idCustomer, CardNumber, ExpMonth, ExpYear, CVV);
            return ic.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class updateCustomerPaymentInfoAsync extends AsyncTask<Void, Void, Boolean>
    {
        private int idCustomer;
        private String cardNumber;
        private String expMonth;
        private String expYear;
        private String CVV;

        public updateCustomerPaymentInfoAsync(int IdCustomer, String CardNumber, String ExpMonth, String ExpYear, String CVV)
        {
            this.idCustomer = IdCustomer; this.cardNumber = CardNumber; this.expMonth = ExpMonth; this.expYear = ExpYear; this.CVV = CVV;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "UPDATE CustomerPaymentInfo SET CardNumber = ?, ExpirationMonth = ?, ExpirationYear = ?, CVV = ? WHERE id_Customer = ?";

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, this.cardNumber);
                pstmt.setString(2, this.expMonth);
                pstmt.setString(3, this.expYear);
                pstmt.setString(4, this.CVV);
                pstmt.setInt(5, this.idCustomer);

                int result = pstmt.executeUpdate();

                //Customer customer = null;
                //customer = getCustomerById(idCustomer);

                conn.close();

                return true;


            } catch (Exception e) {System.out.println("Error Adding User: " + e.toString());}
            return false;
        }
    }

    public Driver updateDriverPaymentInfo(int idDriver, String AccountNumber, String RoutingNumber)
    {
        try{
            updateDriverPaymentInfoAsync ud =  new updateDriverPaymentInfoAsync(idDriver, AccountNumber, RoutingNumber);
            return ud.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class updateDriverPaymentInfoAsync extends AsyncTask<Void, Void, Driver>
    {
        private int idDriver;
        private String accountNumber;
        private String routingNumber;

        public updateDriverPaymentInfoAsync(int IdDriver, String AccountNumber, String RoutingNumber)
        {
            this.idDriver = IdDriver; this.accountNumber = AccountNumber; this.routingNumber = RoutingNumber;
        }

        @Override
        protected Driver doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "UPDATE DriverPaymentInfo SET BankAccountNumber = ?, RoutingNumber = ? WHERE id_Driver = ?";

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, this.accountNumber);
                pstmt.setString(2, this.routingNumber);
                pstmt.setInt(3, this.idDriver);

                int result = pstmt.executeUpdate();

                Driver driver = null;
                driver = getDriverById(idDriver);

                conn.close();

                return driver;


            } catch (Exception e) {System.out.println("Error Adding User: " + e.toString());}
            return null;
        }
    }

    public Boolean updateDriverVehicleInfo(int idDriver, String Make, String Model, int Year, String LicensePlate, float LoadCapacity)
    {
        try{
            updateDriverVehicleInfoAsync ud =  new updateDriverVehicleInfoAsync(idDriver, Make, Model, Year, LicensePlate, LoadCapacity);
            return ud.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return false;
    }

    public Boolean updateDriverVehicleInfo(Vehicle vehicle)
    {
        try{
            updateDriverVehicleInfoAsync ud =  new updateDriverVehicleInfoAsync(vehicle.getIdDriver(), vehicle.getMake(),
                                                vehicle.getModel(), vehicle.getYear(), vehicle.getLicensePlate(), vehicle.getLoadCapacity());
            return ud.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return false;
    }

    private class updateDriverVehicleInfoAsync extends AsyncTask<Void, Void, Boolean>
    {
        private int idDriver;
        private String make;
        private String model;
        private int year;
        private String licensePlate;
        private float loadCapacity;

        public updateDriverVehicleInfoAsync(int IdDriver, String Make, String Model, int Year, String LicensePlate, float LoadCapacity)
        {
            this.idDriver = IdDriver; this.make = Make; this.model = Model; this.year = Year; this.licensePlate = LicensePlate;
            this.loadCapacity = LoadCapacity;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "UPDATE Vehicle SET CarMake = ?, CarModel = ?, CarYear = ?, LicensePlate = ?, LoadCapacity = ? WHERE id_Driver = ?";

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, this.make);
                pstmt.setString(2, this.model);
                pstmt.setInt(3, this.year);
                pstmt.setString(4, this.licensePlate);
                pstmt.setFloat(4, this.loadCapacity);
                pstmt.setInt(5, this.idDriver);

                int result = pstmt.executeUpdate();

                conn.close();

                return true;


            } catch (Exception e) {System.out.println("Error Updating Driver Vehicle: " + e.toString());}
            return false;
        }
    }

    public Boolean insertDriverVehicle(int idDriver, String Make, String Model, int Year, String LicensePlate, float LoadCapacity, byte[] picture)
    {
        try{
            insertDriverVehicleAsync id =  new insertDriverVehicleAsync(idDriver, Make, Model, Year, LicensePlate, LoadCapacity, picture);
            return id.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return false;
    }

    public Boolean insertDriverVehicle(Vehicle vehicle)
    {
        try{
            insertDriverVehicleAsync id =  new insertDriverVehicleAsync(vehicle.getIdDriver(), vehicle.getMake(),
                    vehicle.getModel(), vehicle.getYear(), vehicle.getLicensePlate(), vehicle.getLoadCapacity(), vehicle.getPicture());
            return id.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return false;
    }

    private class insertDriverVehicleAsync extends AsyncTask<Void, Void, Boolean>
    {
        private int idDriver;
        private String make;
        private String model;
        private int year;
        private String licensePlate;
        private float loadCapacity;
        private byte[] picture;

        public insertDriverVehicleAsync(int IdDriver, String Make, String Model, int Year, String LicensePlate, float LoadCapacity, byte[] picture)
        {
            this.idDriver = IdDriver; this.make = Make; this.model = Model; this.year = Year; this.licensePlate = LicensePlate;
            this.loadCapacity = LoadCapacity; this.picture = picture;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "INSERT INTO Vehicle (id_Driver, CarMake, CarModel, CarYear, LicensePlate, LoadCapacity, Picture) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?)" ;

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, this.idDriver);
                pstmt.setString(2, this.make);
                pstmt.setString(3, this.model);
                pstmt.setInt(4, this.year);
                pstmt.setString(5, this.licensePlate);
                pstmt.setFloat(6, this.loadCapacity);
                pstmt.setBytes(7, this.picture);

                int result = pstmt.executeUpdate();

                conn.close();

                return true;

            } catch (Exception e) {System.out.println("Error Inserting Driver Vehicle: " + e.toString());}
            return false;
        }
    }


    public Boolean checkForActiveSRById(int id)
    {
        try{
            checkForActiveSRByIdASync cfsrbi =  new checkForActiveSRByIdASync(id);
            return cfsrbi.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return null;
    }

    private class checkForActiveSRByIdASync extends AsyncTask<Void, Void, Boolean>
    {
        private int id;

        public checkForActiveSRByIdASync(int Id)
        {
            this.id = Id;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "SELECT 1 FROM EventLog WHERE (idDriver = ? OR idCustomer = ?) and isActive = 1";

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, this.id);
                pstmt.setInt(2, this.id);

                ResultSet rs = pstmt.executeQuery();

                if(rs.next())
                {
                    conn.close();
                    return true;
                }

                conn.close();
                return false;



            } catch (Exception e) {System.out.println("Error checking for active sr by id: " + e.toString());}
            return false;
        }
    }

    public Boolean updateServiceRequestCompleted(int idServiceRequest, boolean isCompleted)
    {
        try{
            updateServiceRequestCompletedAsync usr =  new updateServiceRequestCompletedAsync(idServiceRequest, isCompleted);
            usr.execute().get();
        } catch (Exception e) {System.out.println(e);}
        return true;
    }

    private class updateServiceRequestCompletedAsync extends AsyncTask<Void, Void, Boolean>
    {
        private int idServiceRequest;
        private boolean isCompleted;

        public updateServiceRequestCompletedAsync(int IdServiceRequest, boolean IsCompleted)
        {
            this.idServiceRequest = IdServiceRequest;
            this.isCompleted = IsCompleted;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                Connection conn = DataAccess.this.ConnectToDB();

                String query = "UPDATE ServiceRequest SET isCompleted = ? WHERE id = ?";

                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setBoolean(1, this.isCompleted);
                pstmt.setInt(2, this.idServiceRequest);

                ResultSet rs = pstmt.executeQuery();

                conn.close();
                return true;

            } catch (Exception e) {System.out.println("Error Adding User: " + e.toString());}
            return true;
        }
    }

}
