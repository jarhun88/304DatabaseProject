package api;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping(path = "/view-vehicles", consumes = "multipart/form-data", produces = "application/json")
    public String viewVehicles(@RequestPart(name="carType", required=false) String carType,
                           @RequestPart(name="location", required=false) String location,
                           @RequestPart(name="city", required=false) String city,
                           @RequestPart(name="from", required=false) String from,
                           @RequestPart(name="to", required=false) String to) {
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







