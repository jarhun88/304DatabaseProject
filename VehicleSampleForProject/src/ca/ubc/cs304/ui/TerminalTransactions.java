package ca.ubc.cs304.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ca.ubc.cs304.delegates.TerminalTransactionsDelegate;
import ca.ubc.cs304.model.VehicleModel;

/**
 * The class is only responsible for handling terminal text inputs.
 */
public class TerminalTransactions {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private static final int INVALID_INPUT = Integer.MIN_VALUE;
    private static final int EMPTY_INPUT = 0;

    private BufferedReader bufferedReader = null;
    private TerminalTransactionsDelegate delegate = null;

    public TerminalTransactions() {
    }

    /**
     * Displays simple text interface
     */
    public void showMainMenu(TerminalTransactionsDelegate delegate) {
        this.delegate = delegate;

        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int choice = INVALID_INPUT;

        while (choice != 3) {
            System.out.println();
            System.out.println("1. Show All Vehicles");
            System.out.println("2. Show Available Vehicles");
            System.out.println("3. Show Available Vehicles based on the customer's input");
            System.out.println("4. Quit");
            System.out.print("Please choose one of the above 4 options: ");

            choice = readInteger(false);

            System.out.println(" ");

            if (choice != INVALID_INPUT) {
                switch (choice) {

                    case 1:
                        delegate.showVehicle();
                        break;
                    case 2:
                        delegate.showAvailableVehicle();
                        break;
                    case 3:
                        handleViewDesiredVehicleOption();
                        break;
                    case 4:
                        handleQuitOption();
                        break;
                    default:
                        System.out.println(WARNING_TAG + " The number that you entered was not a valid option.");
                        break;
                }
            }
        }
    }


    private void handleQuitOption() {
        System.out.println("Good Bye!");

        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                System.out.println("IOException!");
            }
        }

        delegate.terminalTransactionsFinished();
    }


    private int readInteger(boolean allowEmpty) {
        String line = null;
        int input = INVALID_INPUT;
        try {
            line = bufferedReader.readLine();
            input = Integer.parseInt(line);
        } catch (IOException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        } catch (NumberFormatException e) {
            if (allowEmpty && line.length() == 0) {
                input = EMPTY_INPUT;
            } else {
                System.out.println(WARNING_TAG + " Your input was not an integer");
            }
        }
        return input;
    }

    private String readLine() {
        String result = null;
        try {
            result = bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return result;
    }

    private void handleViewDesiredVehicleOption() {

        System.out.print("Please enter the branch location in which you wish to see the availability: ");
        String location = readLine().trim();
        if (location.length() == 0) {
            location = null;
        }

        System.out.print("Please enter the branch city in which you wish to see the availability: ");
        String city = readLine().trim();
        if (city.length() == 0) {
            city = null;
        }

        System.out.print("Please enter the car type you wish to see the availability for: ");
        String vtname = readLine().trim();
        if (vtname.length() == 0) {
            vtname = null;
        }

        System.out.print("Please enter the starting date and time you wish to see the availability for: ");
        int[] dateTimeInfo = createDataTimeArray();
        String fromDateTime = generateDateTime(dateTimeInfo);

        System.out.print("Please enter the starting date and time you wish to see the availability for: ");
        dateTimeInfo = createDataTimeArray();
        String toDateTime = generateDateTime(dateTimeInfo);

        delegate.showDesiredVehicleForUser(vtname,location,city,fromDateTime,toDateTime);

    }

    private int[] createDataTimeArray() {
        int[] dateTimeInfo = new int[5];

        int year = INVALID_INPUT;
        while (year == INVALID_INPUT) {
            System.out.print("Please enter year: ");
            year = readInteger(true);
            dateTimeInfo[0] = year;
        }

        int month = INVALID_INPUT;
        while (month == INVALID_INPUT) {
            System.out.print("Please enter month: ");
            month = readInteger(true);
            dateTimeInfo[1] = month;
        }

        int day = INVALID_INPUT;
        while (day == INVALID_INPUT) {
            System.out.print("Please enter day: ");
            day = readInteger(true);
            dateTimeInfo[2] = day;
        }

        int hour = INVALID_INPUT;
        while (hour == INVALID_INPUT) {
            System.out.print("Please enter hour: ");
            hour = readInteger(true);
            dateTimeInfo[3] = hour;
        }

        int minute = INVALID_INPUT;
        while (minute == INVALID_INPUT) {
            System.out.print("Please enter hour: ");
            minute = readInteger(true);
            dateTimeInfo[4] = minute;
        }


        return dateTimeInfo;

    }

    private String generateDateTime(int[] dateTimeInfo) {
        String result = "" + dateTimeInfo[0] + "-" + dateTimeInfo[1] + "-" + dateTimeInfo[2] + ":" + dateTimeInfo[3] + ":" + dateTimeInfo[4];
        return result;
    }

    //	private void handleDeleteOption() {
//		int branchId = INVALID_INPUT;
//		while (branchId == INVALID_INPUT) {
//			System.out.print("Please enter the branch ID you wish to delete: ");
//			branchId = readInteger(false);
//			if (branchId != INVALID_INPUT) {
//				delegate.deleteBranch(branchId);
//			}
//		}
//	}

//	private void handleInsertOption() {
//		int id = INVALID_INPUT;
//		while (id == INVALID_INPUT) {
//			System.out.print("Please enter the branch ID you wish to insert: ");
//			id = readInteger(false);
//		}
//
//		String name = null;
//		while (name == null || name.length() <= 0) {
//			System.out.print("Please enter the branch name you wish to insert: ");
//			name = readLine().trim();
//		}
//
//		// branch address is allowed to be null so we don't need to repeatedly ask for the address
//		System.out.print("Please enter the branch address you wish to insert: ");
//		String address = readLine().trim();
//		if (address.length() == 0) {
//			address = null;
//		}
//
//		String city = null;
//		while (city == null || city.length() <= 0) {
//			System.out.print("Please enter the branch city you wish to insert: ");
//			city = readLine().trim();
//		}
//
//		int phoneNumber = INVALID_INPUT;
//		while (phoneNumber == INVALID_INPUT) {
//			System.out.print("Please enter the branch phone number you wish to insert: ");
//			phoneNumber = readInteger(true);
//		}
//
//		VehicleModel model = new VehicleModel(address,
//											city,
//											id,
//											name,
//											phoneNumber);
//		delegate.insertBranch(model);
//	}

    //	private void handleUpdateOption() {
//		int id = INVALID_INPUT;
//		while (id == INVALID_INPUT) {
//			System.out.print("Please enter the branch ID you wish to update: ");
//			id = readInteger(false);
//		}
//
//		String name = null;
//		while (name == null || name.length() <= 0) {
//			System.out.print("Please enter the branch name you wish to update: ");
//			name = readLine().trim();
//		}
//
//		delegate.updateBranch(id, name);
//	}


}
