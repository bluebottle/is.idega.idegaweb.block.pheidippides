jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('form.userForm').validate({
		rules: {
			prm_password_repeat: {
		    	equalTo: "#prm_password"
		    }
		}
	});
	
	jQuery('a.store').click(function(event) {
		event.preventDefault();
		var form = jQuery('form#changePassword');
		if (form.valid()) {
			form.submit();
		}
	});
});