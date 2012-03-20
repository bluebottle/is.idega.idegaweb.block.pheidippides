jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('form.userForm').validate();
	
	jQuery.mask.definitions['M']='[01]';	
	jQuery.mask.definitions['D']='[0123]';	
	
	jQuery('input[name="prm_date_of_birth"]').mask('D9.M9.9999');

	jQuery('a.send').click(function(event) {
		event.preventDefault();
		var form = jQuery('form#lostPassword');
		if (form.valid()) {
			form.submit();
		}
	});
});