package controller;

import database.DatabaseConnectionHandler;
import model.*;

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
//        boolean didConnect = dbHandler.login("ora_jamesens", "a98263510");
        boolean didConnect = dbHandler.login("ora_aktoriam", "a42603381");

        // test the methods in DatabaseConnectionHandler class
//        testMethodsInDCHandler();
         testVehicleManipulation();
//        testDailyReportRentWholeCompany();
//        testCustomerManipulation();


        System.out.printf("here");
    }

    public static DatabaseConnectionHandler getDbHandler() {
        return dbHandler;
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
    public static int makeReservation(String phoneNumber, String name, String address, String dlicense, String vid,
                                      String fromDate, String toDate) {
        return dbHandler.makeReservation(phoneNumber, name, address, dlicense, vid, fromDate, toDate);
    }

    // Rents a specific vehicle
    public static int rentVehicle(String vid, String cellphone, String fromDateTime, String toDateTime, String odometer,
                                   String confNo, String cardName, String cardNo, String expDate) {

        return dbHandler.rentVehicle(vid, cellphone, fromDateTime, toDateTime, odometer, confNo, cardName, cardNo, expDate);
    }

    // Returns a vehicle
    public static ReturnConfirmationMessageModel returnVehicle(String rid, String returnDateTime, String odometer, String fulltank) {
        return dbHandler.returnVehicle(rid, returnDateTime,odometer, fulltank);
        // String rid, String returnDateTime, String odometer, String fulltank, String confNo
    }


    public static void testMethodsInDCHandler() {

        VehicleModel[] ddd = dbHandler.getVehicleInfo();

        VehicleModel[] temp = dbHandler.getVehicleInfo("Economy", "UBC", "Vancouver", "2019-01-19", "2019-01-30");
        VehicleModel[] temp1 = dbHandler.getVehicleInfo("", "UBC", "Vancouver", "2019-01-19", "2019-01-30");
        int numOfAvailableCar = dbHandler.getAvailableNumOfVehicle("", "", "", "", "");
        int confNo = dbHandler.makeReservation("1234567890", "asdf", "asdf", "111111111", "10",
                "2200-01-01:00:00", "2200-02-01:00:00");
        ReservationModel reservationModel = dbHandler.getReservation(1);
        int rid = dbHandler.rentVehicle("10", "1234567890", "2200-01-01:00:00", "2200-02-01:00:00", "1234",
                "" + confNo, "Visa", "4444777788889999", "2200-10-10");

        ReturnConfirmationMessageModel rcm = dbHandler.returnVehicle("" + rid, "2200-02-01:00:00", "1244", "T");


        VehicleModel[] vm = dbHandler.generateReportDailyRentalsAllVehicleInfoOnBranch("2019-01-01", "UBC", "Vancouver");
        ReportGroupedByVehilceModel[] rgv = dbHandler.getNumOfVehicleDailyRentalGBVehicleOnBranch("2019-01-01", "UBC", "Vancouver");
        int numRentB = dbHandler.getNumOfVehicleDailyRentalOnBranch("2019-01-01", "UBC", "Vancouver");
        int numnewB = dbHandler.getNumOfVehicleNewlyDailyRentalOnBranch("2019-01-01", "UBC", "Vancouver");

        VehicleModel[] vmreturn = dbHandler.generateReportDailyReturnsAllVehicleInfo("2019-01-07");
        ReportGroupedByVehilceModel[] rgbvreturn = dbHandler.getNumOdVehicleDailyReturnGBVehicle("2019-01-07");
        RevenueReportGroupedByVehilceModel[] rrgbvm = dbHandler.getRevenueDailyReturnGBVehicle("2019-01-07");
        ReportTotalNumAndRevenueGBBranchModel[] total = dbHandler.getTotalNumAndRevenueGBBranch("2019-01-07");

        VehicleModel[] vmreturntes = dbHandler.generateReportDailyReturnsAllVehicleInfoOnBranch("2019-01-07", "UBC", "Vancouver");
        ReportGroupedByVehilceModel[] rgbvreturnB = dbHandler.getNumOdVehicleDailyReturnGBVehicleOnBranch(
                "2019-01-07", "UBC", "Vancouver");
        RevenueReportGroupedByVehilceModel[] rgmvmreturnB = dbHandler.getRevenueDailyReturnGBVehicleOnBranch(
                "2019-01-07", "UBC", "Vancouver");
        ReportTotalNumAndRevenueOnBranchModel resulttt = dbHandler.getTotalRevAndNumRentalsOnBranch(
                "2019-01-07", "UBC", "Vancouver");

        boolean eee = dbHandler.addNewCustomer("9999999990", "John Doe", "123 w ave, van, vc", "112344888");
        RentConfirmationMessageModel rmdd = Controller.dbHandler.getRentConfMessage(2);

    }

    public static void testDailyReportRentWholeCompany() {
        VehicleModel[] rp0 = dbHandler.generateReportDailyRentalsAllVehicleInfo("2019-01-02");
        ReportGroupedByVehilceModel[] rp = dbHandler.getNumOfVehicleDailyRentalGBVehicle("2019-01-02");
        ReportGroupByBranchModel[] rp2 = dbHandler.getNumOfVehicleDailyRentalGBBranch("2019-01-02");
        int numVDayRent = Controller.dbHandler.getNumOfVehicleNewlyDailyRental("2019-01-01");


    }

    public static void testVehicleManipulation() {
        VehicleModel[] all = dbHandler.getVehicleInfo();
//        boolean delete = dbHandler.deleteVehicle("10");
//        VehicleModel[] after = dbHandler.getVehicleInfo();
        boolean insert = dbHandler.insertVehicle("123456", "aaa", "model", "1234", "reed",
                "123", "available", "Economy", "UBC", "Vancouver");
        VehicleModel[] all2 = dbHandler.getVehicleInfo();
        boolean update = dbHandler.updateVehicle("11", "123455", "make2", "fjfjf", "3344", "black",
                "44444", "rented", "", "", "");
//        boolean update2 = dbHandler.updateVehicle("11", "123455", "make2", "model2", "3344", "black",
//                "44444", "rented", null, "", "");

    }

    public static void testCustomerManipulation() {
        CustomerModel[] all = dbHandler.getCustomerInfo();
        boolean insert = dbHandler.insertCustomer("1221221222", "nana nana", "akdkkd, kkdk, kd", "123123123");
        CustomerModel[] all2 = dbHandler.getCustomerInfo();

    }




    public static VehicleModel[] generateReportDailyRentalsAllVehicleInfo(String date){
        return dbHandler.generateReportDailyRentalsAllVehicleInfo(date);
    }
    public static VehicleModel[] generateReportDailyReturnsAllVehicleInfo(String date){
        return dbHandler.generateReportDailyReturnsAllVehicleInfo(date);
    }
    public static VehicleModel[] generateReportDailyReturnsAllVehicleInfoOnBranch(String date, String location, String city){
        return dbHandler.generateReportDailyReturnsAllVehicleInfoOnBranch(date, location, city);
    }
    public static VehicleModel[] generateReportDailyRentalsAllVehicleInfoOnBranch(String date, String location, String city){
        return dbHandler.generateReportDailyRentalsAllVehicleInfoOnBranch(date, location, city);
    }


    public static void reservationManipulation(String mType, String confNo, String vtname, String phoneNum, String from, String to) {
        switch (mType){
            case "update":
                break;
        }
    }

    public static void rentalManipulation(String mType, String date, String vtname, String phoneNum, String from, String to,
                                          String odometer, String cardName, String city, String license, String confNo) {
    }

    public static void returnManipulation(String mType, String rid, String date, String odometer, String fulltank, String value) {
    }

    public static void vehicleManipulation(String mType, String vid, String vlicense, String model, String year, String color, String odometer,
                                           String status, String vtname, String location, String city) {
    }

    public static void vehicleTypeManipulation(String mType, String date, String features, String wrate, String drate, String hrate,
                                               String wirate, String dirate, String hirate, String krate) {
    }

    public static void customerManipulation(String mType, String phoneNum, String address, String name, String city, String license) {
    }

    public static void viewAll() {
    }

    public static void terminalTransactionsFinished() {
        dbHandler.close();
        dbHandler = null;

        System.exit(0);
    }
}



