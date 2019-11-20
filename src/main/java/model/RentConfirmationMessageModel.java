package model;

import java.sql.Timestamp;
import java.util.Date;

public class RentConfirmationMessageModel {
    private final int rid;
    private final int vid;
    private final String cellphone;
    private final Timestamp fromDateTime;
    private final Timestamp toDateTime;
    private final int odometer;
    private final String cardName;
    private final String cardNo;
    private final Date expDate;
    private final int confNo;

    private final int vlicense;
    private final String make;
    private final String model;
    private final String year;
    private final String color;
    private final String status;
    private final String vtname;
    private final String location;
    private final String city;


    public RentConfirmationMessageModel(int rid, int vid, String cellphone, Timestamp fromDateTime,
                                        Timestamp toDateTime, int odometer, String cardName, String cardNo, Date expDate,
                                        int confNo, int vlicense, String make, String model, String year, String color,
                                        String status, String vtname, String location, String city) {
        this.rid = rid;
        this.vid = vid;
        this.cellphone = cellphone;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.odometer = odometer;
        this.cardName = cardName;
        this.cardNo = cardNo;
        this.expDate = expDate;
        this.confNo = confNo;
        this.vlicense = vlicense;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.status = status;
        this.vtname = vtname;
        this.location = location;
        this.city = city;
    }

    public int getRid() {
        return rid;
    }

    public int getVid() {
        return vid;
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

    public int getOdometer() {
        return odometer;
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

    public int getConfNo() {
        return confNo;
    }

    public int getVlicense() {
        return vlicense;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public String getStatus() {
        return status;
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



