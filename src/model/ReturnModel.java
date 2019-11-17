package model;

import java.sql.Timestamp;

public class ReturnModel {

    private final int rid;
    private final Timestamp returnDateTime;
    private final int odometer;
    private final boolean fulltank;
    private final int value;

    public ReturnModel(int rid, Timestamp returnDateTime, int odometer, boolean fulltank, int value) {
        this.rid = rid;
        this.returnDateTime = returnDateTime;
        this.odometer = odometer;
        this.fulltank = fulltank;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isFulltank() {
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
