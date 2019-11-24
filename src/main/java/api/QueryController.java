package api;

import controller.Controller;
import org.json.JSONArray;
import org.json.JSONString;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class QueryController {

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/view-vehicles", consumes = "multipart/form-data", produces = "application/json")
    public String viewVehicles(@RequestPart(name="carType", required=false) String carType,
                               @RequestPart(name="location", required=false) String location,
                               @RequestPart(name="city", required=false) String city,
                               @RequestPart(name="from", required=false) String from,
                               @RequestPart(name="to", required=false) String to) {

        JSONArray mJSONArray = new JSONArray(Arrays.asList(Controller.viewVehicles(carType, location, city, from, to)));
        return mJSONArray.toString();
        // return "view-vehicles";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/reservation", consumes = "multipart/form-data", produces = "application/json")
    public String reservation(@RequestPart(name="phoneNum", required=false) String phoneNum,
                              @RequestPart(name="name", required=false) String name,
                              @RequestPart(name="address", required=false) String address,
                              @RequestPart(name="city", required=false) String city, // TODO remove
                              @RequestPart(name="license", required=false) String license,
                              @RequestPart(name="vtname", required=false) String vtname, //TODO needs to be vid
                              @RequestPart(name="from", required=false) String from,
                              @RequestPart(name="to", required=false) String to) {


        //todo change this assignment so that we can store ReservationModel
        int confNum = -1; //Controller.makeReservation(phoneNum, name, address, license, vtname, from, to);
        return String.valueOf(confNum);
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/rent", consumes = "multipart/form-data", produces = "application/json")
    public String rent(@RequestPart(name="phoneNum", required=false) String phoneNum,
                       @RequestPart(name="name", required=false) String name,
                       @RequestPart(name="address", required=false) String address,
                       @RequestPart(name="city", required=false) String city,
                       @RequestPart(name="license", required=false) String license,
                       @RequestPart(name="vtname", required=false) String vtname, //TODO change to vid
                       @RequestPart(name="from", required=false) String from,
                       @RequestPart(name="to", required=false) String to) { 

        //TODO add these to params
        String confNum = "";
        String cardName ="";
        String cardNo="";
        String expDate ="";

        // todo (like makeReservation method,) please change the assignment so that it returns the rentConfMassage
        int rid = -1;
        return String.valueOf(rid);
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/return", consumes = "multipart/form-data", produces = "application/json")
    public String returns(@RequestPart(name="rid", required=false) String rid,
                          @RequestPart(name="vtname", required=false) String vtname,
                          @RequestPart(name="city", required=false) String city,
                          @RequestPart(name="time", required=false) String time) {
        //String rid, String returnDateTime, String odometer, String fulltank
        // TODO add these to params
        String retDate ="";
        String odometer ="";
        String fulltank="";

        JSONArray retMessage = new JSONArray(Controller.returnVehicle(rid, retDate, odometer, fulltank));
        return retMessage.toString();

    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/daily-returns", consumes = "multipart/form-data", produces = "application/json")
    public String dailyReturns(@RequestPart(name="date", required=false) String date) {
        JSONArray mJSONArray = new JSONArray(Arrays.asList(Controller.generateReportDailyReturnsAllVehicleInfo(date)));
        return mJSONArray.toString();
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/daily-rentals", consumes = "multipart/form-data", produces = "application/json")
    public String dailyRentals(@RequestPart(name="date", required=false) String date) {
        JSONArray mJSONArray = new JSONArray(Arrays.asList(Controller.generateReportDailyRentalsAllVehicleInfo(date)));
        return mJSONArray.toString();
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/daily-returns-branch", consumes = "multipart/form-data", produces = "application/json")
    public String dailyReturnsBranch(@RequestPart(name="date", required=false) String date,
                                     @RequestPart(name="address", required=false) String address,
                                     @RequestPart(name="city", required=false) String city) {
        JSONArray mJSONArray = new JSONArray(Arrays.asList(Controller.generateReportDailyReturnsAllVehicleInfoOnBranch(date, address, city)));
        return mJSONArray.toString();
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/daily-rentals-branch", consumes = "multipart/form-data", produces = "application/json")
    public String dailyRentalsBranch(@RequestPart(name="date", required=false) String date,
                                     @RequestPart(name="address", required=false) String address,
                                     @RequestPart(name="city", required=false) String city) {
        JSONArray mJSONArray = new JSONArray(Arrays.asList(Controller.generateReportDailyRentalsAllVehicleInfoOnBranch(date, address, city)));
        return mJSONArray.toString();
    }
}







