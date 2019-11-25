package api;

import controller.Controller;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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


        return Controller.reservationManipulation(mType, confNo, vtname, phoneNum, from, to);
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/ren", consumes = "multipart/form-data", produces = "application/json")
    public String rentalManipulation(@RequestPart(name="mType") String mType,
                                     @RequestPart(name="rid", required=false) String rid,
                                     @RequestPart(name="vtname", required=false) String vtname,
                                     @RequestPart(name="phoneNum", required=false) String phoneNum,
                                     @RequestPart(name="from", required=false) String from,
                                     @RequestPart(name="to", required=false) String to,
                                     @RequestPart(name="odometer", required=false) String odometer,
                                     @RequestPart(name="cardName", required=false) String cardName,
                                     @RequestPart(name="cardNo", required=false) String cardNo,
                                     @RequestPart(name="expDate", required=false) String expDate,
                                     @RequestPart(name="confNo", required=false) String confNo) {


        return Controller.rentalManipulation(mType, rid, vtname, phoneNum, from, to, odometer, cardName, cardNo, expDate, confNo);
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/ret", consumes = "multipart/form-data", produces = "application/json")
    public String returnManipulation(@RequestPart(name="mType") String mType,
                                     @RequestPart(name="rid", required=false) String rid,
                                     @RequestPart(name="date", required=false) String date,
                                     @RequestPart(name="odometer", required=false) String odometer,
                                     @RequestPart(name="fulltank", required=false) String fulltank,
                                     @RequestPart(name="value", required=false) String value) {

        return  Controller.returnManipulation(mType, rid, date, odometer, fulltank, value);
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
                                      @RequestPart(name="city", required=false) String city) { //TODO add make

        String make ="";


        return Controller.vehicleManipulation(mType, vid, vlicense, make, model, year, color, odometer, status, vtname, location, city);
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/vehicle-type", consumes = "multipart/form-data", produces = "application/json")
    public String vehicleTypeManipulation(@RequestPart(name="mType") String mType,
                                          @RequestPart(name="vtname", required=false) String vtname,
                                          @RequestPart(name="features", required=false) String features,
                                          @RequestPart(name="wrate", required=false) String wrate,
                                          @RequestPart(name="drate", required=false) String drate,
                                          @RequestPart(name="hrate", required=false) String hrate,
                                          @RequestPart(name="wirate", required=false) String wirate,
                                          @RequestPart(name="dirate", required=false) String dirate,
                                          @RequestPart(name="hirate", required=false) String hirate,
                                          @RequestPart(name="krate", required=false) String krate) {


        return Controller.vehicleTypeManipulation(mType, vtname, features, wrate, drate, hrate, wirate, dirate, hirate, krate);
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/customer", consumes = "multipart/form-data", produces = "application/json")
    public String customerManipulation(@RequestPart(name="mType") String mType,
                                       @RequestPart(name="phone", required=false) String phone,
                                       @RequestPart(name="address", required=false) String address,
                                       @RequestPart(name="name", required=false) String name,
                                       @RequestPart(name="driverLicense", required=false) String driverLicense) {


        return Controller.customerManipulation(mType, phone, address, name, driverLicense);
    }
    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/branch", consumes = "multipart/form-data", produces = "application/json")
    public String branchManipulation(@RequestPart(name="mType") String mType,
                                       @RequestPart(name="city", required=false) String city,
                                       @RequestPart(name="location", required=false) String location) {
        return Controller.branchManipulation(mType, city, location);
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @GetMapping(path = "/view-all")
    public String viewAll() {

        JSONArray mJSONArray = new JSONArray(Arrays.asList(Controller.viewAll()));
        return mJSONArray.toString();
    }

}
