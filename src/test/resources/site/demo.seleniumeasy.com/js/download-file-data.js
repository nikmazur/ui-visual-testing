	$(document).ready(function() {
     $('#create').attr('disabled', 'disabled');
     $('#textbox').keyup(function() {
        if($(this).val() == '') {
           $('#create').attr('disabled','disabled');
        } else {
		$('#create').removeAttr('disabled');
		}
     });
	});
	
	$(document).ready(function() {
		var text_max = 500;
    $('#textarea_feedback').html(text_max + ' characters remaining');
    $('#textbox').keyup(function() {
        var text_length = $('#textbox').val().length;
        var text_remaining = text_max - text_length;
        $('#textarea_feedback').html(text_remaining + ' characters remaining');
    });
	});
	
	(function () {
	var newTextFile = null,
  makeTextFile = function (text) {
    var data = new Blob([text], {type: 'text/plain'});
	
    if (newTextFile !== null) {
      window.URL.revokeObjectURL(newTextFile);
    }

    newTextFile = window.URL.createObjectURL(data);
    return newTextFile;
  };

  var createFile = document.getElementById('create'),
    textbox = document.getElementById('textbox');

  createFile.addEventListener('click', function () {
    var link = document.getElementById('link-to-download');
    link.href = makeTextFile(textbox.value);
    link.style.display = 'block';
  }, false);
})();