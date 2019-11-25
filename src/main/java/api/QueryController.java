package api;

import controller.Controller;
import model.*;
import org.json.JSONArray;
import org.json.JSONObject;
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
    public String viewVehicles(@RequestPart(name = "carType", required = false) String carType,
                               @RequestPart(name = "location", required = false) String location,
                               @RequestPart(name = "city", required = false) String city,
                               @RequestPart(name = "from", required = false) String from,
                               @RequestPart(name = "to", required = false) String to) {

        JSONArray mJSONArray = new JSONArray(Arrays.asList(Controller.viewVehicles(carType, location, city, from, to)));

        return mJSONArray.toString();
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/reservation", consumes = "multipart/form-data", produces = "application/json")
    public String reservation(@RequestPart(name = "phoneNum", required = false) String phoneNum,
                              @RequestPart(name = "name", required = false) String name,
                              @RequestPart(name = "address", required = false) String address,
                              @RequestPart(name = "license", required = false) String license,
                              @RequestPart(name = "vtname", required = false) String vtname, //vid
                              @RequestPart(name = "from", required = false) String from,
                              @RequestPart(name = "to", required = false) String to) {

        if (phoneNum == null || name == null || address == null || license == null || vtname == null || from == null || to == null)
            return "Please fill in all fields";


        //JSONObject obj = new JSONObject(Controller.makeReservation(phoneNum, name, address, license, vtname, from, to));
        ReservationModel rm = Controller.makeReservation(phoneNum, name, address, license, vtname, from, to);
        if (rm == null) {
            return "Could not make reservation!";
        }
        return rm.toString();
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/rent", consumes = "multipart/form-data", produces = "application/json")
    public String rent(@RequestPart(name="phoneNum", required=false) String phoneNum,
                       @RequestPart(name="name", required=false) String name,
                       @RequestPart(name="address", required=false) String address,
                       @RequestPart(name="license", required=false) String license,
                       @RequestPart(name="vtname", required=false) String vtname, // vid
                       @RequestPart(name="from", required=false) String from,
                       @RequestPart(name="to", required=false) String to,
                       @RequestPart(name="confNum", required=false) String confNum,
                       @RequestPart(name="cardName", required=false) String cardName,
                       @RequestPart(name="cardNo", required=false) String cardNo,
                       @RequestPart(name="expDate", required=false) String expDate) {

        if(phoneNum==null || name ==null || address==null || license == null || vtname ==null || from==null || to==null
         || cardName == null || expDate == null){
            return "Please fill in all fields";
        }

        RentConfirmationMessageModel rm = Controller.rentVehicle(name, address, license, vtname, phoneNum, from,
                to, confNum, cardName, cardNo, expDate);
        if (rm == null) {
            return "Could not make rental!";
        }
        return rm.toString();
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/return", consumes = "multipart/form-data", produces = "application/json")
    public String returns(@RequestPart(name = "rid", required = false) String rid,
                          @RequestPart(name = "retDate", required = false) String retDate,
                          @RequestPart(name = "odometer", required = false) String odometer,
                          @RequestPart(name = "fulltank", required = false) String fulltank) {

        if(rid==null || retDate==null || odometer==null || fulltank==null){
            return "Please fill in all fields";
        }

        ReturnConfirmationMessageModel rcmm = Controller.returnVehicle(rid, retDate, odometer, fulltank);
        //JSONArray retMessage = new JSONArray(Controller.returnVehicle(rid, retDate, odometer, fulltank));
        if (rcmm == null) {
            return "Could not process return!";
        }
        return rcmm.toString();

    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/daily-returns", consumes = "multipart/form-data", produces = "application/json")
    public String dailyReturns(@RequestPart(name = "date", required = false) String date) {

        String[][] temp = Controller.generateReportDailyReturnsAllVehicleInfo(date);
        JSONArray parentJsonArray = new JSONArray();
        // loop through your elements
        for (int i = 0; i < temp.length; i++) {
            JSONArray childJsonArray = new JSONArray(temp[i]);
            parentJsonArray.put(childJsonArray);
        }
        JSONArray mJSONArray = new JSONArray(Arrays.asList(Controller.generateReportDailyReturnsAllVehicleInfo(date)));
        return parentJsonArray.toString();
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/daily-rentals", consumes = "multipart/form-data", produces = "application/json")
    public String dailyRentals(@RequestPart(name = "date", required = false) String date) {

        String[][] temp = Controller.generateReportDailyRentalsAllVehicleInfo(date);
        JSONArray parentJsonArray = new JSONArray();
        // loop through your elements
        for (int i = 0; i < temp.length; i++) {
            JSONArray childJsonArray = new JSONArray(temp[i]);
            parentJsonArray.put(childJsonArray);
        }

        //JSONArray mJSONArray = new JSONArray(Arrays.asList(Controller.generateReportDailyRentalsAllVehicleInfo(date)));
        return parentJsonArray.toString();
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/daily-returns-branch", consumes = "multipart/form-data", produces = "application/json")
    public String dailyReturnsBranch(@RequestPart(name = "date", required = false) String date,
                                     @RequestPart(name = "address", required = false) String address,
                                     @RequestPart(name = "city", required = false) String city) {

        String[][] temp = Controller.generateReportDailyRentalsAllVehicleInfoOnBranch(date, address, city);
        JSONArray parentJsonArray = new JSONArray();
        // loop through your elements
        for (int i = 0; i < temp.length; i++) {
            JSONArray childJsonArray = new JSONArray(temp[i]);
            parentJsonArray.put(childJsonArray);
        }

        // JSONArray mJSONArray = new JSONArray(Arrays.asList(Controller.generateReportDailyReturnsAllVehicleInfoOnBranch(date, address, city)));
        return parentJsonArray.toString();
    }

    @CrossOrigin(origins = {"http://localhost:8081", "http://127.0.0.1:8081", "http://206.87.116.219:8081"})
    @PostMapping(path = "/daily-rentals-branch", consumes = "multipart/form-data", produces = "application/json")
    public String dailyRentalsBranch(@RequestPart(name = "date", required = false) String date,
                                     @RequestPart(name = "address", required = false) String address,
                                     @RequestPart(name = "city", required = false) String city) {

        String[][] temp = Controller.generateReportDailyRentalsAllVehicleInfoOnBranch(date, address, city);
        JSONArray parentJsonArray = new JSONArray();
        // loop through your elements
        for (int i = 0; i < temp.length; i++) {
            JSONArray childJsonArray = new JSONArray(temp[i]);
            parentJsonArray.put(childJsonArray);
        }
        //JSONArray mJSONArray = new JSONArray(Arrays.asList(Controller.generateReportDailyRentalsAllVehicleInfoOnBranch(date, address, city)));
        return parentJsonArray.toString();
    }
}







