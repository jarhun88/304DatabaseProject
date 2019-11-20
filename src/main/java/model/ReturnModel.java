package model;

import java.sql.Timestamp;

public class ReturnModel {

    private final int rid;
    private final Timestamp returnDateTime;
    private final int odometer;
    private final String fulltank;
    private final double value;

    public ReturnModel(int rid, Timestamp returnDateTime, int odometer, String fulltank, double value) {
        this.rid = rid;
        this.returnDateTime = returnDateTime;
        this.odometer = odometer;
        this.fulltank = fulltank;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String getFulltank() {
        return fulltank;
    }

    public int getOdometer() {
        return odometer;
    }

    public Timestamp getReturnDateTime() {
        return returnDateTime;
    }

    public int getRid() {
        return rid;
    }
}
