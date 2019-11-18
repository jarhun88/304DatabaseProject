$( document ).ready(function()
{
    const localURL = "http://localhost:8080";

    console.log( "ready!" );
    $("#make-reservation-fields").hide(); 
    $("#make-return-fields").hide();

    // hides "reservation" inputs when you click "view-vehicles"
    $("#view-vehicles").click(function() {
        $("#make-reservation-fields").hide(); 
        $("#view-vehicles-fields").show(); 
    })

    // hides "view-vehicle" inputs when you click "reservation"
    $("#make-reservation").click(function() {
        $("#view-vehicles-fields").hide(); 
        $("#make-reservation-fields").show(); 
    })

    $("#rent").click(function() {
        $("make-return-fields").hide(); 
        $("#make-rental-fields").show(); 
    })

    $("#return").click(function() {
        $("#make-rental-fields").hide(); 
        $("#make-return-fields").show(); 
    })

    $("#branch").click(function() {
        let isBranchSelected = $("#branch").is(':checked');
        if (isBranchSelected){
            $("#branch-fields").show(); 
        } 
        else {
            $("#branch-fields").hide(); 
        }
    })

    $("#customer-button").click(function() {
        let viewVehiclesSelected = $("#view-vehicles").is(':checked');
        let body;
        let config = {
            headers: {'Access-Control-Allow-Origin': '*'}
        };
        if (viewVehiclesSelected) {
            body = viewVehiclesBody(body)
            axios.post(localURL + "/view-vehicles", body, config).then((response) => {
                console.log(response)
            }).catch((error) => {
                console.log(error)
            })
        }
        else {
            body = reservationBody(body)
            axios.post(localURL + "/reservation", body, config).then((response) => {
                console.log(response)
            }).catch((error) => {
                console.log(error)
            })
        }

    });

    $("#rent-reserve-button").click(function() {
        console.log("rent-reserve");
        let body;
        let rentVehicleSelected = $("#rent").is(':checked');
        if (rentVehicleSelected) {
            body = rentBody();
            axios.post(localURL + "/rent", body).then((response) => {
                console.log(response)
            }).catch((error) => {
                console.log(error)
            })  
        }
        else {
            body = returnBody();
            axios.post(localURL + "/return", body).then((response) => {
                console.log(response)
            }).catch((error) => {
                console.log(error)
            })  
        }      
        console.log(body);
    });

    $("#generate-button").click(function() {
        let body = {};
        let isBranchSelected = $("#branch").is(':checked');
        let isReturnsSelected = $("#daily-returns").is(':checked');
        body["date"] = $("#g-date").val();
        if (isBranchSelected) {
            body = branchBody(body);
        }
        console.log(body);
        if (isReturnsSelected) {
            axios.post(localURL + "/daily-returns", body).then((response) => {
                console.log(response)
            }).catch((error) => {
                console.log(error)
            })  
        }
        else {
            axios.post(localURL +  "/daily-rentals", body).then((response) => {
                console.log(response)
            }).catch((error) => {
                console.log(error)
            })  
        }
    })
})

function branchBody(body) {
    body["address"] = $("#g-address").val()
    body["city"] = $("#g-city").val()
    return body;
}



function returnBody() {
    let body = {};
    let rid = $("#return-rid").val();
    let vtname = $("#return-vtname").val();
    let city = $("#return-city").val();
    let time = $("#return-time").val();
    body["rid"] = rid;
    body["vtname"] = vtname;
    body["city"] = city;
    body["time"] = time;
    return body;
}

function rentBody() {
    let body = {};
    let phoneNum = $("#rent-phone-num").val();
    let name = $("#rent-name").val();
    let address = $("#rent-address").val();
    let license = $("#rent-driver-license").val();
    let vtname = $("#rent-vtname").val();
    let city = $("#rent-city").val();
    let from = $("#rent-from").val();
    let to = $("#rent-to").val();
    body["phoneNum"] = phoneNum;
    body["name"] = name;
    body["address"] = address;
    body["city"] = city;
    body["license"] = license;
    body["vtname"] = vtname;
    body["from"] = from;
    body["to"] = to;
    return body;
}

function viewVehiclesBody() {
    let body = new FormData();
    let carType = $("#vv-car-type").val();
    let location = $("#vv-location").val();
    let city = $("#vv-city").val();
    let from = $("#vv-from").val();
    let to = $("#vv-to").val();
    body.set("carType", carType);
    body.set("location", location);
    body.set("city", city);
    body.set("from", from);
    body.set("to", to);
    // body["carType"] = carType;
    // body["location"] = location;
    // body["city"] = city;
    // body["from"] = from;
    // body["to"] = to;
    console.log(body);
    return body;
}

function reservationBody() {
    let body = {};
    let phoneNum = $("#mr-phone-num").val();
    let name = $("#mr-name").val();
    let address = $("#mr-address").val();
    let city = $("#mr-city").val();
    let license = $("#mr-driver-license").val();
    let vtname = $("#mr-vtname").val();
    let from = $("#mr-from").val();
    let to = $("#mr-to").val();
    body["phoneNum"] = phoneNum;
    body["name"] = name;
    body["address"] = address;
    body["city"] = city;
    body["license"] = license;
    body["vtname"] = vtname;
    body["from"] = from;
    body["to"] = to;
    return body;
}
