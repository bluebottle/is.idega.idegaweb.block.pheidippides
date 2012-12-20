jQuery.noConflict();
var validUser = null;

jQuery(document).ready(function() {
	jQuery.validator.addMethod("validUser", function( value, element ) {
		if (value.length == 0) {
			return true;
		}
		else {
			return validUser == value;
		}
	}, "No participant found with entered personal ID.");
	
	jQuery('input[name="prm_personal_id"], input[name="prm_email"], input[name="prm_email_repeat"]').keypress(function(e) {
        if(e.which == 13) {
            jQuery(this).blur();
            jQuery('a.next').click();
        }
    });
	
	jQuery('form.registrationForm').validate({
		onkeyup: false,
		onfocusout: false,
		
		rules: {
			prm_email_repeat: {
		    	equalTo: "#prm_email"
		    },
		    prm_personal_id: {
		    	required: true,
		    	validUser: true
		    }
		}
	});
	
	jQuery('input[name="prm_personal_id"]').keyup(function() {
		var input = jQuery(this);
		var value = input.val();

		if (value.length == 10) {
			PheidippidesService.getParticipant(value, {
				callback: function(participant) {
					if (participant != null) {
						validUser = participant.personalId;
					}
				}
			});
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

	jQuery('a.remove').click(function(event) {
		event.preventDefault();
		
		var link = jQuery(this);
		jQuery('input[name="prm_remove"]').val(link.attr('rel'));
		
		var form = jQuery('form.registrationForm');
		form.validate().cancelSubmit = true;
		form.submit();
	});

	jQuery('a.back').click(function(event) {
		var link = jQuery(this);
		var rel = link.attr('rel');
		
		if (rel) {
			event.preventDefault();
			
			jQuery('input[name="prm_action"]').val(rel);
			
			var form = jQuery('form.registrationForm');
			form.validate().cancelSubmit = true;
			form.submit();
		}
	});
});