package controller;

import database.DatabaseConnectionHandler;
import model.ReservationModel;
import model.ReturnConfirmationMessageModel;
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
//        boolean didConnect = dbHandler.login("ora_jamesens", "a98263510");
        boolean didConnect = dbHandler.login("ora_aktoriam", "a42603381");

        VehicleModel[] temp = dbHandler.getVehicleInfo("Economy", "UBC", "Vancouver", "2019-01-19","2019-01-30");
        VehicleModel[] temp1 = dbHandler.getVehicleInfo("", "UBC", "Vancouver", "2019-01-19","2019-01-30");
        int numOfAvailableCar = dbHandler.getAvailableNumOfVehicle("","","","","");
//        int confNo = dbHandler.makeReservation("1234567890", "asdf", "asdf", "111111111", "10",
//                "2200-01-01:00:00", "2200-02-01:00:00");
        ReservationModel reservationModel =  dbHandler.getReservation(1);
//        int rid = dbHandler.rentVehicle("10", "1234567890", "2200-01-01", "2200-02-01", "1234",
//                "", "Visa","4444777788889999", "2200-10-10");
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
    public static int makeReservation(String phoneNumber, String name, String address, String city, String dlicense, String vid,
                                      String fromDate, String toDate) {
        return dbHandler.makeReservation(phoneNumber, name, address, dlicense, vid, fromDate, toDate);
    }

    // Rents a specific vehicle
    public static int rentVehicle(String vid, String cellphone, String fromDateTime, String toDateTime, String odometer, String address,
                                  String dLicense, String confNo, String cardName, String cardNo, String expDate) {

        return dbHandler.rentVehicle(vid, cellphone, fromDateTime, toDateTime, odometer, confNo, cardName, cardNo, expDate);
    }

    // Returns a vehicle
    public static ReturnConfirmationMessageModel returnVehicle(String rid, String returnDateTime, String odometer, String fulltank) {
        return dbHandler.returnVehicle(rid, returnDateTime,odometer, fulltank);
        // String rid, String returnDateTime, String odometer, String fulltank, String confNo
    }



}



