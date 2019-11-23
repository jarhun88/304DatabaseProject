package model;

import java.sql.Timestamp;

public class ReturnModel {

    private final int rid;
    private final Timestamp returnDateTime;
    private final double odometer;
    private final String fulltank;
    private final double value;

    public ReturnModel(int rid, Timestamp returnDateTime, double odometer, String fulltank, double value) {
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

    public double getOdometer() {
        return odometer;
    }

    public Timestamp getReturnDateTime() {
        return returnDateTime;
    }

    public int getRid() {
        return rid;
    }

    @Override
    public String toString() {
        return "ReturnModel{" +
                "rid=" + rid +
                ", returnDateTime=" + returnDateTime +
                ", odometer=" + odometer +
                ", fulltank='" + fulltank + '\'' +
                ", value=" + value +
                '}';
    }
}
