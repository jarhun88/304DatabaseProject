package model;

public class RevenueReportGroupedByVehilceModel {
    private final double total;
    private final String vtname;

    public RevenueReportGroupedByVehilceModel(double total, String vtname) {
        this.total = total;
        this.vtname = vtname;
    }

    public double getTotal() {
        return total;
    }

    public String getVtname() {
        return vtname;
    }
}
