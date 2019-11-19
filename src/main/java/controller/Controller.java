package controller;

import database.DatabaseConnectionHandler;
import model.VehicleModel;

//This is the main controller class that will orchestrate everything.
public class Controller {
    private static DatabaseConnectionHandler dbHandler = null;

    //opens browser instance, navigates to web page, and logs in
    public static void start() {
       /* File htmlFile = new File(url);
        try {
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }*/


        // Init jdbc handler
        dbHandler = new DatabaseConnectionHandler();

        // Log in with given credentials
        boolean didConnect = dbHandler.login("ora_jamesens", "a98263510");
        //boolean didConnect = dbHandler.login("ora_aktoriam@stu", "a42603381");

        VehicleModel[] temp = dbHandler.getVehicleInfo("", "", "", "","");
        System.out.printf("here");
    }

    // Adds a new customer
    public static boolean addNewCustomer(String cellphone, String name, String address, String dlicense) {
        return dbHandler.addNewCustomer(cellphone, name, address, dlicense);
    }
    // Adds a new vehicle
    // TODO
    public static int addNewVehicle( ){
        return 0;
    }

    //Returns array of all vehicles
    public static VehicleModel[] viewVehicles() {
        return dbHandler.getVehicleInfo();
    }

    //Return filtered array of vehicles based on specific car type,location,and time interval.
    public static VehicleModel[] viewVehicles(String carType, String location, String city, String startTime, String endTime) {
        return dbHandler.getVehicleInfo(carType, location, city, startTime, endTime);
    }

    // Makes a reservation and returns the confirmation number
    public static int makeReservation(String phoneNumber, String name, String address, String city, String dlicense, String vtname,
                                      String fromDate, String toDate) {
        return dbHandler.makeReservation(phoneNumber, name, address, city, dlicense, vtname, fromDate, toDate);
    }

    // Rents a specific vehicle
    public static int rentVehicle(String vid, String cellphone, String fromDateTime, String toDateTime, String name, String address,
                                  String dLicense, String confNo, String cardName, String cardNo, String expDate) {

        return dbHandler.rentVehicle(vid, cellphone, fromDateTime, toDateTime, name, address, dLicense, confNo, cardName, cardNo, expDate);
    }

    // Returns a vehicle
    public static int returnVehicle(String rid, String returnDateTime, String odometer, Boolean fulltank) {
        return dbHandler.returnVehicle(rid, returnDateTime, odometer, fulltank);
    }

    // Generate daily report for all returns
    public static VehicleModel[] generateReportDailyReturns(String date) {
        return dbHandler.generateReportDailyReturns(date);
    }

    // Generate daily report for all returns at supplied branch
    public static VehicleModel[] generateReportDailyReturns(String date, String city, String location) {
        return dbHandler.generateReportDailyReturns(date, city, location);
    }

    // Generate daily report for all active rentals
    public static VehicleModel[] generateReportDailyRentals(String date) {
        return dbHandler.generateReportDailyRentals(date);
    }

    // Generate daily report for all rentals at supplied branch
    public static VehicleModel[] generateReportDailyRentals(String date, String city, String location) {
        return dbHandler.generateReportDailyRentals(date, city, location);
    }
}



