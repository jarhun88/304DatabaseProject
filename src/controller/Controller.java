package controller;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//This is the main controller class that will orchestrate everything.
public class Controller {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private Connection connection = null;

    public static void main(String[] args) throws IOException {

        Controller c = new Controller();
        c.start();

    }

    //opens browser instance, navigates to web page, and logs in
    private void start() {
        File htmlFile = new File("index.html");
        try {
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }

        login("ora_jamesens", "a98263510");
    }

    // connects to Oracle database with supplied username and password

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    public void addNewCustomer() {

    }

    public void viewVehicles() {

    }

    public void makeReservation() {

    }

    public void rentVehicle() {

    }

    public void returnVehicle() {

    }

    public void generateReport() {

    }
}



