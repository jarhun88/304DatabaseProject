$( document ).ready(function()
{
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

    $("#customer-button").click(function() {
        let body;
        let viewVehiclesSelected = $("#view-vehicles").is(':checked');
        if (viewVehiclesSelected) {
            body = viewVehiclesBody()
        }
        else {
            body = reservationBody()
        }
        axios.post("/someUrl", body).then((response) => {
            console.log(response)
        }).catch((error) => {
            console.log(error)
        })
    });

    $("#rent-reserve-button").click(function() {
        console.log("rent-reserve");
        let body;
        let rentVehicleSelected = $("#rent").is(':checked');
        if (rentVehicleSelected) {
            body = rentBody();
        }
        else {
            body = returnBody();
        }      
        axios.post("/someUrl", body).then((response) => {
            console.log(response)
        }).catch((error) => {
            console.log(error)
        })  
    });
    

});

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
    let name = $("#rent-phone-num").val();
    let address = $("#rent-phone-num").val();
    let license = $("#rent-phone-num").val();
    let vtname = $("#rent-phone-num").val();
    let city = $("#rent-phone-num").val();
    let from = $("#rent-phone-num").val();
    let to = $("#rent-phone-num").val();
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
    let body = {};
    let carType = $("#vv-car-type").val();
    let location = $("#vv-location").val();
    let city = $("#vv-city").val();
    let from = $("#vv-from").val();
    let to = $("#vv-to").val();
    body["carType"] = carType;
    body["location"] = location;
    body["city"] = city;
    body["from"] = from;
    body["to"] = to;
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

// function customerButton() {
//     let viewVehiclesSelected = $("#view-vehicles").is(':checked');
//     let body = {};
//     // add some stuff to body to send
//     if (viewVehiclesSelected) {
//         axios.post("/someUrl", body).then((response) => {
//             console.log(response)
//         }).catch((error) => {
//             console.log(error)
//         })
//     }
//     else {
//         axios.post("/someUrl", body).then((response) => {
//             console.log(response)
//         }).catch((error) => {
//             console.log(error)
//         })
//     }
//     console.log(viewVehiclesSelected)
// }

// function clerkButton() {
//     let rentingVehiclesSelected = $("#rent").is(':checked');
//     let body = {};
//     // add some stuff to body to send
//     if (rentingVehiclesSelected) {
//         axios.post("/someUrl", body).then((response) => {
//             console.log(response)
//         }).catch((error) => {
//             console.log(error)
//         })
//     }
//     else {
//         axios.post("/someUrl", body).then((response) => {
//             console.log(response)
//         }).catch((error) => {
//             console.log(error)
//         })
//     }
//     console.log(rentingVehiclesSelected);
// }

// function generateButton() {
//     let body = {};
//     // add some stuff to body to send
//     let dailyReturnsSelected = $("#daily-returns").is(':checked');
//     if (dailyReturnsSelected) {
//         axios.post("/someUrl", body).then((response) => {
//             console.log(response)
//         })
//     }
//     else {
//         axios.post("/someUrl", body).then((response) => {
//             console.log(response)
//         })
//     }
//     console.log(dailyReturnsSelected)
// }