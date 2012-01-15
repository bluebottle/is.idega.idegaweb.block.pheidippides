jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('form.registrationForm').validate({
		rules: {
			prm_email_repeat: {
		    	equalTo: "#prm_email"
		    },
		    prm_personal_id: {
		    	required: "#prm_no_personal_id:unchecked"
		    }
		}
	});
	
	jQuery('a.next').click(function(event) {
		event.preventDefault();
		
		var link = jQuery(this);
		jQuery('input[name="prm_action"]').val(link.attr('rel'));
		
		var form = jQuery('form.registrationForm');
		if (form.valid()) {
			form.submit();
		}
	});

	jQuery('a.back').click(function(event) {
		event.preventDefault();
		
		var link = jQuery(this);
		jQuery('input[name="prm_action"]').val(link.attr('rel'));
		
		var form = jQuery('form.registrationForm');
		form.validate({
			onsubmit: false
		});
		form.submit();
	});
});