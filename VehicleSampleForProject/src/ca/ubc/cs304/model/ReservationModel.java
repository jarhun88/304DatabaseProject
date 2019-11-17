package ca.ubc.cs304.model;

public class ReservationModel {

    private int confNo;
    private String vtname;
    private String cellphone;
    private String fromDateTime;
    private String toDateTime;

    public ReservationModel(int confNo, String vtname, String cellphone, String fromDateTime, String toDateTime) {
        this.confNo = confNo;
        this.vtname = vtname;
        this.cellphone = cellphone;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    public int getConfNo() {
        return confNo;
    }

    public String getVtname() {
        return vtname;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getFromDateTime() {
        return fromDateTime;
    }

    public String getToDateTime() {
        return toDateTime;
    }
}
