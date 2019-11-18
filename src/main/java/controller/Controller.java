package controller;

import database.DatabaseConnectionHandler;
import model.VehicleModel;

import java.io.IOException;

//This is the main controller class that will orchestrate everything.
public class Controller  {
    private static DatabaseConnectionHandler dbHandler = null;

    //opens browser instance, navigates to web page, and logs in
    private void start() {
       /* File htmlFile = new File(url);
        try {
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }*/


        //login("ora_aktoriam@stu", "a42603381");
        boolean didConnect = dbHandler.login("ora_jamesens", "a98263510");
    }

    private void OpenHTTPConn() throws IOException {

    }

    //TODO
    public void addNewCustomer() {

    }

    //Returns array of all vehicles
    public static VehicleModel[] viewVehicles()  {
        return dbHandler.getVehicleInfo();
    }

    //Return filtered array of vehicles based on specific car type,location,and time interval.
    public static VehicleModel[] viewVehicles(String carType, String location, String startTime, String endTime){
        return dbHandler.getVehicleInfo(carType, location, startTime, endTime);
    }

    //TODO
    public void makeReservation() {

    }

    //TODO
    public void rentVehicle() {

    }

    //TODO
    public void returnVehicle() {

    }

    //TODO
    public void generateReport() {

    }


}



