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
		try  {
			connection.rollback();	
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}
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

			while(rs.next()) {
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

	public VehicleModel[] getVehicleInfo(String carType, String location, String startTime, String endTime) {
		ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();

		String cTypeFilter ="";
		String locFilter = "";
		String timeIntFilter = "";
		if(carType.length()>0){
   			cTypeFilter = "AND VTNAME = " + carType;
		}
		if(location.length()>0){
			locFilter = "AND LOCATION = " + location;
		}
		// Checks to see if a reservation has already been made from that interval
		if(startTime.length()>0 && endTime.length()>0){
			//  not exists( Select * from reservation where timeInterval between (FROMDATETIME, TODATETIME)
			timeIntFilter = "AND NOT EXISTS (Select * from Reservation WHERE " + startTime +
					"Between(FROMDATETIME, TODATETIME) AND "+ endTime + "Between(FROMDATETIME, TODATETIME)" ;
		}

		try {
			Statement stmt = connection.createStatement();
			String query = "SELECT * FROM Vehicle where STATUS = 'available'";
			query = query + cTypeFilter + locFilter + timeIntFilter;
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

			while(rs.next()) {
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
}
