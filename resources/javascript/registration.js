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
	
	jQuery('form.registrationForm').validate({
		onkeyup: false,
		onfocusout: false,
		
		rules: {
			prm_email_repeat: {
		    	equalTo: "#prm_email"
		    },
		    prm_personal_id: {
		    	required: "#prm_no_personal_id:unchecked",
		    	validUser: true
		    },
		    prm_charity: {
		    	required: "#prm_use_charity:checked"
		    },
		    prm_best_marathon_time: {
		    	required: "#prm_has_not_run_marathon:unchecked"
		    },
		    prm_best_marathon_year: {
		    	required: "#prm_has_not_run_marathon:unchecked"
		    },
		    prm_best_ultra_marathon_time: {
		    	required: "#prm_has_not_run_ultra_marathon:unchecked"
		    },
		    prm_best_ultra_marathon_year: {
		    	required: "#prm_has_not_run_ultra_marathon:unchecked"
		    }
		}
	});
	
	jQuery('input.bestTime').mask("99:99");
	jQuery('input.bestYear').mask("9999");

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
	
	jQuery('input[name="prm_personalId"]').keyup(function() {
		var input = jQuery(this);
		var value = input.val();
		var parent = input.parent().parent();
		
		var empty = true;
		if (value.length == 10) {
			PheidippidesService.getParticipant(value, {
				callback: function(participant) {
					if (participant != null) {
						jQuery('input[name="prm_name"]', parent).val(participant.fullName);
						jQuery('input[name="prm_email"]', parent).val(participant.email);
						empty = false;
					}
				}
			});
		}
		if (empty) {
			jQuery('input[name="prm_name"]', parent).val('');
			jQuery('input[name="prm_email"]', parent).val('');
		}
	});
	
	jQuery('select[name="prm_race_pk"]').change(function() {
		var racePK = jQuery(this).val();
		var language = jQuery('input[name="prm_language"]').val();
		var shirt = jQuery('select[name="prm_shirt_size"]');
		var value = shirt.val();
		
		PheidippidesService.getLocalizedShirts(racePK, language, {
			callback: function(shirts) {
				dwr.util.removeAllOptions(shirt.attr('id'));
				
				for (var i = 0; i < shirts.length; i++) {
					shirt.append('<option value="' + shirts[i].id + '">' + shirts[i].value + '</option>')
				}
				
				if (value != null) {
					dwr.util.setValue(shirt.attr('id'), value);
				}
			}
		});
	});
	
	jQuery('input.extraCheckbox').click(function() {
		var input = jQuery(this);
		var parent = input.parents('fieldset.formSection');
		
		if (input.is(':checked')) {
			jQuery('input:text', parent).attr('disabled', 'disabled');
		}
		else {
			jQuery('input:text', parent).removeAttr('disabled');
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
		form.validate().cancelSubmit = true;
		form.submit();
	});
});