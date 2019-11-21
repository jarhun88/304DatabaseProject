package api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;

public class TableManipulationController {

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/reservation-manipulation", consumes = "multipart/form-data", produces = "application/json")
    public String reservationManipulation(@RequestPart(name="mType") String mType,
                                          @RequestPart(name="confNo") String confNo,
                                          @RequestPart(name="vtname", required=false) String vtname,
                                          @RequestPart(name="phoneNum", required=false) String phoneNum,
                                          @RequestPart(name="from", required=false) String from,
                                          @RequestPart(name="to", required=false) String to) {
        return "reservationManipulation";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/rental-manipulation", consumes = "multipart/form-data", produces = "application/json")
    public String rentalManipulation(@RequestPart(name="mType") String mType,
                                     @RequestPart(name="rid") String date,
                                     @RequestPart(name="vtname", required=false) String vtname,
                                     @RequestPart(name="phoneNum", required=false) String phoneNum,
                                     @RequestPart(name="from", required=false) String from,
                                     @RequestPart(name="to", required=false) String to,
                                     @RequestPart(name="odometer", required=false) String odometer,
                                     @RequestPart(name="cardName", required=false) String cardName,
                                     @RequestPart(name="cardNo", required=false) String city,
                                     @RequestPart(name="expDate", required=false) String license,
                                     @RequestPart(name="confNo", required=false) String confNo) {
        return "rentalManipulation";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/return-manipulation", consumes = "multipart/form-data", produces = "application/json")
    public String returnManipulation(@RequestPart(name="mType", required=true) String mType,
                                     @RequestPart(name="rid") String date,
                                     @RequestPart(name="vtname", required=false) String vtname,
                                     @RequestPart(name="city", required=false) String city,
                                     @RequestPart(name="to", required=false) String to) {
        return "returnManipulation";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/vehicle-manipulation", consumes = "multipart/form-data", produces = "application/json")
    public String vehicleManipulation(@RequestPart(name="mType", required=true) String mType,
                                      @RequestPart(name="vid") String vid,
                                      @RequestPart(name="vlicense", required=false) String vlicense,
                                      @RequestPart(name="model", required=false) String model,
                                      @RequestPart(name="year", required=false) String year,
                                      @RequestPart(name="color", required=false) String color,
                                      @RequestPart(name="odometer", required=false) String odometer,
                                      @RequestPart(name="status", required=false) String status,
                                      @RequestPart(name="vtname", required=false) String vtname,
                                      @RequestPart(name="location", required=false) String location,
                                      @RequestPart(name="city", required=false) String city) {
        return "vehicleManipulation";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/vehicle-type-manipulation", consumes = "multipart/form-data", produces = "application/json")
    public String vehicleTypeManipulation(@RequestPart(name="mType", required=true) String mType,
                                          @RequestPart(name="vtname") String date,
                                          @RequestPart(name="features", required=false) String features,
                                          @RequestPart(name="wrate", required=false) String wrate,
                                          @RequestPart(name="drate", required=false) String drate,
                                          @RequestPart(name="hrate", required=false) String hrate,
                                          @RequestPart(name="wirate", required=false) String wirate,
                                          @RequestPart(name="dirate", required=false) String dirate,
                                          @RequestPart(name="hirate", required=false) String hirate,
                                          @RequestPart(name="krate", required=false) String krate) {
        return "vehicleTypeManipulation";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/customer-manipulation", consumes = "multipart/form-data", produces = "application/json")
    public String customerManipulation(@RequestPart(name="mType", required=true) String mType,
                                       @RequestPart(name="phoneNum", required=false) String phoneNum,
                                       @RequestPart(name="address", required=false) String address,
                                       @RequestPart(name="name", required=false) String name,
                                       @RequestPart(name="city", required=false) String city,
                                       @RequestPart(name="license", required=false) String license) {
        return "customerManipulation";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/view-all", consumes = "multipart/form-data", produces = "application/json")
    public String viewAll() {
        return "viewall";
    }

}
