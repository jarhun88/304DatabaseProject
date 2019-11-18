package model;

import java.util.Date;

public class RentModel {
    private final int rid;
    private final int vid;
    private final String cellphone;
    private final Date fromDateTime;
    private final Date toDateTime;
    private final int odometer;
    private final String cardName;
    private final String cardNo;
    private final Date expDate;
    private final int confNo;

    public RentModel(int rid, int vid, String cellphone,
                     Date fromDateTime, Date toDateTime, int odometer, String cardName, String cardNo, Date expDate, int confNo) {
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
    }

    public int getConfNo() {
        return confNo;
    }

    public Date getExpDate() {
        return expDate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getCardName() {
        return cardName;
    }

    public int getOdometer() {
        return odometer;
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

    public int getVid() {
        return vid;
    }

    public int getRid() {
        return rid;
    }
}
