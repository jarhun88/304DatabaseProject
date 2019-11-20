package model;

import com.sun.tools.corba.se.idl.constExpr.Times;
import oracle.sql.TIMESTAMP;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class ReservationModel {

    private final int confNo;
    private final int vid;
    private final String cellphone;
    private final Timestamp  fromDateTime;
    private final Timestamp toDateTime;


    public ReservationModel(int confNo, int vid, String cellphone, Timestamp fromDateTime, Timestamp toDateTime) {
        this.confNo = confNo;
        this.vid = vid;
        this.cellphone = cellphone;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    public Timestamp getToDateTime() {
        return toDateTime;
    }

    public Timestamp getFromDateTime() {
        return fromDateTime;
    }

    public String getCellphone() {
        return cellphone;
    }

    public int getVid() {
        return vid;
    }

    public int getConfNo() {
        return confNo;
    }
}
