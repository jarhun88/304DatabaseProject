package model;

import java.util.Date;

public class ReservationModel {

    private final int confNo;
    private final String vtname;
    private final String cellphone;
    private final Date  fromDateTime;
    private final Date toDateTime;


    public ReservationModel(int confNo, String vtname, String cellphone, Date fromDateTime, Date toDateTime) {
        this.confNo = confNo;
        this.vtname = vtname;
        this.cellphone = cellphone;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    public Date getToDateTime() {
        return toDateTime;
    }

    public Date getFromDateTime() {
        return fromDateTime;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getVtname() {
        return vtname;
    }

    public int getConfNo() {
        return confNo;
    }
}
