package model;

public class ReportTotalNumAndRevenueOnBranchModel {

    private final double totalRevenue;
    private final int totalNumOfVehicle;

    public ReportTotalNumAndRevenueOnBranchModel(double totalRevenue, int totalNumOfVehicle) {
        this.totalRevenue = totalRevenue;
        this.totalNumOfVehicle = totalNumOfVehicle;

    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public int getTotalNumOfVehicle() {
        return totalNumOfVehicle;
    }

}
