package api;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping(path = "/view-vehicles", consumes = "multipart/form-data", produces = "application/json")
    public String viewVehicles(@RequestPart("carType") String carType,
                           @RequestPart("location") String location,
                           @RequestPart("city") String city,
                           @RequestPart("from") String from,
                           @RequestPart("to") String to) {

        return "hello";
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping(path = "/reservation", consumes = "multipart/form-data", produces = "application/json")
    public String reservation() {

        return "hello";
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping(path = "/rent", consumes = "multipart/form-data", produces = "application/json")
    public String rent() {

        return "hello";
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping(path = "/return", consumes = "multipart/form-data", produces = "application/json")
    public String returns() {

        return "hello";
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping(path = "/daily-returns", consumes = "multipart/form-data", produces = "application/json")
    public String dailyReturns() {

        return "hello";
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping(path = "/daily-rentals", consumes = "multipart/form-data", produces = "application/json")
    public String dailyRentals() {

        return "hello";
    }


}







