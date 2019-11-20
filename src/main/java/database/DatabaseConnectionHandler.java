package database;

import model.RentConfirmationMessageModel;
import model.ReservationModel;
import model.VehicleModel;
import oracle.jdbc.proxy.annotation.Pre;

import javax.swing.plaf.nimbus.State;
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
                        rs.getString("vlicense"),
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
                        rs.getString("vlicense"),
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


    // REQUIRES: all the inputs to be non-empty
    // EFFECTS: Makes a reservation and returns confirmation number
    //          if a reservation cannot be made for some reason, it returns -1
    public int makeReservation(String phoneNumber, String name, String address, String city, String dlicense, String vid,
                               String fromDateTime, String toDateTime) {
        int confNo = -1;
        if (isCustomerMember(phoneNumber)) {
            boolean status = addNewCustomer(phoneNumber, name, address,dlicense);
        }

        if (isOverBooked(vid,fromDateTime,toDateTime)) {
            return confNo;
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("insert into reservation (vid, cellphone, fromDateTime, toDateTime) values ( " +
                    "?, ?, to_timestamp(?, 'YYYY-MM-DD:HH24:MI'), " +
                    "to_timestamp(?, 'YYYY-MM-DD:HH24:MI'))");
            ps.setInt(1, Integer.parseInt(vid));
            ps.setString(2, phoneNumber);
            ps.setString(3, fromDateTime);
            ps.setString(4, toDateTime);

            ps.executeUpdate();
            connection.commit();

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
            rollbackConnection();
        }



        // get confirmation number
        try {
            Statement stmt = connection.createStatement();
            String query = "select confNo from reservation where confNo = " +
                    "(select max(confNo) from reservation)";
            ResultSet rs = stmt.executeQuery(query);

            rs.first();
            confNo = rs.getInt("confNo");

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return confNo;
    }

    // EFFECTS: returns the Reservation detail based on the confirmation number
    public ReservationModel getReservation(int confNo) {
        ReservationModel model = null;
        try {
            Statement stmt = connection.createStatement();
            String query = "select * from reservation where confNo = " + confNo;
            ResultSet rs = stmt.executeQuery(query);

            rs.first();
            model = new ReservationModel(rs.getInt("confNo"), rs.getInt("vid"),
                    rs.getString("cellphone"), rs.getTimestamp("fromDateTime"), rs.getTimestamp("toDateTime"));

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }



    // REQUIRES: all the inputs are in the valid format
    // EFFECTS: Rents a vehicle and returns confirmation number (rid)
    public int rentVehicle(String vid, String cellphone, String fromDateTime, String toDateTime, String odometer,
                           String confNo, String cardName, String cardNo, String expDate) {

        boolean isSuccessful = false;
        int rid = -1;
        try {
            PreparedStatement ps = connection.prepareStatement("insert into rent (vid, cellphone, fromDateTime, " +
                    "toDateTime, odometer, cardName, cardNo, expDate, confNo) values (" +
                    "?, ?, to_timestamp(?,'YYYY-MM-DD:HH24:MI'), to_timestamp(?,'YYYY-MM-DD:HH24:MI'), " +
                    "?, ?, ?, to_date(?, 'YYYY-MM-DD'), ?)");
            ps.setInt(1,Integer.parseInt(vid));
            ps.setString(2,cellphone);
            ps.setString(3, fromDateTime);
            ps.setString(4, toDateTime);
            ps.setInt(5, Integer.parseInt(odometer));
            ps.setString(6, cardName);
            ps.setString(7, cardNo);
            ps.setString(8, expDate);
            ps.setInt(9, Integer.parseInt(confNo));

            ps.executeUpdate();
            connection.commit();
            isSuccessful = true;

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
            rollbackConnection();
        }

        if (isSuccessful) {
            try {
                PreparedStatement ps = connection.prepareStatement("update vehicle set status = 'rented' where vid = ?");
                ps.setInt(1,Integer.parseInt(vid));
                ps.executeUpdate();
                connection.commit();

                ps.close();

            } catch (SQLException e) {
                e.printStackTrace();
                rollbackConnection();
            }

            rid = getRidForRent(Integer.parseInt(confNo), Integer.parseInt(vid));

        }
        return rid;

    }

    // EFFECTS: returns the rid (confirmation of the rent) based on the confNo of reservation and vid
    public int getRidForRent(int confNo, int vid) {
        int rid = -1;
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT rid from Rent where confNo = "+ confNo+" AND vid = " + vid;
            ResultSet rs = stmt.executeQuery(query);

            rs.first();
            rid = rs.getInt("rid");

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rid;

    }



    // EFFECTS: returns the confirmation message upon the successful return of vehicle
    public int returnVehicle(String rid, String returnDateTime, String odometer, String fulltank, String confNo) {

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

    // EFFECTS: returns the confirmation message model for renting based on the rid
    public RentConfirmationMessageModel getRentConfMessage(int rid) {
        RentConfirmationMessageModel result = null;
        try {
            Statement stmt = connection.createStatement();
            String query = "select r.rid, r.cellphone, r.fromDateTime, r.toDateTime, r.cardName, r.cardNo, r.expDate, " +
                    "r.odometer, r.confNo, v.vid, v.vlicense, v.vtname, v.location, v.city " +
                    "from Rent r, Vehicle v where r.vid = v.vid and r.rid =" + rid;

            ResultSet rs = stmt.executeQuery(query);

            rs.first();
            result = new RentConfirmationMessageModel(rs.getInt("r.rid"), rs.getString("r.cellphone"),
                    rs.getTimestamp("r.fromDateTime"), rs.getTimestamp("r.toDateTime"),
                    rs.getString("r.cardName"), rs.getString("r.cardNo"), rs.getDate("r.expDate"),
                    rs.getInt("r.odometer"), rs.getInt("r.confNo"), rs.getInt("v.vid"),
                    rs.getString("v.vlicense"), rs.getString("v.vtname"), rs.getString("v.location"),
                    rs.getString("v.city"));

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;


    }


}
