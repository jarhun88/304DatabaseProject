package database;

import model.VehicleModel;
import oracle.jdbc.proxy.annotation.Pre;

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

    // EFFECTS: returns the array of result sets for the query of getting available vehicles based on the params
    // note: you can use the size of the array to show the number of vehicles first!!
    public VehicleModel[] getVehicleInfo(String carType, String location, String city, String startTime, String endTime) {
        ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();

        // Build up filter list based on input
        String cTypeFilter = "";
        String locFilter = "";
        String cityFilter = "";
        String timeIntFilter = "";
        if (carType != null && carType.length() > 0) {
            cTypeFilter = "AND v.vtname = '" + carType + "'";
        }
        if (location != null && location.length() > 0) {
            locFilter = "AND v.location = '" + location + "'";
        }
        if (city != null && city.length() > 0) {
            cityFilter = "AND v.city = '" + city + "'";
        }
        // Checks to see if a reservation has already been made from that interval
        if (startTime != null && endTime != null && startTime.length() > 0 && endTime.length() > 0) {
            //  not exists( Select * from reservation where timeInterval between (FROMDATETIME, TODATETIME)
            timeIntFilter = "and v.vid not in (select r.vid " +
                    "from reservation r, vehicle v1 " +
                    "where v1.vid = r.vid and r.fromDateTime <= to_timestamp('" + startTime + "','YYYY-MM-DD:HH24:MI')" +
                    "and r.toDateTime >= to_timestamp('" + endTime + "','YYYY-MM-DD:HH24:MI'))";
        }

        /*
        select distinct * from vehicle v where v.vtname = 'Compact' and v.location = 'UBC' and v.city = 'Vancouver'
and v.vid not in (select r.vid from reservation r, vehicle v1 where v1.vid = r.vid and r.fromDateTime <= to_timestamp('2019-01-02:00:00','YYYY-MM-DD:HH24:MI')
and r.toDateTime >= to_timestamp('2019-01-03','YYYY-MM-DD'))
         */

        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT distinct * FROM Vehicle v where status <> 'maintenance'"; // rented is okay because the current state does not matter
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

    // EFFECTS: returns the number of available vehicles based on params
    // this is redundant, but leaves here anyway...
    public int getAvailableNumOfVehicle(String carType, String location, String city, String startTime, String endTime) {
        ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();

        // Build up filter list based on input
        String cTypeFilter = "";
        String locFilter = "";
        String cityFilter = "";
        String timeIntFilter = "";
        if (carType != null && carType.length() > 0) {
            cTypeFilter = "AND v.vtname = '" + carType + "'";
        }
        if (location != null && location.length() > 0) {
            locFilter = "AND v.location = '" + location + "'";
        }
        if (city != null && city.length() > 0) {
            cityFilter = "AND v.city = '" + city + "'";
        }
        // Checks to see if a reservation has already been made from that interval
        if (startTime != null && endTime != null && startTime.length() > 0 && endTime.length() > 0) {
            //  not exists( Select * from reservation where timeInterval between (FROMDATETIME, TODATETIME)
            timeIntFilter = "and v.vid not in (select r.vid " +
                    "from reservation r, vehicle v1 " +
                    "where v1.vid = r.vid and r.fromDateTime <= to_timestamp('" + startTime + "','YYYY-MM-DD:HH24:MI')" +
                    "and r.toDateTime >= to_timestamp('" + endTime + "','YYYY-MM-DD:HH24:MI'))";
        }

        /*
        select distinct * from vehicle v where v.vtname = 'Compact' and v.location = 'UBC' and v.city = 'Vancouver'
and v.vid not in (select r.vid from reservation r, vehicle v1 where v1.vid = r.vid and r.fromDateTime <= to_timestamp('2019-01-02:00:00','YYYY-MM-DD:HH24:MI')
and r.toDateTime >= to_timestamp('2019-01-03','YYYY-MM-DD'))
         */
        int resultNum = 0;

        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT distinct * FROM Vehicle v where status <> 'maintenance'"; // rented is okay because the current state does not matter
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
                resultNum++;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return resultNum;
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


    //EFFECTS: returns true if a customer is successfully added
    //          returns false other wise
    public boolean addNewCustomer(String cellphone, String name, String address, String dlicense) {
        boolean isSuccessful = false;
        try {
            PreparedStatement ps = connection.prepareStatement("insert into customer values (?,?,?,?)");
            ps.setString(1, cellphone);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, dlicense);

            ps.executeUpdate();
            connection.commit();

            ps.close();
            isSuccessful = true;
        } catch (SQLException e) {
            e.printStackTrace();
            rollbackConnection();
        }
        return isSuccessful;


    }

    // REQUIRES: cellphone has to be in a valid format, and cannot be empty
    // EFFECTS: returns true if the customer is already a member
    //          returns false otherwise
    public boolean isCustomerMember(String cellphone) {
        int resultCount = 0;
        try {
            Statement stmt = connection.createStatement();
            String query = "select count(*) as total from customer where cellphone = '" + cellphone + "'";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                resultCount = rs.getInt("total");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultCount > 0;

    }

    // REQUIRES: all inputs cannot be null and all inputs are in valid format
    // EFFECTS: returns true if there is overbooking based on the params
    //          returns false otherwise
    public boolean isOverBooked(String vid, String fromDateTime, String toDateTime) {
        int resultCount = 0;
        try {
            Statement stmt = connection.createStatement();
            String query = "select count(*) as total " +
                    "from reservation " +
                    "where vid = " + Integer.parseInt(vid) + " and fromDateTime <= to_timestamp('" + fromDateTime + "', 'YYYY-MM-DD:HH24:MI') " +
                    "and toDateTime >= to_timestamp('" + toDateTime + "', 'YYYY-MM-DD:HH24:MI')";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                resultCount = rs.getInt("total");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultCount > 0;

    }


}
