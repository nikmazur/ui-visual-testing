$(document).ready(function() 
 {
 $('.board a[title]').tooltip();
  $('.nav-tabs li').click(function () {
     var selectedLi = this;
     $('.nav-tabs').find('li:not(:last):not(:first)').each(function(){
       $(this).find('i').removeClass('down');
       if(selectedLi == this) $(this).find('i').addClass('down');
     });
 });
 
$('#home').on( "click", '#btn_basic_example', function(e){
   $('#basic_example').trigger('click');
});

$('#basic').on( "click", '#btn_inter_example', function(e){
   $('#inter_example').trigger('click');
});
$('#intermediate').on( "click", '#btn_advanced_example', function(e){
   $('#advanced_example').trigger('click');
});
$('#advanced').on( "click", '#btn_done_example', function(e){
   $('#done_example').trigger('click');
});
	
 });