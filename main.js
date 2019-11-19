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
        let responseText;
        let config = {
            headers: {'Access-Control-Allow-Origin': '*'}
        };
        if (viewVehiclesSelected) {
            body = viewVehiclesBody(body)
            axios.post(localURL + "/view-vehicles", body, config).then((response) => {
                responseText = response.data;
                console.log(responseText)
                document.getElementById("customer-output").innerHTML = responseText;
            }).catch((error) => {
                responseText = error.data;
                console.log(responseText)
                document.getElementById("customer-output").innerHTML = responseText;
            })
        }
        else {
            body = reservationBody(body)
            axios.post(localURL + "/reservation", body, config).then((response) => {
                responseText = response.data;
                console.log(responseText)
                document.getElementById("customer-output").innerHTML = responseText;
            }).catch((error) => {
                responseText = error.data;
                document.getElementById("customer-output").innerHTML = responseText;

            })
        }
    });

    $("#rent-reserve-button").click(function() {
        let body;
        let responseText;
        let config = {
            headers: {'Access-Control-Allow-Origin': '*'}
        };
        let rentVehicleSelected = $("#rent").is(':checked');
        if (rentVehicleSelected) {
            body = rentBody();
            axios.post(localURL + "/rent", body, config).then((response) => {
                responseText = response.data;
                console.log(responseText)
                document.getElementById("clerk-output").innerHTML = responseText;
            }).catch((error) => {
                responseText = error.data;
                console.log(responseText)
                document.getElementById("clerk-output").innerHTML = responseText;
            })  
        }
        else {
            body = returnBody();
            axios.post(localURL + "/return", body, config).then((response) => {
                responseText = response.data;
                console.log(responseText)
                document.getElementById("clerk-output").innerHTML = responseText;
            }).catch((error) => {
                responseText = error.data;
                console.log(responseText)
                document.getElementById("clerk-output").innerHTML = responseText;
            })  
        }      
        console.log(body);
    });

    $("#generate-button").click(function() {
        let body = new FormData();
        let responseText;
        let isBranchSelected = $("#branch").is(':checked');
        let isReturnsSelected = $("#daily-returns").is(':checked');
        date = $("#g-date").val();
        body.set("date", date);
        
        console.log(body);
        if (isReturnsSelected) {
            if (isBranchSelected) {
                body = branchBody(body);
                axios.post(localURL + "/daily-returns-branch", body).then((response) => {
                    responseText = response.data;
                    console.log(responseText)
                    document.getElementById("generate-output").innerHTML = responseText;
                }).catch((error) => {
                    responseText = error.data;
                    console.log(responseText)
                    document.getElementById("generate-output").innerHTML = responseText;
                })  
            }
            else {
                axios.post(localURL + "/daily-returns", body).then((response) => {
                    responseText = response.data;
                    console.log(responseText)
                    document.getElementById("generate-output").innerHTML = responseText;
                }).catch((error) => {
                    responseText = error.data;
                    console.log(responseText)
                    document.getElementById("generate-output").innerHTML = responseText;
                })  
            }   
        }
        else {
            if (isBranchSelected) {
                body = branchBody(body);
                axios.post(localURL + "/daily-rentals-branch", body).then((response) => {
                    responseText = response.data;
                    console.log(responseText)
                    document.getElementById("generate-output").innerHTML = responseText;
                }).catch((error) => {
                    responseText = error.data;
                    console.log(responseText)
                    document.getElementById("generate-output").innerHTML = responseText;
                })  
            }
            else {
                axios.post(localURL +  "/daily-rentals", body).then((response) => {
                    responseText = response.data;
                    console.log(responseText)
                    document.getElementById("generate-output").innerHTML = responseText;
                }).catch((error) => {
                    responseText = error.data;
                    console.log(responseText)
                    document.getElementById("generate-output").innerHTML = responseText;
                })  
            }
        }
    })
})

function branchBody(body) {
    address = $("#g-address").val()
    city = $("#g-city").val()
    body.set("address", address);
    body.set("city", city);
    return body;
}

function returnBody() {
    let body = new FormData()
    let rid = $("#return-rid").val();
    let vtname = $("#return-vtname").val();
    let city = $("#return-city").val();
    let time = $("#return-time").val();
    body.set("rid", rid);
    body.set("vtname", vtname);
    body.set("city", city);
    body.set("time", time);
    return body;
}

function rentBody() {
    let body = new FormData();
    let phoneNum = $("#rent-phone-num").val();
    let name = $("#rent-name").val();
    let address = $("#rent-address").val();
    let license = $("#rent-driver-license").val();
    let vtname = $("#rent-vtname").val();
    let city = $("#rent-city").val();
    let from = $("#rent-from").val();
    let to = $("#rent-to").val();
    body.set("phoneNum", phoneNum);
    body.set("name", name);
    body.set("address", address);
    body.set("city", city);
    body.set("license", license);
    body.set("vtname", vtname);
    body.set("from", from);
    body.set("to", to);
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
    console.log(body);
    return body;
}

function reservationBody() {
    let body = new FormData();
    let phoneNum = $("#mr-phone-num").val();
    let name = $("#mr-name").val();
    let address = $("#mr-address").val();
    let city = $("#mr-city").val();
    let license = $("#mr-driver-license").val();
    let vtname = $("#mr-vtname").val();
    let from = $("#mr-from").val();
    let to = $("#mr-to").val();
    body.set("phoneNum", phoneNum);
    body.set("name", name);
    body.set("address", address);
    body.set("city", city);
    body.set("license", license);
    body.set("vtname", vtname);
    body.set("from", from);
    body.set("to", to);
    return body;
}
