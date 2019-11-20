package model;

import java.sql.Timestamp;

public class ReturnConfirmationMessageModel {
    private final int rid;
    private final int confNo;
    private final Timestamp returnDateTime;
    private final double value;

    public ReturnConfirmationMessageModel(int rid, int confNo, Timestamp returnDateTime, double value) {
        this.rid = rid;
        this.confNo = confNo;
        this.returnDateTime = returnDateTime;
        this.value = value;
    }

    public int getRid() {
        return rid;
    }

    public int getConfNo() {
        return confNo;
    }

    public Timestamp getReturnDateTime() {
        return returnDateTime;
    }

    public double getValue() {
        return value;
    }
}
