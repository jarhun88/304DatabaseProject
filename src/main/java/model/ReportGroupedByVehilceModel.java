package model;

public class ReportGroupedByVehilceModel {
    private final int total;
    private final String vtname;

    public ReportGroupedByVehilceModel(int total, String vtname) {
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
