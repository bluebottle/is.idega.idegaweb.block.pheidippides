jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery.validator.addMethod("validUser", function( value, element ) {
		var valid = true;
		if (value.length == 10) {
			PheidippidesService.isValidPersonalID(value, {
				callback: function(validUser) {
					valid = validUser;
				}
			});
		}
		else {
			valid = false;
		}
		return valid;
	}, "No participant found with entered personal ID.");	
	
	jQuery('form.registrationForm').validate({
		onkeyup: false,
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
		    }
		}
	});
	
	jQuery('select[name="prm_race_pk"]').change(function() {
		var racePK = jQuery(this).val();
		var shirt = jQuery('select[name="prm_shirt_size"]');
		
		PheidippidesService.getShirts(racePK, {
			callback: function(shirtSizes) {
				dwr.util.removeAllOptions(shirt.attr('id'));
				dwr.util.addOptions(
					shirt.attr('id'),
					shirtSizes,
					function(raceShirt) {
						return raceShirt.size.id;
					},
					function(raceShirt) {
						return raceShirt.size.size + ' - ' + raceShirt.size.gender;
					}
				);
			}
		});
	}).trigger('change');
	
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