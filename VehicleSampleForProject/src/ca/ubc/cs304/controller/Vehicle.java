package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.LoginWindowDelegate;
import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.VehicleModel;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.TerminalTransactions;

/**
 * This is the main controller class that will orchestrate everything.
 */
public class Vehicle implements LoginWindowDelegate, TerminalTransactionsDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;

    public Vehicle() {
        dbHandler = new DatabaseConnectionHandler();
    }

    private void start() {
        loginWindow = new LoginWindow();
        loginWindow.showFrame(this);
    }

    /**
     * LoginWindowDelegate Implementation
     * <p>
     * connects to Oracle database with supplied username and password
     */
    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            // Once connected, remove login window and start text transaction flow
            loginWindow.dispose();

            TerminalTransactions transaction = new TerminalTransactions();
            transaction.showMainMenu(this);
        } else {
            loginWindow.handleLoginFailed();

            if (loginWindow.hasReachedMaxLoginAttempts()) {
                loginWindow.dispose();
                System.out.println("You have exceeded your number of allowed attempts");
                System.exit(-1);
            }
        }
    }

    /**
     * TermainalTransactionsDelegate Implementation
     *
     * Insert a branch with the given info
     */
//    public void insertBranch(VehicleModel model) {
//    	dbHandler.insertBranch(model);
//    }

    /**
     * TermainalTransactionsDelegate Implementation
     *
     * Delete branch with given branch ID.
     */
//    public void deleteBranch(int branchId) {
//    	dbHandler.deleteBranch(branchId);
//    }

    /**
     * TermainalTransactionsDelegate Implementation
     *
     * Update the branch name for a specific ID
     */

//    public void updateBranch(int branchId, String name) {
//    	dbHandler.updateBranch(branchId, name);
//    }

    /**
     * TermainalTransactionsDelegate Implementation
     * <p>
     * Displays information about varies bank branches.
     */
    public void showVehicle() {
        VehicleModel[] models = dbHandler.getAllVehicleInfo();
        String result = queryResultOfVehicle(models);
        System.out.println(result);
    }

    @Override
    public void showAvailableVehicle() {
        VehicleModel[] models = dbHandler.getAvailableVehicle();
        String result = queryResultOfVehicle(models);
        System.out.println(result);

    }

    @Override
    public void showDesiredVehicleForUser(String vtname, String location, String city, String fromDateTime, String toDateTime) {
        VehicleModel[] models = dbHandler.showDesiredVehicleForUser(vtname,location,city,fromDateTime,toDateTime);
        String result = queryResultOfVehicle(models);
        System.out.println(result);
    }


    // EFFECTS: returns the string of formatted the query result
    public String queryResultOfVehicle(VehicleModel[] models) {
        String result = "";

        result += String.format("%-20.20s", "VID");
        result += String.format("%-20.20s", "VLICENSE");
        result += String.format("%-20.20s", "MAKE");
        result += String.format("%-20.20s", "MODEL");
        result += String.format("%-20.20s", "YEAR");
        result += String.format("%-20.20s", "COLOR");
        result += String.format("%-20.20s", "ODOMETER");
        result += String.format("%-20.20s", "STATUS");
        result += String.format("%-20.20s", "VTNAME");
        result += String.format("%-20.20s", "LOCATION");
        result += String.format("%-20.20s", "CITY");
        result += "￿\n";

        for (int i = 0; i < models.length; i++) {
            VehicleModel model = models[i];

            // simplified output formatting; truncation may occur
//    		System.out.printf("%-10.10s", model.getId());
            result += String.format("%-20.20s", model.getVid());
            if (model.getVlicense() == null) {
                result += String.format("%-20.20s", "null");
            } else {
                result += String.format("%-20.20s", model.getVlicense());
            }
            if (model.getMake() == null) {
                result += String.format("%-20.20s", "null");
            } else {
                result += String.format("%-20.20s", model.getMake());
            }
            if (model.getModel() == null) {
                result += String.format("%-20.20s", "null");
            } else {
                result += String.format("%-20.20s", model.getModel());
            }
            if (model.getYear() == null) {
                result += String.format("%-20.20s", "null");
            } else {
                result += String.format("%-20.20s", model.getYear());
            }
            if (model.getColor() == null) {
                result += String.format("%-20.20s", "null");
            } else {
                result += String.format("%-20.20s", model.getColor());
            }
            if (model.getOdometer() == null) {
                result += String.format("%-20.20s", "null");
            } else {
                result += String.format("%-20.20s", model.getOdometer());
            }
            if (model.getStatus() == null) {
                result += String.format("%-20.20s", "null");
            } else {
                result += String.format("%-20.20s", model.getStatus());
            }
            if (model.getVtname() == null) {
                result += String.format("%-20.20s", "null");
            } else {
                result += String.format("%-20.20s", model.getVtname());
            }
            if (model.getLocation() == null) {
                result += String.format("%-20.20s", "null");
            } else {
                result += String.format("%-20.20s", model.getLocation());
            }
            if (model.getCity() == null) {
                result += String.format("%-20.20s", "null");
            } else {
                result += String.format("%-20.20s", model.getCity());
            }
            result += "￿\n";
        }

        return result;


    }

    /**
     * TerminalTransactionsDelegate Implementation
     * <p>
     * The TerminalTransaction instance tells us that it is done with what it's
     * doing so we are cleaning up the connection since it's no longer needed.
     */
    public void terminalTransactionsFinished() {
        dbHandler.close();
        dbHandler = null;

        System.exit(0);
    }

    /**
     * Main method called at launch time
     */
    public static void main(String args[]) {
        Vehicle vehicle = new Vehicle();
        vehicle.start();
    }
}
