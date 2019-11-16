package controller;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//This is the main controller class that will orchestrate everything.
public class Controller {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private Connection connection = null;

    private HttpURLConnection http;
    private String url = "index.html";

    public static void main(String[] args) throws IOException {

        Controller c = new Controller();
        c.start();

    }

    //opens browser instance, navigates to web page, and logs in
    private void start() {
        File htmlFile = new File(url);
        try {
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }

        OpenHTTPConn();

        login("ora_jamesens", "a98263510");
       try {
           viewVehicles();
       }
       catch (Exception e){

       }
    }

    private void OpenHTTPConn() {
        try {
            URL url = new URL("http://127.0.0.1:8080/index.html");
            URLConnection con = url.openConnection();
            http = (HttpURLConnection) con;
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }

        try {
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
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

    //TODO
    public void addNewCustomer() {

    }

    //TODO
    public void viewVehicles() throws IOException {


        byte[] out = "{\"username\":\"root\",\"password\":\"password\"}".getBytes(StandardCharsets.UTF_8);
        int length = out.length;
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();

        try (OutputStream os = http.getOutputStream()) {
            os.write(out);
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
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



