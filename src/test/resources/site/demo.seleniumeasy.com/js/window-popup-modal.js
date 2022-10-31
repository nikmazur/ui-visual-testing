$(document).ready(function () {
$('.followeasy').click(function (event) {
        var width = 575,
                height = 400,
                left = ($(window).width() - width) / 2,
                top = ($(window).height() - height) / 2,
                url = this.href,
                opts = 'status=1' +
                        ',width=' + width +
                        ',height=' + height +
                        ',top=' + top +
                        ',left=' + left;

        window.open(url, 'twitter', opts);

        return false;
    });
	
$('#followall').click(function(event) {
    var width = 575,
                height = 400,
                left = ($(window).width() - width) / 2,
                top = ($(window).height() - height) / 2,
                url = this.href,
                opts = 'status=1' +
                        ',width=' + width +
                        ',height=' + height +
                        ',top=' + top +
                        ',left=' + left;
		window.open('http://facebook.com/seleniumeasy', 'Facebook', opts);
		window.open('https://twitter.com/intent/follow?screen_name=seleniumeasy', 'Twitter', opts);
		window.open('https://plus.google.com/+Seleniumeasy', 'Google+', opts);

        return false;

});

});