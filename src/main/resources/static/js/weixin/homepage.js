<<<<<<< HEAD

	$(document).ready(function () {
		$('#buttom').click(function() {
			var url = $('#urlblock').val();
			location.href=url;
		});
=======

	$(document).ready(function () {
		var isFail = $('#fail').val();

	    $('#desc_fail').attr('style','display:none');
		if(isFail=="true"){
			$('#desc_normal').attr('style','display:none');
		    $('#desc_fail').attr('style','display:block');
		}
		$('#buttom').click(function() {
			var url = $('#urlblock').val();
			location.href=url;
		});
>>>>>>> 32955b6a2b1f4430ffe18822b5a9203e1b3606eb
	});