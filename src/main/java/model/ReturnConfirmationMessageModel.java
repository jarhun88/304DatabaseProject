package model;

import java.sql.Timestamp;

public class ReturnConfirmationMessageModel {
    private final int rid;
    private final String returnDateTime;
    private final double value;
    private final String calculationDetail;

    public ReturnConfirmationMessageModel(int rid, String returnDateTime, double value, String calculationDetail) {
        this.rid = rid;
        this.returnDateTime = returnDateTime;
        this.value = value;
        this.calculationDetail = calculationDetail;
    }

    public int getRid() {
        return rid;
    }

    public String getReturnDateTime() {
        return returnDateTime;
    }

    public double getValue() {
        return value;
    }

    public String getCalculationDetail() {
        return calculationDetail;
    }

}
