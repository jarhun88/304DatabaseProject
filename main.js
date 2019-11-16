$( document ).ready(function()
{
    console.log( "ready!" );
});

function customerButton() {
    let viewVehiclesSelected = $("#view-vehicles").is(':checked');
    let body = {};
    // add some stuff to body to send
    if (viewVehiclesSelected) {
        axios.post("/someUrl", body).then((response) => {
            console.log(response)
        }).catch((error) => {
            console.log(error)
        })
    }
    else {
        axios.post("/someUrl", body).then((response) => {
            console.log(response)
        }).catch((error) => {
            console.log(error)
        })
    }
    console.log(viewVehiclesSelected)
}

function clerkButton() {
    let rentingVehiclesSelected = $("#rent").is(':checked');
    let body = {};
    // add some stuff to body to send
    if (rentingVehiclesSelected) {
        axios.post("/someUrl", body).then((response) => {
            console.log(response)
        }).catch((error) => {
            console.log(error)
        })
    }
    else {
        axios.post("/someUrl", body).then((response) => {
            console.log(response)
        }).catch((error) => {
            console.log(error)
        })
    }
    console.log(rentingVehiclesSelected);
}

function generateButton() {
    let body = {};
    // add some stuff to body to send
    let dailyReturnsSelected = $("#daily-returns").is(':checked');
    if (dailyReturnsSelected) {
        axios.post("/someUrl", body).then((response) => {
            console.log(response)
        })
    }
    else {
        axios.post("/someUrl", body).then((response) => {
            console.log(response)
        })
    }
    console.log(dailyReturnsSelected)
}