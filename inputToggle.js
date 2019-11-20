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

    let tableFn = 0;

    $("#add").click(function() {
        $("#primary-key").show();
        enableInputs()
        disableInputAll()
        $("#rentals").prop("disabled", true);
        $("#reservation").prop("disabled", true);
        $("#returns").prop("disabled", true);
        $("#rentals").prop("checked", false);
        $("#reservation").prop("checked", false);
        $("#returns").prop("checked", false);
        tableFn = 0;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#delete").click(function() {
        $("#primary-key").show();
        enableInputs()
        disableInputAll()
        tableFn = 1;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#update").click(function() {
        $("#primary-key").show();
        enableInputs()
        disableInputAll()
        tableFn = 2;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#view").click(function() {
        $("#primary-key").hide();
        $("#all").prop("disabled", false);
        tableFn = 3;
        switchCustomerInputs(tableFn, tableType);
    })

    let tableType = 0;

    $("#reservation").click(function() {
       tableType = 0;
       switchCustomerInputs(tableFn, tableType);
    })

    $("#rentals").click(function() {
        tableType = 1;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#returns").click(function() {
        tableType = 2;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#vehicles").click(function() {
        tableType = 3;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#vehicleTypes").click(function() {
        tableType = 4;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#customer").click(function() {
        tableType = 5;
        switchCustomerInputs(tableFn, tableType);
    })

    $("#all").click(function() {
        tableType = 6;
        switchCustomerInputs(tableFn, tableType);
    })
     
    
})

function disableInputs() {
    $("#vehicles").prop("disabled", true);
    $("#vehicleTypes").prop("disabled", true);
    $("#customer").prop("disabled", true);
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

function switchCustomerInputs(tableFn, tableType) {
    switch(tableFn) {
        case 0:
            console.log(tableType);
            if (tableType == 3) {
                console.log("hii")
                $("#vehicle").show(); 
                $("#vehicle-type").hide(); 
                $("#customers").hide(); 
            }
            else if (tableType == 4) {
                $("#vehicle").hide(); 
                $("#vehicle-type").show(); 
                $("#customers").hide(); 
            }
            else if (tableType == 5) {
                $("#vehicle").hide(); 
                $("#vehicle-type").hide(); 
                $("#customers").show(); 
            }
            break;
        case 1:
            $("#vehicle").hide(); 
            $("#vehicle-type").hide(); 
            $("#customers").hide(); 
            switch (tableType) {
                case 0:
                    $("#pk-title").text("confNo");
                    break;
                case 1:
                    $("#pk-title").text("rid");
                    break;
                case 2:
                    $("#pk-title").text("rid");
                    break;
                case 3:
                    $("#pk-title").text("vid");
                    break;
                case 4:
                    $("#pk-title").text("vtname");
                    break;
                case 5:
                    $("#pk-title").text("phone number");
                    break;
            }
            console.log(tableType);
            break;
        case 2:
            console.log(tableType);
            break;
        case 3:
            console.log(tableType);
            break;
        case 4:
            console.log(tableType);
            break;
        case 5:
            console.log(tableType);
            break;
        case 6:
            console.log(tableType);
        break;
    }
}