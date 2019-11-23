package model;

import java.sql.Timestamp;

public class TimeIntervalOdometerModel {
    private final Timestamp fromDateTime;
    private final Timestamp toDateTime;
    private final double odometer;

    public TimeIntervalOdometerModel(Timestamp fromDateTime, Timestamp toDateTime, double odometer) {
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.odometer = odometer;
    }

    public Timestamp getFromDateTime() {
        return fromDateTime;
    }

    public Timestamp getToDateTime() {
        return toDateTime;
    }

    public double getOdometer() {
        return odometer;
    }

    @Override
    public String toString() {
        return "TimeIntervalOdometerModel{" +
                "fromDateTime=" + fromDateTime +
                ", toDateTime=" + toDateTime +
                ", odometer=" + odometer +
                '}';
    }
}
