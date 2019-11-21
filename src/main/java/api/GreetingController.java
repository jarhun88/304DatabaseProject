package api;

import controller.Controller;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class GreetingController {

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/view-vehicles", consumes = "multipart/form-data", produces = "application/json")
    public String viewVehicles(@RequestPart(name="carType", required=false) String carType,
                           @RequestPart(name="location", required=false) String location,
                           @RequestPart(name="city", required=false) String city,
                           @RequestPart(name="from", required=false) String from,
                           @RequestPart(name="to", required=false) String to) {

//        JSONArray mJSONArray = new JSONArray(Arrays.asList(Controller.viewVehicles(carType, location, city, from, to)));
//        return mJSONArray.toString();
        return "view-vehicles";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/reservation", consumes = "multipart/form-data", produces = "application/json")
    public String reservation(@RequestPart(name="phoneNum", required=false) String phoneNum,
                              @RequestPart(name="name", required=false) String name,
                              @RequestPart(name="address", required=false) String address,
                              @RequestPart(name="city", required=false) String city,
                              @RequestPart(name="license", required=false) String license,
                              @RequestPart(name="vtname", required=false) String vtname,
                              @RequestPart(name="from", required=false) String from,
                              @RequestPart(name="to", required=false) String to) {
        return "reservation";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/rent", consumes = "multipart/form-data", produces = "application/json")
    public String rent(@RequestPart(name="phoneNum", required=false) String phoneNum,
                       @RequestPart(name="name", required=false) String name,
                       @RequestPart(name="address", required=false) String address,
                       @RequestPart(name="city", required=false) String city,
                       @RequestPart(name="license", required=false) String license,
                       @RequestPart(name="vtname", required=false) String vtname,
                       @RequestPart(name="from", required=false) String from,
                       @RequestPart(name="to", required=false) String to) {
        return "rent";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/return", consumes = "multipart/form-data", produces = "application/json")
    public String returns(@RequestPart(name="rid", required=false) String rid,
                          @RequestPart(name="vtname", required=false) String vtname,
                          @RequestPart(name="city", required=false) String city,
                          @RequestPart(name="time", required=false) String time) {
        return "return";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/daily-returns", consumes = "multipart/form-data", produces = "application/json")
    public String dailyReturns(@RequestPart(name="date", required=false) String date) {
        return "dailyreturns";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/daily-rentals", consumes = "multipart/form-data", produces = "application/json")
    public String dailyRentals(@RequestPart(name="date", required=false) String date) {
        return "dailyrentals";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/daily-returns-branch", consumes = "multipart/form-data", produces = "application/json")
    public String dailyReturnsBranch(@RequestPart(name="date", required=false) String date,
                                     @RequestPart(name="address", required=false) String address,
                                     @RequestPart(name="city", required=false) String city) {
        return "dailyReturnsBranch";
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/daily-rentals-branch", consumes = "multipart/form-data", produces = "application/json")
    public String dailyRentalsBranch(@RequestPart(name="date", required=false) String date,
                                     @RequestPart(name="address", required=false) String address,
                                     @RequestPart(name="city", required=false) String city) {
        return "dailyRentalsBranch";
    }
}







