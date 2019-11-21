package model;

public class ReportTotalNumAndRevenueGBBranchModel {

    private final int totalRevenue;
    private final int totalNumOfVehicle;
    private final String location;
    private final String city;

    public ReportTotalNumAndRevenueGBBranchModel(int totalRevenue, int totalNumOfVehicle, String location, String city) {
        this.totalRevenue = totalRevenue;
        this.totalNumOfVehicle = totalNumOfVehicle;
        this.location = location;
        this.city = city;
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    public int getTotalNumOfVehicle() {
        return totalNumOfVehicle;
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }
}
