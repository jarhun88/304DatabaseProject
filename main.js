$(document).ready(function () {
    console.log("ready!");
});

$.ajaxSetup({
    success: function(event,request, settings) {
        console.log("success");
        console.log(request);
    }
});