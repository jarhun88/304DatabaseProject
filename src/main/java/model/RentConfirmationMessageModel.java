package model;

import java.sql.Timestamp;
import java.util.Date;

public class RentConfirmationMessageModel {
    private final int rid;
    private final String cellphone;
    private final Timestamp fromDateTime;
    private final Timestamp toDateTime;
    private final String cardName;
    private final String cardNo;
    private final Date expDate;
    private final double odometer;
    private final int confNo;
    private final int vid;
    private final String vlicense;
    private final String vtname;
    private final String location;
    private final String city;

    public RentConfirmationMessageModel(int rid, String cellphone, Timestamp fromDateTime, Timestamp toDateTime,
                                        String cardName, String cardNo, Date expDate, double odometer, int confNo, int vid,
                                        String vlicense, String vtname, String location, String city) {
        this.rid = rid;
        this.cellphone = cellphone;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.cardName = cardName;
        this.cardNo = cardNo;
        this.expDate = expDate;
        this.odometer = odometer;
        this.confNo = confNo;
        this.vid = vid;
        this.vlicense = vlicense;
        this.vtname = vtname;
        this.location = location;
        this.city = city;
    }

    public int getRid() {
        return rid;
    }

    public String getCellphone() {
        return cellphone;
    }

    public Timestamp getFromDateTime() {
        return fromDateTime;
    }

    public Timestamp getToDateTime() {
        return toDateTime;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public Date getExpDate() {
        return expDate;
    }

    public double getOdometer() {
        return odometer;
    }

    public int getConfNo() {
        return confNo;
    }

    public int getVid() {
        return vid;
    }

    public String getVlicense() {
        return vlicense;
    }

    public String getVtname() {
        return vtname;
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }
}



