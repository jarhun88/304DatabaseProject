package model;

public class RentReportGroupedByVehilceModel {
    private final int total;
    private final String vtname;

    public RentReportGroupedByVehilceModel(int total, String vtname) {
        this.total = total;
        this.vtname = vtname;
    }

    public int getTotal() {
        return total;
    }

    public String getVtname() {
        return vtname;
    }
}
