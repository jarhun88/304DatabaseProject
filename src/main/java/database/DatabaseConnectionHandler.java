package database;

//import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;

import model.*;
import oracle.jdbc.proxy.annotation.Pre;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;


/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    public static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    public static final String EXCEPTION_TAG = "[EXCEPTION]";
    public static final String WARNING_TAG = "[WARNING]";

    public static final String TYPE_STRING = "String";
    public static final String TYPE_DOUBLE = "double";
    public static final String TYPE_INT = "int";
    public static final String TYPE_TIMESTAMP = "timestamp";
    public static final String TYPE_DATE = "date";
    public static final String TYPE_FLOAT = "float";

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

    public Connection getConnection() {
        return connection;
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

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT distinct * FROM Vehicle v where status = 'available'";
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
                        rs.getString("color"), rs.getDouble("odometer"), rs.getString("status"),
                        rs.getString("vtname"), rs.getString("location"), rs.getString("city"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return result.toArray(new VehicleModel[result.size()]);
        }
    }

    // EFFECTS: returns the number of available vehicles based on params
    // this is redundant, but leaves here anyway...
    public int getAvailableNumOfVehicle(String carType, String location, String city, String startTime, String endTime) {
        VehicleModel[] models = getVehicleInfo(carType, location, city, startTime, endTime);
        if (models == null) {
            return 0;
        }
        return models.length;

    }


    //tested
    // REQUIRES: all the inputs to be non-empty
    // EFFECTS: Makes a reservation and returns reservation details
    //          if a reservation cannot be made for some reason, it returns null;
    public ReservationModel makeReservation(String phoneNumber, String name, String address, String dlicense, String vid,
                               String fromDateTime, String toDateTime) {
        ReservationModel result = null;
        int confNo = -1;
        if (!isCustomerMember(phoneNumber)) {
            boolean status = insertCustomer(phoneNumber, name, address, dlicense);
        }

        if (isOverBooked(vid, fromDateTime, toDateTime)) {
            return null;
        }

        boolean insertReservation = insertReservation(vid, phoneNumber, fromDateTime, toDateTime);
        if (insertReservation == false) {
            return null;
        }

        // get confirmation number
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "select confNo from reservation where confNo = " +
                    "(select max(confNo) from reservation)";
            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            confNo = rs.getInt("confNo");

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (confNo != -1) {
            return getReservationDetail(confNo);
        } else {
            return null;
        }

    }

    // tested
    // EFFECTS: returns the Reservation detail based on the confirmation number
    public ReservationModel getReservationDetail(int confNo) {
        ReservationModel model = null;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "select * from reservation where confNo = " + confNo;
            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            model = new ReservationModel(rs.getInt("confNo"), rs.getInt("vid"),
                    rs.getString("cellphone"), rs.getTimestamp("fromDateTime"), rs.getTimestamp("toDateTime"));

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return model;
        }


    }

    //tested
    // REQUIRES: all the inputs are in the valid format
    // EFFECTS: Rents a vehicle and returns confirmation number (rid)
    public int rentVehicle(String vid, String cellphone, String fromDateTime, String toDateTime, String odometer,
                           String confNo, String cardName, String cardNo, String expDate) {

        int rid = -1;
        boolean isSuccessful = insertRent(vid, cellphone, fromDateTime, toDateTime, odometer, cardName, cardNo, expDate, confNo);

        if (isSuccessful) {
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement("update vehicle set status = 'rented' where vid = ?");
                ps.setInt(1, Integer.parseInt(vid));
                ps.executeUpdate();
                connection.commit();

            } catch (SQLException e) {
                e.printStackTrace();
                rollbackConnection();
            } finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                rid = getRidForRent(Integer.parseInt(confNo), Integer.parseInt(vid));
            }


        }
        return rid;

    }

    //tested
    // EFFECTS: returns the rid (confirmation of the rent) based on the confNo of reservation and vid
    public int getRidForRent(int confNo, int vid) {
        int rid = -1;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT rid from Rent where confNo = " + confNo + " AND vid = " + vid;
            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            rid = rs.getInt("rid");

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return rid;
        }


    }


    //tested
    // REQUIRES: all inputs must be in the valid format
    // EFFECTS: returns the confirmation message upon the successful return of vehicle
    public ReturnConfirmationMessageModel returnVehicle(String rid, String returnDateTime, String odometer, String fulltank) {
        ReturnConfirmationMessageModel confMessage = null;

        // calculate the amount
        VehicleTypeModel rateInfo = getRateInfo(Integer.parseInt(rid));
        TimeIntervalOdometerModel timeIntOdometer = getTimeIntervalAndOdometer(Integer.parseInt(rid));

        Timestamp fromDateTime = timeIntOdometer.getFromDateTime();
        Timestamp toDateTime = timeIntOdometer.getToDateTime();
        // double originalOdometer = timeIntOdometer.getOdometer();
        long startTime = fromDateTime.getTime();
        long endTime = toDateTime.getTime();
        long timeIntervalInHour = (endTime - startTime) / 3600000;

        long weeks = timeIntervalInHour / 168;
        timeIntervalInHour = timeIntervalInHour % 168;
        long days = timeIntervalInHour / 24;
        long hours = timeIntervalInHour % 24;

        double regularCost = weeks * rateInfo.getWrate() + days * rateInfo.getDrate() + hours * rateInfo.getHrate();
        double insuranceCost = weeks * rateInfo.getWirate() + days * rateInfo.getDirate() + hours * rateInfo.getHirate();
        double value = regularCost + insuranceCost;

        String regularCalDetail = "Payment Rate for this Vehicle:" + rateInfo.toString() + "￿￿\n"
                + "Regular Cost -> " + weeks + " week(s) * " + rateInfo.getWrate() + " + "
                + days + " day(s) * " + rateInfo.getDrate() + " + "
                + hours + " hour(s) * " + rateInfo.getHrate() + " = " + regularCost + "￿￿\n";
        String insCalDetail = "Insurance Cost -> " + weeks + " week(s) * " + rateInfo.getWirate() + " + "
                + days + " day(s) * " + rateInfo.getDirate() + " + "
                + hours + " hour(s) * " + rateInfo.getHirate() + " = " + insuranceCost + "￿￿\n"
                + "Total: " + value;

        String calculationDetail = regularCalDetail + insCalDetail;

        boolean isSuccessful = insertReturn(rid, returnDateTime, odometer, fulltank, "" + value);

        boolean isSuccessful2 = false;

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("update vehicle set status = 'available'" +
                    " where vid = ANY (select v.vid from vehicle v, rent r where v.vid = r.vid and r.rid = ?)");
            ps.setInt(1, Integer.parseInt(rid));

            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                isSuccessful2 = true;
            }
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (isSuccessful && isSuccessful2) {
            confMessage = new ReturnConfirmationMessageModel(Integer.parseInt(rid), returnDateTime, value, calculationDetail);
            return confMessage;

        } else {
            return null;
        }
    }

    //tested
    // EFFECTS: returns the vehicles rented our on that day
    public VehicleModel[] generateReportDailyRentalsAllVehicleInfo(String date) {
        ArrayList<VehicleModel> result = new ArrayList<>();

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT v.vid, v.vlicense, v.make, v.model, v.year, " +
                    "v.color, v.odometer, v.status, v.vtname, v.location, v.city " +
                    "FROM Rent r, Vehicle v " +
                    "WHERE fromDateTime <= to_timestamp('" + date + ":00:00', 'YYYY-MM-DD:HH24:MI') " +
                    "AND toDateTime >=  to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "AND r.vid = v.vid " +
                    "ORDER BY v.city, v.location, v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                VehicleModel model = new VehicleModel(rs.getInt("vid"),
                        rs.getString("vlicense"),
                        rs.getString("make"), rs.getString("model"), rs.getString("year"),
                        rs.getString("color"), rs.getDouble("odometer"), rs.getString("status"),
                        rs.getString("vtname"), rs.getString("location"), rs.getString("city"));
                result.add(model);

            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return result.toArray(new VehicleModel[result.size()]);

        }



        /*
         VID					   NOT NULL NUMBER(38)
 VLICENSE					    CHAR(6)
 MAKE						    VARCHAR2(30)
 MODEL						    VARCHAR2(30)
 YEAR						    VARCHAR2(4)
 COLOR						    VARCHAR2(20)
 ODOMETER					    NUMBER
 STATUS 					    VARCHAR2(20)
 VTNAME 					    VARCHAR2(9)
 LOCATION					    VARCHAR2(20)
 CITY						    VARCHAR2(20)
         */
    }


    //tested
    // EFFECTS: returns the number of vehicles rented out on that day grouped by vehicle
    public ReportGroupedByVehilceModel[] getNumOfVehicleDailyRentalGBVehicle(String date) {
        ArrayList<ReportGroupedByVehilceModel> result = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT COUNT(*) as total, v.vtname " +
                    "FROM Vehicle v, Rent r " +
                    "WHERE fromDateTime <= to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND toDateTime >= to_timestamp('" + date + ":23:59','YYYY-MM-DD:HH24:MI') AND r.vid = " +
                    "v.vid " +
                    "GROUP BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ReportGroupedByVehilceModel model = new ReportGroupedByVehilceModel(rs.getInt("total"),
                        rs.getString("vtname"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new ReportGroupedByVehilceModel[result.size()]);
        }


    }


    //tested
    // EFFECTS: returns the number of vehicles rented out on that day grouped by branch
    public ReportGroupByBranchModel[] getNumOfVehicleDailyRentalGBBranch(String date) {
        ArrayList<ReportGroupByBranchModel> result = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT COUNT(*) as total, v.location, v.city " +
                    "FROM Vehicle v, Rent r " +
                    "WHERE fromDateTime <=  to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND toDateTime >=  to_timestamp('" + date + ":23:59','YYYY-MM-DD:HH24:MI') AND r.vid = v.vid " +
                    "GROUP BY v.location, v.city";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ReportGroupByBranchModel model = new ReportGroupByBranchModel(rs.getInt("total"),
                        rs.getString("location"), rs.getString("city"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return result.toArray(new ReportGroupByBranchModel[result.size()]);
        }


    }


    //tested
    // EFFECTS: returns the number of new rental on that day in the entire company
    public int getNumOfVehicleNewlyDailyRental(String date) {
        int total = -1;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT COUNT(*) as total " +
                    "FROM Rent " +
                    "WHERE fromDateTime >=  to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND fromDateTime <=  to_timestamp('" + date + ":23:59','YYYY-MM-DD:HH24:MI')";

            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            total = rs.getInt("total");

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return total;
        }


    }


    //tested
    // EFFECTS: returns the vehicles rented our on that day on the specified branch
    public VehicleModel[] generateReportDailyRentalsAllVehicleInfoOnBranch(String date, String location, String city) {
        ArrayList<VehicleModel> result = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT v.vid, v.vlicense, v.make, v.model, v.year," +
                    "v.color, v.odometer, v.status, v.vtname, v.location, v.city " +
                    "FROM Rent r, Vehicle v " +
                    "WHERE fromDateTime <=  to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND toDateTime >=  to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI')  " +
                    "AND r.vid = v.vid AND v.location = '" + location + "' AND v.city = '" + city + "' " +
                    "ORDER BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                VehicleModel model = new VehicleModel(rs.getInt("vid"),
                        rs.getString("vlicense"),
                        rs.getString("make"), rs.getString("model"), rs.getString("year"),
                        rs.getString("color"), rs.getDouble("odometer"), rs.getString("status"),
                        rs.getString("vtname"), rs.getString("location"), rs.getString("city"));
                result.add(model);

            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new VehicleModel[result.size()]);
        }


    }

    // tested
    // EFFECTS: returns the number of vehicles rented out on that day grouped by vehicle on the branch
    public ReportGroupedByVehilceModel[] getNumOfVehicleDailyRentalGBVehicleOnBranch(String date, String location, String city) {
        ArrayList<ReportGroupedByVehilceModel> result = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT COUNT(*) as total, v.vtname " +
                    "FROM Vehicle v, Rent r " +
                    "WHERE fromDateTime <= to_timestamp('" + date + ":00:00', 'YYYY-MM-DD:HH24:MI')  " +
                    "AND  toDateTime >= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') AND r.vid = " +
                    "v.vid " +
                    "AND v.location = '" + location + "' AND v.city = '" + city + "' " +
                    "GROUP BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ReportGroupedByVehilceModel model = new ReportGroupedByVehilceModel(rs.getInt(1),
                        rs.getString("vtname"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new ReportGroupedByVehilceModel[result.size()]);

        }


    }

    // tested
    // EFFECTS: returns the number of vehicle rented out on that day in the branch
    public int getNumOfVehicleDailyRentalOnBranch(String date, String location, String city) {
        int total = -1;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT COUNT(*) as total " +
                    "FROM Vehicle v, Rent r " +
                    "WHERE fromDateTime <=  to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND toDateTime >= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') AND r.vid = " +
                    "v.vid AND v.location = '" + location + "' AND v.city = '" + city + "'";

            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            total = rs.getInt(1);

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return total;

        }

    }


    //tested

    // EFFECTS: returns the number of new rental on that day in the branch
    public int getNumOfVehicleNewlyDailyRentalOnBranch(String date, String location, String city) {
        int total = -1;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT COUNT(*) as total " +
                    "FROM Rent r, Vehicle v " +
                    "WHERE fromDateTime >= to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND fromDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "AND r.vid = v.vid AND v.location = '" + location + "' AND v.city = '" + city + "'";

            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            total = rs.getInt(1);

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return total;
        }


    }


    //tested
    // EFFECTS: returns all the vehicle returned on that day in the entire ccompany
    public VehicleModel[] generateReportDailyReturnsAllVehicleInfo(String date) {
        ArrayList<VehicleModel> result = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT v.vid, v.vlicense, v.make, v.model, v.year, " +
                    "v.color, v.odometer, v.status, v.vtname, v.location, v.city " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00', 'YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "ORDER BY v.city, v.location, v.vtname";

            //v.vid, v.vlicense, v.make, v.model, v.year, v.color, v.odometer, v.status, v.vtname, v.location, v.city

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                VehicleModel model = new VehicleModel(rs.getInt("vid"),
                        rs.getString("vlicense"),
                        rs.getString("make"), rs.getString("model"), rs.getString("year"),
                        rs.getString("color"), rs.getDouble("odometer"), rs.getString("status"),
                        rs.getString("vtname"), rs.getString("location"), rs.getString("city"));
                result.add(model);

            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new VehicleModel[result.size()]);

        }


    }

    //tested
    // EFFECTS: returns the number of vehicles returned on the day grouped by vtname in the entire company
    public ReportGroupedByVehilceModel[] getNumOdVehicleDailyReturnGBVehicle(String date) {
        ArrayList<ReportGroupedByVehilceModel> result = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT count(*) as total, v.vtname " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00', 'YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "GROUP BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ReportGroupedByVehilceModel model = new ReportGroupedByVehilceModel(rs.getInt(1),
                        rs.getString("vtname"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new ReportGroupedByVehilceModel[result.size()]);

        }


    }

    //tested
    // EFFECTS: returns the total revenue per vtname in the entire company
    public RevenueReportGroupedByVehilceModel[] getRevenueDailyReturnGBVehicle(String date) {
        ArrayList<RevenueReportGroupedByVehilceModel> result = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT sum(r.value) as total, v.vtname " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00', 'YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "GROUP BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                RevenueReportGroupedByVehilceModel model = new RevenueReportGroupedByVehilceModel(rs.getDouble(1),
                        rs.getString("vtname"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new RevenueReportGroupedByVehilceModel[result.size()]);

        }


    }

    //tested

    //returns the total revenue and the number of cars returned grouped by branch
    public ReportTotalNumAndRevenueGBBranchModel[] getTotalNumAndRevenueGBBranch(String date) {
        ArrayList<ReportTotalNumAndRevenueGBBranchModel> result = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT sum(r.value) as totalRev, count(*) as totalNum, v.location, v.city " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00', 'YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "GROUP BY v.city, v.location";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ReportTotalNumAndRevenueGBBranchModel model = new ReportTotalNumAndRevenueGBBranchModel(rs.getInt(1),
                        rs.getInt(2), rs.getString("location"), rs.getString("city"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new ReportTotalNumAndRevenueGBBranchModel[result.size()]);

        }


    }


    //tested

    // EFFECTS: returns the information of vehicle returned on that day on the specified branch
    public VehicleModel[] generateReportDailyReturnsAllVehicleInfoOnBranch(String date, String location, String city) {
        ArrayList<VehicleModel> result = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT v.vid, v.vlicense, v.make, v.model, v.year, v.color, v.odometer, v.status, v.vtname, v.location, v.city " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "AND v.location = '" + location + "' AND v.city = '" + city + "' " +
                    "ORDER BY v.vtname";

            //v.vid, v.vlicense, v.make, v.model, v.year, v.color, v.odometer, v.status, v.vtname, v.location, v.city

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                VehicleModel model = new VehicleModel(rs.getInt("vid"),
                        rs.getString("vlicense"),
                        rs.getString("make"), rs.getString("model"), rs.getString("year"),
                        rs.getString("color"), rs.getDouble("odometer"), rs.getString("status"),
                        rs.getString("vtname"), rs.getString("location"), rs.getString("city"));
                result.add(model);

            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new VehicleModel[result.size()]);

        }

    }


    //tested

    // EFFECTS: returns the number of vehicles returned on the day grouped by vtname on the branch
    public ReportGroupedByVehilceModel[] getNumOdVehicleDailyReturnGBVehicleOnBranch(String date, String location, String city) {
        ArrayList<ReportGroupedByVehilceModel> result = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT count(*) as total, v.vtname " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI')  " +
                    "AND v.location = '" + location + "' AND v.city = '" + city + "' " +
                    "GROUP BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ReportGroupedByVehilceModel model = new ReportGroupedByVehilceModel(rs.getInt(1),
                        rs.getString("vtname"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new ReportGroupedByVehilceModel[result.size()]);

        }


    }

    //tested
    // EFFECTS: returns the total revenue per vtname on the branch
    public RevenueReportGroupedByVehilceModel[] getRevenueDailyReturnGBVehicleOnBranch(String date, String location, String city) {
        ArrayList<RevenueReportGroupedByVehilceModel> result = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT sum(r.value) as total, v.vtname " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "AND v.location = '" + location + "' AND v.city = '" + city + "' " +
                    "GROUP BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                RevenueReportGroupedByVehilceModel model = new RevenueReportGroupedByVehilceModel(rs.getDouble(1),
                        rs.getString("vtname"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new RevenueReportGroupedByVehilceModel[result.size()]);

        }


    }

    //tested

    // EFFECTS: returns the total revenue and total num of cars returned on that day on the branch
    public ReportTotalNumAndRevenueOnBranchModel getTotalRevAndNumRentalsOnBranch(String date, String location, String city) {
        ReportTotalNumAndRevenueOnBranchModel result = null;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "SELECT sum(r.value) as totalRev, count(*) as totalNum " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "AND v.location = '" + location + "' AND v.city = '" + city + "'";

            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            result = new ReportTotalNumAndRevenueOnBranchModel(rs.getDouble(1), rs.getInt(2));

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;

        }


    }


    //tested
    // REQUIRES: cellphone has to be in a valid format, and cannot be empty
    // EFFECTS: returns true if the customer is already a member
    //          returns false otherwise
    public boolean isCustomerMember(String cellphone) {
        int resultCount = 0;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "select count(*) as total from customer where cellphone = '" + cellphone + "'";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                resultCount = rs.getInt("total");
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return resultCount > 0;

        }


    }

    //tested
    // REQUIRES: all inputs cannot be null and all inputs are in valid format
    // EFFECTS: returns true if there is overbooking based on the params
    //          returns false otherwise
    public boolean isOverBooked(String vid, String fromDateTime, String toDateTime) {
        int resultCount = 0;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "select count(*) as total " +
                    "from reservation " +
                    "where vid = " + Integer.parseInt(vid) + " and fromDateTime <= to_timestamp('" + fromDateTime + "', 'YYYY-MM-DD:HH24:MI') " +
                    "and toDateTime >= to_timestamp('" + toDateTime + "', 'YYYY-MM-DD:HH24:MI')";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                resultCount = rs.getInt("total");
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return resultCount > 0;

        }


    }

    //tested
    // EFFECTS: returns the confirmation message model for renting based on the rid
    public RentConfirmationMessageModel getRentConfMessage(int rid) {
        RentConfirmationMessageModel result = null;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "select r.rid, r.cellphone, r.fromDateTime, r.toDateTime, r.cardName, r.cardNo, r.expDate, " +
                    "r.odometer, r.confNo, v.vid, v.vlicense, v.vtname, v.location, v.city " +
                    "from Rent r, Vehicle v where r.vid = v.vid and r.rid =" + rid;

            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            result = new RentConfirmationMessageModel(rs.getInt("rid"), rs.getString("cellphone"),
                    rs.getTimestamp("fromDateTime"), rs.getTimestamp("toDateTime"),
                    rs.getString("cardName"), rs.getString("cardNo"), rs.getDate("expDate"),
                    rs.getDouble("odometer"), rs.getInt("confNo"), rs.getInt("vid"),
                    rs.getString("vlicense"), rs.getString("vtname"), rs.getString("location"),
                    rs.getString("city"));

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;

        }


    }

    //tested
    public VehicleTypeModel getRateInfo(int rid) {
        VehicleTypeModel result = null;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "select * from VehicleType vt " +
                    "where vt.vtname = ANY( select v.vtname From rent r, " +
                    "vehicle v where r.vid = v.vid and r.rid = " + rid + ")";

            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            result = new VehicleTypeModel(rs.getString("vtname"), rs.getString("features"),
                    rs.getFloat("wrate"), rs.getFloat("drate"), rs.getFloat("hrate"),
                    rs.getFloat("wirate"), rs.getFloat("dirate"), rs.getFloat("hirate"),
                    rs.getFloat("krate"));

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;

        }


    }

    //tested
    // EFFECTS: returns the model for time interval and odometer to calculate cost
    public TimeIntervalOdometerModel getTimeIntervalAndOdometer(int rid) {
        TimeIntervalOdometerModel result = null;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "select fromDateTime, toDateTime, odometer " +
                    "from rent " +
                    "where rid = " + rid;
            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            result = new TimeIntervalOdometerModel(rs.getTimestamp("fromDateTime"), rs.getTimestamp("toDateTime"),
                    rs.getDouble("odometer"));

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }



        /*
        select fromDateTime, toDateTime, odometer
from rent
where rid = 4
         */
    }


    // table manipulation for Vehicle

    // EFFECTS: returns all the vehicle in the table
    public VehicleModel[] getVehicleInfo() {
        ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String query = "select * from vehicle";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                VehicleModel model = new VehicleModel(rs.getInt("vid"),
                        rs.getString("vlicense"),
                        rs.getString("make"), rs.getString("model"), rs.getString("year"),
                        rs.getString("color"), rs.getDouble("odometer"), rs.getString("status"),
                        rs.getString("vtname"), rs.getString("location"), rs.getString("city"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println(DatabaseConnectionHandler.EXCEPTION_TAG + " " + e.getMessage());
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new VehicleModel[result.size()]);
        }


    }


    // EFFECTS: delete record from vehicle with certain vid
    public boolean deleteVehicle(String vid) {
        boolean isSuccessful = false;

        PreparedStatement ps = null;
        int vidnum = Integer.parseInt(vid);
        try {
            ps = connection.prepareStatement("DELETE FROM vehicle WHERE vid = ?");
            ps.setInt(1, vidnum);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Vehicle " + vid + " does not exist!");
            } else {
                isSuccessful = true;
            }
            connection.commit();


        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return isSuccessful;
        }
    }

    // EFFECTS: insert a record to vehicle table
    public boolean insertVehicle(String vlicense, String make, String model, String year, String color,
                                 String odometer, String status, String vtname, String location, String city) {

        boolean isSuccessful = false;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("INSERT INTO vehicle (vlicense, " +
                    "make, model, year, color, odometer, status, vtname, location, city" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, vlicense);
            ps.setString(2, make);
            ps.setString(3, model);
            ps.setString(4, year);
            ps.setString(5, color);
            ps.setDouble(6, Double.parseDouble(odometer));
            ps.setString(7, status);
            ps.setString(8, vtname);
            ps.setString(9, location);
            ps.setString(10, city);

            int rowCount = ps.executeUpdate();
            if (rowCount != 0) {
                isSuccessful = true;
            }

            connection.commit();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return isSuccessful;
        }
    }

    // EFFECTS: update the table vehicle
    public boolean updateVehicle(String vid, String vlicense, String make, String model, String year, String color,
                                 String odometer, String status, String vtname, String location, String city) {

        vid = nullStringConverter(vid);
        if (vid.equals("") || !isInt(vid)) {
            return false;
        }

        vlicense = nullStringConverter(vlicense);
        make = nullStringConverter(make);
        model = nullStringConverter(model);
        year = nullStringConverter(year);
        color = nullStringConverter(color);
        odometer = nullStringConverter(odometer);
        status = nullStringConverter(status);
        vtname = nullStringConverter(vtname);
        location = nullStringConverter(location);
        city = nullStringConverter(city);


        int vidNum = Integer.parseInt(vid);
        String idName = "vid";
        String tableName = "vehicle";
        boolean[] subSuccess = new boolean[9];
        for (int i = 0; i < subSuccess.length; i++) {
            subSuccess[i] = true;
        }


        if (vlicense.length() > 0) {
            subSuccess[0] = updateTableWithIntegerkey(vidNum, "vlicense", vlicense, tableName, idName, TYPE_STRING);
        }
        if (make.length() > 0) {
            subSuccess[1] = updateTableWithIntegerkey(vidNum, "make", make, tableName, idName, TYPE_STRING);
        }
        if (model.length() > 0) {
            subSuccess[2] = updateTableWithIntegerkey(vidNum, "model", model, tableName, idName, TYPE_STRING);
        }
        if (year.length() > 0) {
            subSuccess[3] = updateTableWithIntegerkey(vidNum, "year", year, tableName, idName, TYPE_STRING);
        }
        if (color.length() > 0) {
            subSuccess[4] = updateTableWithIntegerkey(vidNum, "color", color, tableName, idName, TYPE_STRING);
        }
        if (odometer.length() > 0) {
            subSuccess[5] = updateTableWithIntegerkey(vidNum, "odometer", odometer, tableName, idName, TYPE_DOUBLE);
        }
        if (status.length() > 0) {
            subSuccess[6] = updateTableWithIntegerkey(vidNum, "status", status, tableName, idName, TYPE_STRING);
        }
        if (vtname.length() > 0) {
            subSuccess[7] = updateTableWithIntegerkey(vidNum, "vtname", vtname, tableName, idName, TYPE_STRING);
        }
        if (location.length() > 0 || city.length() > 0) {
            subSuccess[8] = updateBranchInfoInTableWithIntegerkey(vidNum, location, city, tableName, idName);
        }

        for (int i = 0; i < subSuccess.length; i++) {
            if (subSuccess[i] == false) {
                return false;
            }
        }

        return true;


    }

    // table manipulation for Rent

    /*
    RID					   NOT NULL NUMBER(38)
    VID						    NUMBER(38)
    CELLPHONE					    CHAR(10)
    FROMDATETIME					    TIMESTAMP(6)
    TODATETIME					    TIMESTAMP(6)
    ODOMETER					    NUMBER
    CARDNAME					    VARCHAR2(50) //
    CARDNO 					    CHAR(16)//
    EXPDATE					    DATE
    CONFNO 					    NUMBER(38)
    */

    // EFFECTS: returns all records in rent table
    public RentModel[] getRentInfo() {
        ArrayList<RentModel> result = new ArrayList<RentModel>();

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM rent");

            while (rs.next()) {
                RentModel model = new RentModel(rs.getInt("rid"), rs.getInt("vid"),
                        rs.getString("cellphone"), rs.getTimestamp("fromDateTime"),
                        rs.getTimestamp("toDateTIme"), rs.getDouble("odometer"),
                        rs.getString("cardName"), rs.getString("cardNo"),
                        rs.getDate("expDate"), rs.getInt("confNo"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println(DatabaseConnectionHandler.EXCEPTION_TAG + " " + e.getMessage());
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new RentModel[result.size()]);


        }


    }

    // EFFECTS: delete a record from Rent based on the rid
    public boolean deleteRent(String rid) {
        boolean isSuccessful = false;
        int ridnum = Integer.parseInt(rid);
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DELETE FROM rent WHERE rid = ?");
            ps.setInt(1, ridnum);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Rent " + rid + " does not exist!");
            } else {
                isSuccessful = true;
            }

            connection.commit();


        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return isSuccessful;
        }

    }

    // EFFECTS: insert a record to rent table
    public boolean insertRent(String vid, String cellphone,
                              String fromDateTime, String toDateTime, String odometer, String cardName, String cardNo, String expDate, String confNo) {
        boolean isSuccessful = false;

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("INSERT INTO rent (vid, " +
                    "cellphone, fromDateTime, toDateTime, odometer, cardName, cardNo, expDate, confNo" +
                    ") VALUES (?,?,to_timestamp(?, 'YYYY-MM-DD:HH24:MI'),to_timestamp(?, 'YYYY-MM-DD:HH24:MI'),?,?,?,to_date(?, 'YYYY-MM-DD'),?)");
            ps.setInt(1, Integer.parseInt(vid));
            ps.setString(2, cellphone);
            ps.setString(3, fromDateTime);
            ps.setString(4, toDateTime);
            ps.setDouble(5, Double.parseDouble(odometer));
            ps.setString(6, cardName);
            ps.setString(7, cardNo);
            ps.setString(8, expDate);
            ps.setInt(9, Integer.parseInt(confNo));

            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                isSuccessful = true;
            }
            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return isSuccessful;
        }

    }

        /*
    RID					   NOT NULL NUMBER(38)
    VID						    NUMBER(38)
    CELLPHONE					    CHAR(10)
    FROMDATETIME					    TIMESTAMP(6)
    TODATETIME					    TIMESTAMP(6)
    ODOMETER					    NUMBER
    CARDNAME					    VARCHAR2(50) //
    CARDNO 					    CHAR(16)//
    EXPDATE					    DATE
    CONFNO 					    NUMBER(38)
    */

    // EFFECTS: update the rent
    public boolean updateRent(String rid, String vid, String cellphone,
                              String fromDateTime, String toDateTime, String odometer, String cardName,
                              String cardNo, String expDate, String confNo) {

        rid = nullStringConverter(rid);
        if (rid.equals("") || !isInt(rid)) {
            return false;
        }

        vid = nullStringConverter(vid);
        cellphone = nullStringConverter(cellphone);
        fromDateTime = nullStringConverter(fromDateTime);
        toDateTime = nullStringConverter(toDateTime);
        odometer = nullStringConverter(odometer);
        cardName = nullStringConverter(cardName);
        cardNo = nullStringConverter(cardNo);
        expDate = nullStringConverter(expDate);
        confNo = nullStringConverter(cardNo);

        int ridNum = Integer.parseInt(rid);
        String idName = "rid";
        String tableName = "rent";
        boolean[] subSuccess = new boolean[9];
        for (int i = 0; i < subSuccess.length; i++) {
            subSuccess[i] = true;
        }

        if (vid.length() > 0) {
            subSuccess[0] = updateTableWithIntegerkey(ridNum, "vid", vid, tableName, idName, TYPE_INT);
        }
        if (cellphone.length() > 0) {
            subSuccess[1] = updateTableWithIntegerkey(ridNum, "cellphone", cellphone, tableName, idName, TYPE_STRING);
        }
        if (fromDateTime.length() > 0) {
            subSuccess[2] = updateTableWithIntegerkey(ridNum, "fromDateTime", fromDateTime, tableName, idName, TYPE_TIMESTAMP);
        }
        if (toDateTime.length() > 0) {
            subSuccess[3] = updateTableWithIntegerkey(ridNum, "toDateTime", toDateTime, tableName, idName, TYPE_TIMESTAMP);
        }
        if (odometer.length() > 0) {
            subSuccess[4] = updateTableWithIntegerkey(ridNum, "odometer", odometer, tableName, idName, TYPE_DOUBLE);
        }
        if (cardName.length() > 0) {
            subSuccess[5] = updateTableWithIntegerkey(ridNum, "cardName", cardName, tableName, idName, TYPE_STRING);
        }
        if (cardNo.length() > 0) {
            subSuccess[6] = updateTableWithIntegerkey(ridNum, "cardNo", cardNo, tableName, idName, TYPE_STRING);
        }
        if (expDate.length() > 0) {
            subSuccess[7] = updateTableWithIntegerkey(ridNum, "expDate", expDate, tableName, idName, TYPE_DATE);
        }
        if (expDate.length() > 0) {
            subSuccess[8] = updateTableWithIntegerkey(ridNum, "confNo", confNo, tableName, idName, TYPE_INT);
        }

        for (int i = 0; i < subSuccess.length; i++) {
            if (subSuccess[i] == false) {
                return false;

            }
        }
        return true;
    }


    // table manipulation for

    /*
     CONFNO 				   NOT NULL NUMBER(38)
 VID						    NUMBER(38)
 CELLPHONE					    CHAR(10)
 FROMDATETIME					    TIMESTAMP(6)
 TODATETIME					    TIMESTAMP(6)
     */

    // EFFECTS: returns all reservations
    public ReservationModel[] getReservationInfo() {
        ArrayList<ReservationModel> result = new ArrayList<ReservationModel>();

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM reservation");

            while (rs.next()) {
                ReservationModel model = new ReservationModel(rs.getInt("confNo"), rs.getInt("vid"),
                        rs.getString("cellphone"),
                        rs.getTimestamp("fromDateTime"), rs.getTimestamp("toDateTime"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println(DatabaseConnectionHandler.EXCEPTION_TAG + " " + e.getMessage());
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return result.toArray(new ReservationModel[result.size()]);


        }


    }

    // EFFECTS: delete a record from reservation based on confNo
    public boolean deleteReservation(String confNo) {
        boolean isSuccessful = false;
        int confNoInt = Integer.parseInt(confNo);
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DELETE FROM reservation WHERE confNo = ?");
            ps.setInt(1, confNoInt);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Reservation " + confNoInt + " does not exist!");
            } else {
                isSuccessful = true;
            }

            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return isSuccessful;
        }

    }

    // EFFECTS: insert a record into reservation
    public boolean insertReservation(String vid, String cellphone, String fromDateTime, String toDateTime) {
        boolean isSuccessful = false;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("INSERT INTO reservation (vid, " +
                    "cellphone, fromDateTime, toDateTime" +
                    ") VALUES (?,?,to_timestamp(?, 'YYYY-MM-DD:HH24:MI'),to_timestamp(?, 'YYYY-MM-DD:HH24:MI'))");
            ps.setInt(1, Integer.parseInt(vid));
            ps.setString(2, cellphone);
            ps.setString(3, fromDateTime);
            ps.setString(4, toDateTime);

            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                isSuccessful = true;
            }
            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return isSuccessful;
        }


    }

    // EFFECTS: update a record in reservation
    public boolean updateReservation(String confNo, String vid, String cellphone, String fromDateTime, String toDateTime) {

        confNo = nullStringConverter(confNo);
        if (confNo.equals("") || !isInt(confNo)) {
            return false;
        }

        vid = nullStringConverter(vid);
        cellphone = nullStringConverter(cellphone);
        fromDateTime = nullStringConverter(fromDateTime);
        toDateTime = nullStringConverter(toDateTime);


        int confNoInt = Integer.parseInt(confNo);
        String idName = "confNo";
        String tableName = "reservation";

        boolean[] subSuccess = new boolean[4];
        for (int i = 0; i < subSuccess.length; i++) {
            subSuccess[i] = true;
        }

        if (vid.length() > 0) {
            subSuccess[0] = updateTableWithIntegerkey(confNoInt, "vid", vid, tableName, idName, TYPE_INT);
        }
        if (cellphone.length() > 0) {
            subSuccess[1] = updateTableWithIntegerkey(confNoInt, "cellphone", cellphone, tableName, idName, TYPE_STRING);
        }
        if (fromDateTime.length() > 0) {
            subSuccess[2] = updateTableWithIntegerkey(confNoInt, "fromDateTime", fromDateTime, tableName, idName, TYPE_TIMESTAMP);
        }
        if (toDateTime.length() > 0) {
            subSuccess[3] = updateTableWithIntegerkey(confNoInt, "toDateTime", toDateTime, tableName, idName, TYPE_TIMESTAMP);
        }
        for (int i = 0; i < subSuccess.length; i++) {
            if (subSuccess[i] == false) {
                return false;
            }
        }
        return true;

    }


    // table manipulation for Return
    /*
     RID					   NOT NULL NUMBER(38)
 RETURNDATETIME 				    TIMESTAMP(6)
 ODOMETER					    NUMBER
 FULLTANK					    CHAR(1)
 VALUE						    NUMBER


     */

    //EFFECTS: returns all returns
    public ReturnModel[] getReturnInfo() {
        ArrayList<ReturnModel> result = new ArrayList<ReturnModel>();

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM return");

            while (rs.next()) {
                ReturnModel model = new ReturnModel(rs.getInt("rid"), rs.getTimestamp("returnDateTime"),
                        rs.getDouble("odometer"),
                        rs.getString("fulltank"), rs.getDouble("value"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println(DatabaseConnectionHandler.EXCEPTION_TAG + " " + e.getMessage());
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new ReturnModel[result.size()]);


        }


    }

    // EFFECTS: delete a record from return table
    public boolean deleteReturn(String rid) {

        boolean isSuccessful = false;
        int ridNum = Integer.parseInt(rid);
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DELETE FROM return WHERE rid = ?");
            ps.setInt(1, ridNum);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Return " + ridNum + " does not exist!");
            } else {
                isSuccessful = true;
            }

            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return isSuccessful;
        }
    }

    // EFFECTS: insert into return
    public boolean insertReturn(String rid, String returnDateTime, String odometer, String fulltank, String value) {
        boolean isSuccessful = false;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("INSERT INTO return VALUES " +
                    "(?, to_timestamp(?, 'YYYY-MM-DD:HH24:MI'), ?, ?, ?)");
            ps.setInt(1, Integer.parseInt(rid));
            ps.setString(2, returnDateTime);
            ps.setDouble(3, Double.parseDouble(odometer));
            ps.setString(4, fulltank);
            ps.setDouble(5, Double.parseDouble(value));

            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                isSuccessful = true;
            }
            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return isSuccessful;
        }

    }

    //EFFECTS: update return
    public boolean updateReturn(String rid, String returnDateTime, String odometer, String fulltank, String value) {
        rid = nullStringConverter(rid);
        if (rid.equals("") || !isInt(rid)) {
            return false;
        }

        returnDateTime = nullStringConverter(returnDateTime);
        odometer = nullStringConverter(odometer);
        fulltank = nullStringConverter(fulltank);
        value = nullStringConverter(value);

        int ridNum = Integer.parseInt(rid);
        String idName = "rid";
        String tableName = "return";

        boolean[] subSuccess = new boolean[4];
        for (int i = 0; i < subSuccess.length; i++) {
            subSuccess[i] = true;
        }

        if (returnDateTime.length() > 0) {
            subSuccess[0] = updateTableWithIntegerkey(ridNum, "returnDateTime", returnDateTime, tableName, idName, TYPE_TIMESTAMP);
        }
        if (odometer.length() > 0) {
            subSuccess[1] = updateTableWithIntegerkey(ridNum, "odometer", odometer, tableName, idName, TYPE_DOUBLE);
        }
        if (fulltank.length() > 0) {
            subSuccess[2] = updateTableWithIntegerkey(ridNum, "fulltank", fulltank, tableName, idName, TYPE_STRING);
        }
        if (value.length() > 0) {
            subSuccess[3] = updateTableWithIntegerkey(ridNum, "value", value, tableName, idName, TYPE_DOUBLE);
        }
        for (int i = 0; i < subSuccess.length; i++) {
            if (subSuccess[i] == false) {
                return false;
            }
        }
        return true;


    }


    // table manipulation for Customer

    /*
     CELLPHONE				   NOT NULL CHAR(10)
 NAME						    VARCHAR2(50)
 ADDRESS					    VARCHAR2(50)
 DLICENSE					    CHAR(9)


     */

    // EFFECTS: returns all customers
    public CustomerModel[] getCustomerInfo() {
        ArrayList<CustomerModel> result = new ArrayList<CustomerModel>();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customer");

            while (rs.next()) {
                CustomerModel model = new CustomerModel(rs.getString("cellphone"), rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("dlicense"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println(DatabaseConnectionHandler.EXCEPTION_TAG + " " + e.getMessage());
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return result.toArray(new CustomerModel[result.size()]);

        }


    }

    // EFFECTS: delete a record from customer
    public boolean deleteCustomer(String cellphone) {
        boolean isSuccessful = false;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DELETE FROM customer WHERE cellphone = ?");
            ps.setString(1, cellphone);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Customer with " + cellphone + " does not exist!");
            } else {
                isSuccessful = true;
            }

            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return isSuccessful;
        }

    }

    // EFFECTS: insert customer based on cellphone
    public boolean insertCustomer(String cellphone, String name, String address, String dlicense) {
        boolean isSuccessful = false;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("INSERT INTO customer VALUES " +
                    "(?, ?, ?, ?)");
            ps.setString(1, cellphone);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, dlicense);

            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                isSuccessful = true;
            }
            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return isSuccessful;
        }


    }

    // EFFECTS: update customer
    public boolean updateCustomer(String cellphone, String name, String address, String dlicense) {

        if (cellphone.equals("")) {
            return false;
        }

        name = nullStringConverter(name);
        address = nullStringConverter(address);
        dlicense = nullStringConverter(dlicense);

        String key = cellphone;
        String idName = "cellphone";
        String tableName = "customer";

        boolean[] subSuccess = new boolean[3];
        for (int i = 0; i < subSuccess.length; i++) {
            subSuccess[i] = true;
        }

        if (name.length() > 0) {
            subSuccess[0] = updateTableWithStringkey(key, "name", name, tableName, idName, TYPE_STRING);
        }
        if (address.length() > 0) {
            subSuccess[1] = updateTableWithStringkey(key, "address", address, tableName, idName, TYPE_STRING);
        }
        if (dlicense.length() > 0) {
            subSuccess[2] = updateTableWithStringkey(key, "dlicense", dlicense, tableName, idName, TYPE_STRING);
        }
        for (int i = 0; i < subSuccess.length; i++) {
            if (subSuccess[i] == false) {
                return false;
            }
        }

        return true;

    }


    // table maipulation for Vehicle type

    /*
     VTNAME 				   NOT NULL VARCHAR2(9)
 FEATURES					    VARCHAR2(100)
 WRATE						    FLOAT(126)
 DRATE						    FLOAT(126)
 HRATE						    FLOAT(126)
 WIRATE 					    FLOAT(126)
 DIRATE 					    FLOAT(126)
 HIRATE 					    FLOAT(126)
 KRATE						    FLOAT(126)
     */

    // EFFECTS: returns all vehicle types
    public VehicleTypeModel[] getVehicleTypeInfo() {
        ArrayList<VehicleTypeModel> result = new ArrayList<VehicleTypeModel>();

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM vehicleType");

            while (rs.next()) {
                VehicleTypeModel model = new VehicleTypeModel(rs.getString("vtname"), rs.getString("features"),
                        rs.getFloat("wrate"), rs.getFloat("drate"), rs.getFloat("hrate"),
                        rs.getFloat("wirate"), rs.getFloat("dirate"), rs.getFloat("hirate"),
                        rs.getFloat("krate"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println(DatabaseConnectionHandler.EXCEPTION_TAG + " " + e.getMessage());
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new VehicleTypeModel[result.size()]);

        }


    }

    // EFFECTS: delete a record from vehicle type
    public boolean deleteVehicleType(String vtname) {
        boolean isSuccessful = false;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DELETE FROM vehicleType WHERE vtname = ?");
            ps.setString(1, vtname);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " VehicleType " + vtname + " does not exist!");
            } else {
                isSuccessful = true;
            }

            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return isSuccessful;
        }

    }

    // EFFECTS: insert vehicle type
    public boolean insertVehicleType(String vtname, String features, String wrate, String drate, String hrate, String wirate,
                                     String dirate, String hirate, String krate) {
        boolean isSuccessful = false;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("INSERT INTO vehicleType VALUES " +
                    "(?,?,?,?,?,?,?,?,?)");
            ps.setString(1, vtname);
            ps.setString(2, features);
            ps.setFloat(3, Float.parseFloat(wrate));
            ps.setFloat(4, Float.parseFloat(drate));
            ps.setFloat(5, Float.parseFloat(hrate));
            ps.setFloat(6, Float.parseFloat(wirate));
            ps.setFloat(7, Float.parseFloat(dirate));
            ps.setFloat(8, Float.parseFloat(hirate));
            ps.setFloat(9, Float.parseFloat(krate));

            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                isSuccessful = true;
            }
            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return isSuccessful;
        }

    }

    // EFFECTS: update vehicle types
    public boolean updateVehicleType(String vtname, String features, String wrate, String drate, String hrate, String wirate,
                                     String dirate, String hirate, String krate) {

        if (vtname.equals("")) {
            return false;
        }

        features = nullStringConverter(features);
        wrate = nullStringConverter(wrate);
        drate = nullStringConverter(drate);
        hrate = nullStringConverter(hrate);
        wirate = nullStringConverter(wirate);
        dirate = nullStringConverter(dirate);
        hirate = nullStringConverter(hirate);
        krate = nullStringConverter(krate);


        String key = vtname;
        String idName = "vtname";
        String tableName = "vehicleType";

        boolean[] subSuccess = new boolean[8];
        for (int i = 0; i < subSuccess.length; i++) {
            subSuccess[i] = true;
        }

        if (features.length() > 0) {
            updateTableWithStringkey(key, "features", features, tableName, idName, TYPE_STRING);
        }
        if (wrate.length() > 0) {
            updateTableWithStringkey(key, "wrate", wrate, tableName, idName, TYPE_FLOAT);
        }
        if (drate.length() > 0) {
            updateTableWithStringkey(key, "drate", drate, tableName, idName, TYPE_FLOAT);
        }
        if (hrate.length() > 0) {
            updateTableWithStringkey(key, "hrate", hrate, tableName, idName, TYPE_FLOAT);
        }
        if (wirate.length() > 0) {
            updateTableWithStringkey(key, "wirate", wirate, tableName, idName, TYPE_FLOAT);
        }
        if (dirate.length() > 0) {
            updateTableWithStringkey(key, "dirate", dirate, tableName, idName, TYPE_FLOAT);
        }
        if (hirate.length() > 0) {
            updateTableWithStringkey(key, "hirate", hirate, tableName, idName, TYPE_FLOAT);
        }
        if (krate.length() > 0) {
            updateTableWithStringkey(key, "krate", krate, tableName, idName, TYPE_FLOAT);
        }

        for (int i = 0; i < subSuccess.length; i++) {
            if (subSuccess[i] == false) {
                return false;
            }
        }
        return true;

    }


    // EFFECTS: returns all information on branch table
    public BranchModel[] getBranchInfo() {
        ArrayList<BranchModel> result = new ArrayList<>();

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM branch");

            while (rs.next()) {
                BranchModel model = new BranchModel(rs.getString("location"), rs.getString("city"));
                result.add(model);
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println(DatabaseConnectionHandler.EXCEPTION_TAG + " " + e.getMessage());
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result.toArray(new BranchModel[result.size()]);

        }

    }

    // EFFECTS: delete the branch with specified location and city
    public boolean deleteBranch(String location, String city) {
        boolean isSuccessful = false;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DELETE FROM branch WHERE location = ? and city = ?");
            ps.setString(1, location);
            ps.setString(2, city);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Branch at " + location + " in " + city + " does not exist!");
            } else {
                isSuccessful = true;
            }

            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return isSuccessful;
        }

    }

    // EFFECTS: insert branch
    public boolean insertBrnach(String location, String city) {
        boolean isSuccessful = false;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("INSERT INTO branch VALUES " +
                    "(?,?)");
            ps.setString(1, location);
            ps.setString(2, city);

            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                isSuccessful = true;
            }
            connection.commit();

        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return isSuccessful;
        }

    }

    // the update method for branch table is not added, since it only contains pairs of primary keys
    // and the primary key has to be immutable.


    // EFFECTS: returns all the info in the table except branch
    public String[][] viewAll() {
        String[][] allInfo = new String[6][];
        VehicleModel[] vehicles = getVehicleInfo();
        VehicleTypeModel[] vehicleTypes = getVehicleTypeInfo();
        CustomerModel[] customers = getCustomerInfo();
        ReservationModel[] reservations = getReservationInfo();
        RentModel[] rentals = getRentInfo();
        ReturnModel[] retuns = getReturnInfo();

        ArrayList<String> vehicleStrings = new ArrayList<>();
        ArrayList<String> vehicleTypeStrings = new ArrayList<>();
        ArrayList<String> customerStrings = new ArrayList<>();
        ArrayList<String> reservationStrings = new ArrayList<>();
        ArrayList<String> rentalStrings = new ArrayList<>();
        ArrayList<String> returnStrings = new ArrayList<>();

        for (VehicleModel vehicle : vehicles) {
            vehicleStrings.add(vehicle.toString());
        }
        String[] vehicleFinal = vehicleStrings.toArray(new String[vehicleStrings.size()]);

        for (VehicleTypeModel vt : vehicleTypes) {
            vehicleTypeStrings.add(vt.toString());
        }
        String[] vehicleTypeFinal = vehicleTypeStrings.toArray(new String[vehicleTypeStrings.size()]);

        for (CustomerModel customer : customers) {
            customerStrings.add(customer.toString());
        }
        String[] customerFinal = customerStrings.toArray(new String[customerStrings.size()]);

        for (ReservationModel reservation : reservations) {
            reservationStrings.add(reservation.toString());
        }
        String[] reservationFinal = reservationStrings.toArray(new String[reservationStrings.size()]);

        for (RentModel rental : rentals) {
            rentalStrings.add(rental.toString());
        }
        String[] rentalFinal = rentalStrings.toArray(new String[rentalStrings.size()]);

        for (ReturnModel returnModel : retuns) {
            returnStrings.add(returnModel.toString());
        }
        String[] returnFinal = returnStrings.toArray(new String[rentalStrings.size()]);

        allInfo[0] = vehicleFinal;
        allInfo[1] = vehicleTypeFinal;
        allInfo[2] = customerFinal;
        allInfo[3] = reservationFinal;
        allInfo[4] = rentalFinal;
        allInfo[5] = returnFinal;

        return allInfo;


    }


    //

    public boolean updateTableWithIntegerkey(int id, String columnName, String value, String tableName, String idName, String valueType) {
        boolean isSuccessful = false;
        String query = "";
        if (valueType.equals(TYPE_STRING) || valueType.equals(TYPE_INT) || valueType.equals(TYPE_DOUBLE)) {
            query = queryGeneratorForUpdate(tableName, columnName, idName);
        } else if (valueType.equals(TYPE_TIMESTAMP)) {
            query = queryGeneratorTimeStampForUpdate(tableName, columnName, idName);
        } else {
            query = queryGeneratorDateForUpdate(tableName, columnName, idName);
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
            if (valueType.equals(TYPE_INT)) {
                ps.setInt(1, Integer.parseInt(value));
            } else if (valueType.equals(TYPE_DOUBLE)) {
                ps.setDouble(1, Double.parseDouble(value));
            } else {
                ps.setString(1, value);
            }
            ps.setInt(2, id);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + "Update is not reflected on  " + tableName + " " + id);
            } else {
                isSuccessful = true;
            }

            connection.commit();


        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            }
            return isSuccessful;
        }
    }

    public boolean updateBranchInfoInTableWithIntegerkey(int id, String location, String city, String tableName, String idName) {
        boolean isSuccessful = false;
        String query = "update " + tableName + " set location = ?, city = ? where " + idName + " = ?";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, location);
            ps.setString(2, city);
            ps.setInt(3, id);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " " + tableName + " " + id + " is not updated");
            } else {
                isSuccessful = true;
            }

            connection.commit();


        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return isSuccessful;
        }

    }

    public boolean updateTableWithStringkey(String id, String columnName, String value, String tableName, String idName, String valueType) {
        boolean isSuccessful = false;
        String query = "";
        if (valueType.equals(TYPE_STRING) || valueType.equals(TYPE_INT) || valueType.equals(TYPE_DOUBLE) || valueType.equals(TYPE_FLOAT)) {
            query = queryGeneratorForUpdate(tableName, columnName, idName);
        } else if (valueType.equals(TYPE_TIMESTAMP)) {
            query = queryGeneratorTimeStampForUpdate(tableName, columnName, idName);
        } else {
            query = queryGeneratorDateForUpdate(tableName, columnName, idName);
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
            if (valueType.equals(TYPE_INT)) {
                ps.setInt(1, Integer.parseInt(value));
            } else if (valueType.equals(TYPE_DOUBLE)) {
                ps.setDouble(1, Double.parseDouble(value));
            } else {
                ps.setString(1, value);
            }
            ps.setString(2, id);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " " + tableName + " " + id + " is not updated!");
            } else {
                isSuccessful = true;
            }

            connection.commit();


        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return isSuccessful;
        }
    }

    public String queryGeneratorForUpdate(String tableName, String columnName, String idName) {
        return "UPDATE " + tableName + " SET " + columnName + " = ? WHERE " + idName + " = ?";
    }

    public String queryGeneratorTimeStampForUpdate(String tableName, String columnName, String idName) {
        return "UPDATE " + tableName + " SET " + columnName + " = to_timestamp(?, 'YYYY-MM-DD:HH24:MI') WHERE " + idName + " = ?";
    }


    public String queryGeneratorDateForUpdate(String tableName, String columnName, String idName) {
        return "UPDATE " + tableName + " SET " + columnName + " = to_date(?, 'YYYY-MM-DD') WHERE " + idName + " = ?";
    }

    public String nullStringConverter(String input) {
        if (input == null || input.equals("")) {
            return "";
        } else {
            return input;
        }
    }

    public boolean isInt(String input) {
        int foo = 0;
        try {
            foo = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;

    }

}
