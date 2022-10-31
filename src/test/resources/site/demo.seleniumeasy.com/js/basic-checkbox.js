$('#check1').click(function(){
    if($('#isChkd').val() == "false"){
        $('.cb1-element').prop('checked','checked');
        $(this).val('Uncheck All');
        $('#isChkd').val(true);
    } else if($('#isChkd').val() == "true"){
        $('.cb1-element').removeAttr('checked');
        $(this).val('Check All');     
        $('#isChkd').val(false);
    }
})

$('.cb1-element').change(function(){
    if($('#isChkd').val() == false){
        $('#check1').val('Uncheck All');
        $('#isChkd').val(true);
    }else if($(':checked').length == 4){
        $('#check1').val('Uncheck All');
        $('#isChkd').val(true);    
    }else{
        $('#check1').val('Check All');
        $('#isChkd').val(false);
    }
});