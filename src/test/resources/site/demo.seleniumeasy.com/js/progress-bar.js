( function( $ ){
      $(document).ready(function() {
          $( '.circle' ).each(function() {
      	$(this).progressCircle({
      			nPercent        : 0,
      			showPercentText : true,
      			thickness       : 10,
      			circleSize      : 150
      	});
      });
      $("#cricle-btn").click(function() {
      var speed = $(this).data("size");
      var speed = parseInt(speed)*10;
      var speed = parseInt(speed)/1024;
      var speed = parseInt(speed);
      $(this).addClass("active");
      		circle('#circle',"#cricleinput",speed);
      })
      
      function circle(circleID,cricleinput, speed) {
      setTimeout(function() {
      	$(circleID).each(function() {
      var ci = $(cricleinput).val();
      var ci = parseInt(ci)+1;
      if(ci<100) {
      	$(this).progressCircle({
      			nPercent        : ci,
      			showPercentText : true,
      			thickness       : 10,
      			circleSize      : 150
      	});
      }
      else
      if(ci==100) {
      	$(this).addClass("end").progressCircle({
      			nPercent        : 100,
      			showPercentText : true,
      			thickness       : 9,
      			circleSize      : 150
      	});
      }	
      else
      if(ci==101) {
      	$(this).progressCircle({
      			nPercent        : 100,
      			showPercentText : true,
      			thickness       : 8,
      			circleSize      : 150
      	});
      }	
      else
      if(ci==102) {
      	$(this).progressCircle({
      			nPercent        : 100,
      			showPercentText : true,
      			thickness       : 7,
      			circleSize      : 150
      	});
      }	
      else
      if(ci==103) {
      	$(this).progressCircle({
      			nPercent        : 100,
      			showPercentText : true,
      			thickness       : 6,
      			circleSize      : 150
      	});
      }	
      else
      if(ci==104) {
      	$(this).progressCircle({
      			nPercent        : 100,
      			showPercentText : true,
      			thickness       : 5,
      			circleSize      : 150
      	});
      }	
      else
      if(ci==105) {
      	$(this).progressCircle({
      			nPercent        : 100,
      			showPercentText : true,
      			thickness       : 4,
      			circleSize      : 150
      	});
      }
      	$(cricleinput).val(ci);
      });
      $( circleID ).promise().done(function() {
      var ci = $("#cricleinput").val();
      if(ci<105) {
      		circle(circleID,cricleinput,speed);
      }
      else {
      var nextid = $(this).data("next");
      	if(nextid != 'none') {
      		$(cricleinput).val(0);
      			circle(nextid,cricleinput,speed);
      	}
      	else {
      	$(".end").addClass("complate");
      	}
      }
      })
      },speed);
      }	
      });
})( jQuery );    