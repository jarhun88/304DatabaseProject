package ca.ubc.cs304.delegates;

import ca.ubc.cs304.model.VehicleModel;

/**
 * This interface uses the delegation design pattern where instead of having
 * the TerminalTransactions class try to do everything, it will only
 * focus on handling the UI. The actual logic/operation will be delegated to the
 * controller class (in this case Bank).
 * <p>
 * TerminalTransactions calls the methods that we have listed below but
 * Bank is the actual class that will implement the methods.
 */
public interface TerminalTransactionsDelegate {
    //	public void deleteBranch(int branchId);
//	public void insertBranch(VehicleModel model);
    public void showVehicle();

    public void showAvailableVehicle();
//	public void updateBranch(int branchId, String name);

    public void terminalTransactionsFinished();
}
