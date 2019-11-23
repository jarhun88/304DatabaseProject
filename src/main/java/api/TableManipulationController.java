package api;

import controller.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class TableManipulationController {

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/res", consumes = "multipart/form-data", produces = "application/json")
    public String reservationManipulation(@RequestPart(name="mType") String mType,
                                          @RequestPart(name="confNo", required=false) String confNo,
                                          @RequestPart(name="vtname", required=false) String vtname,
                                          @RequestPart(name="phoneNum", required=false) String phoneNum,
                                          @RequestPart(name="from", required=false) String from,
                                          @RequestPart(name="to", required=false) String to) {
        Controller.reservationManipulation(mType, confNo, vtname, phoneNum, from, to);
        return "reservationManipulation";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/ren", consumes = "multipart/form-data", produces = "application/json")
    public String rentalManipulation(@RequestPart(name="mType") String mType,
                                     @RequestPart(name="rid", required=false) String date,
                                     @RequestPart(name="vtname", required=false) String vtname,
                                     @RequestPart(name="phoneNum", required=false) String phoneNum,
                                     @RequestPart(name="from", required=false) String from,
                                     @RequestPart(name="to", required=false) String to,
                                     @RequestPart(name="odometer", required=false) String odometer,
                                     @RequestPart(name="cardName", required=false) String cardName,
                                     @RequestPart(name="cardNo", required=false) String city,
                                     @RequestPart(name="expDate", required=false) String license,
                                     @RequestPart(name="confNo", required=false) String confNo) {
        Controller.rentalManipulation(mType, date, vtname, phoneNum, from, to, odometer, cardName, city, license, confNo);
        return "rentalManipulation";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/ret", consumes = "multipart/form-data", produces = "application/json")
    public String returnManipulation(@RequestPart(name="mType") String mType,
                                     @RequestPart(name="rid", required=false) String rid,
                                     @RequestPart(name="date", required=false) String date,
                                     @RequestPart(name="odometer", required=false) String odometer,
                                     @RequestPart(name="fulltank", required=false) String fulltank,
                                     @RequestPart(name="value", required=false) String value) {
        Controller.returnManipulation(mType, rid, date, odometer, fulltank, value);
        return "returnManipulation";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/vehicle", consumes = "multipart/form-data", produces = "application/json")
    public String vehicleManipulation(@RequestPart(name="mType") String mType,
                                      @RequestPart(name="vid", required=false) String vid,
                                      @RequestPart(name="vlicense", required=false) String vlicense,
                                      @RequestPart(name="model", required=false) String model,
                                      @RequestPart(name="year", required=false) String year,
                                      @RequestPart(name="color", required=false) String color,
                                      @RequestPart(name="odometer", required=false) String odometer,
                                      @RequestPart(name="status", required=false) String status,
                                      @RequestPart(name="vtname", required=false) String vtname,
                                      @RequestPart(name="location", required=false) String location,
                                      @RequestPart(name="city", required=false) String city) {

        Controller.vehicleManipulation(mType, vid, vlicense, model, year, color, odometer, status, vtname, location, city);
        return "vehicleManipulation";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/vehicle-type", consumes = "multipart/form-data", produces = "application/json")
    public String vehicleTypeManipulation(@RequestPart(name="mType") String mType,
                                          @RequestPart(name="vtname", required=false) String date,
                                          @RequestPart(name="features", required=false) String features,
                                          @RequestPart(name="wrate", required=false) String wrate,
                                          @RequestPart(name="drate", required=false) String drate,
                                          @RequestPart(name="hrate", required=false) String hrate,
                                          @RequestPart(name="wirate", required=false) String wirate,
                                          @RequestPart(name="dirate", required=false) String dirate,
                                          @RequestPart(name="hirate", required=false) String hirate,
                                          @RequestPart(name="krate", required=false) String krate) {

        Controller.vehicleTypeManipulation(mType, date, features, wrate, drate, hrate, wirate, dirate, hirate, krate);
        return "vehicleTypeManipulation";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/customer", consumes = "multipart/form-data", produces = "application/json")
    public String customerManipulation(@RequestPart(name="mType") String mType,
                                       @RequestPart(name="phoneNum", required=false) String phoneNum,
                                       @RequestPart(name="address", required=false) String address,
                                       @RequestPart(name="name", required=false) String name,
                                       @RequestPart(name="city", required=false) String city,
                                       @RequestPart(name="license", required=false) String license) {
        Controller.customerManipulation(mType, phoneNum, address, name, city, license);

        return "customerManipulation";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @GetMapping(path = "/view-all")
    public String viewAll() {
        Controller.viewAll();
        return "viewall";
    }

}
