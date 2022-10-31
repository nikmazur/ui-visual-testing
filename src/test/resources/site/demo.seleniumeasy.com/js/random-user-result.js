$('#save').click(function () {
    // add loading image to div
    $('#loading').html('<img src="http://seleniumeasy.com/test/img/loader-image.gif"> loading...');
	
	 // run ajax request
    $.ajax({
        type: "GET",
        dataType: "json",
		url: 'https://api.randomuser.me/?nat=us',
        success: function (data) {
            setTimeout(function () {
                $('#loading').html('<img src="' + data.results[0].picture.large + '"><br><br/>' + 
				"First Name : " +data.results[0].name.first + '<br/><br/>' +
				"Last Name : " +data.results[0].name.last);
            }, 1000);
        }
    }); 
});