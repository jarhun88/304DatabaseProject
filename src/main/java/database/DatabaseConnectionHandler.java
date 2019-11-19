package database;

import model.VehicleModel;

import java.sql.*;
import java.util.ArrayList;


/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }


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

    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    // Gets all vehicles that are available
    public VehicleModel[] getVehicleInfo() {
        ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Vehicle where STATUS = 'available'");

//    		// get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}

            while (rs.next()) {
                VehicleModel model = new VehicleModel(rs.getInt("vid"),
                        rs.getInt("vlicense"),
                        rs.getString("make"), rs.getString("model"), rs.getString("year"),
                        rs.getString("color"), rs.getInt("odometer"), rs.getString("status"),
                        rs.getString("vtname"), rs.getString("location"), rs.getString("city"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new VehicleModel[result.size()]);
    }

    // Gets vehicles that are available based on params
    public VehicleModel[] getVehicleInfo(String carType, String location, String city, String startTime, String endTime) {
        ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();

        // Build up filter list based on input
        String cTypeFilter = "";
        String locFilter = "";
        String cityFilter = "";
        String timeIntFilter = "";
        if (carType != null && carType.length() > 0) {
            cTypeFilter = "AND VTNAME = '" + carType+ "'";
        }
        if (location != null && location.length() > 0) {
            locFilter = "AND LOCATION = '" + location+ "'";
        }
        if (city != null && city.length() > 0) {
            cityFilter = "AND CITY = '" + city + "'";
        }
        // Checks to see if a reservation has already been made from that interval
        if (startTime != null && endTime != null && startTime.length() > 0 && endTime.length() > 0) {
            //  not exists( Select * from reservation where timeInterval between (FROMDATETIME, TODATETIME)
            timeIntFilter = "AND NOT EXISTS (Select * from Reservation r WHERE v.vid=r.vid AND to_timestamp('" + startTime +
                    "', ‘YYYY-MM-DD’) >= TODATETIME AND to_timestamp('" + endTime + "', ‘YYYY-MM-DD’) <= FROMDATETIME)";
        }

        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT * FROM Vehicle v where STATUS = 'available'";
            // Add filter list to query
            query = query + cTypeFilter + locFilter + cityFilter + timeIntFilter;
            ResultSet rs = stmt.executeQuery(query);

//    		// get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}

			// Reads sql result into vehicle array
            while (rs.next()) {
                VehicleModel model = new VehicleModel(rs.getInt("vid"),
                        rs.getInt("vlicense"),
                        rs.getString("make"), rs.getString("model"), rs.getString("year"),
                        rs.getString("color"), rs.getInt("odometer"), rs.getString("status"),
                        rs.getString("vtname"), rs.getString("location"), rs.getString("city"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new VehicleModel[result.size()]);
    }

    // Makes a reservation and returns confirmation number
    public int makeReservation(String phoneNumber, String name, String address, String city, String dlicense, String vtname,
                               String fromDate, String toDate) {
        return 0;
    }

    // Rents a vehicle and returns confirmation number
    public int rentVehicle(String vid, String cellphone, String fromDateTime, String toDateTime, String name, String address,
                           String dLicense, String confNo, String cardName, String cardNo, String expDate) {
        return 0;
    }

    // Returns a vehicle and returns a confirmation number
    public int returnVehicle(String rid, String returnDateTime, String odometer, Boolean fulltank) {

        return 0;
    }

    // Generate report for all returns
    public VehicleModel[] generateReportDailyReturns(String date, String city, String location) {
        return null;
    }

    // Generate report for all rentals
    public VehicleModel[] generateReportDailyRentals(String date) {
        return null;
    }

    // Generate report for returns based on branch
    public VehicleModel[] generateReportDailyReturns(String date) {
        return null;
    }

    // Generate report for rentals based on rentals
    public VehicleModel[] generateReportDailyRentals(String date, String city, String location) {
        return null;
    }

	public boolean addNewCustomer(String cellphone, String name, String address, String dlicense) {
    	return false;
	}
}
