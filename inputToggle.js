$( document ).ready(function()
{
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

    // hides "return" inputs when you click "rent"
    $("#rent").click(function() {
        $("make-return-fields").hide(); 
        $("#make-rental-fields").show(); 
    })

    // hides "rent" inputs when you click "return"
    $("#return").click(function() {
        $("#make-rental-fields").hide(); 
        $("#make-return-fields").show(); 
    })

    $("#add").click(function() {
        $("#primary-key").show();
        enableInputs()
        disableInputAll()
    })

    $("#delete").click(function() {
        $("#primary-key").show();
        enableInputs()
        disableInputAll()
    })

    $("#update").click(function() {
        $("#primary-key").show();
        enableInputs()
        disableInputAll()
    })

    $("#view").click(function() {
        $("#primary-key").hide();
        $("#all").prop("disabled", false);
    })
})

function disableInputs() {
    $("#rentals").prop("disabled", true);
    $("#reservation").prop("disabled", true);
    $("#vehicles").prop("disabled", true);
    $("#vehicleTypes").prop("disabled", true);
    $("#customer").prop("disabled", true);
    $("#returns").prop("disabled", true);
}

function enableInputs() {
    $("#all").prop("checked", false);
    $("#rentals").prop("disabled", false);
    $("#reservation").prop("disabled", false);
    $("#vehicles").prop("disabled", false);
    $("#vehicleTypes").prop("disabled", false);
    $("#customer").prop("disabled", false);
    $("#returns").prop("disabled", false);
}

function disableInputAll() {
    $("#all").prop("disabled", true);
}