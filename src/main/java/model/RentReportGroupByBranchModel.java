package model;

public class RentReportGroupByBranchModel {
    private final int total;
    private final String location;
    private final String city;

    public RentReportGroupByBranchModel(int total, String location, String city) {
        this.total = total;
        this.location = location;
        this.city = city;
    }

    public int getTotal() {
        return total;
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }
}
