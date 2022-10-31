$(document).ready(function() {
$('#sandbox-container1 .input-group.date').datepicker({
    format: "dd/mm/yyyy",
    weekStart: 1,
    todayBtn: "linked",
    clearBtn: true,
    daysOfWeekDisabled: "0",
    autoclose: true,
    todayHighlight: true,
	endDate: 'today'
    });

$('#sandbox-container2 .input-daterange').datepicker({
    format: "dd/mm/yyyy",
    maxViewMode: 2,
    daysOfWeekDisabled: "0"
    });
       
});