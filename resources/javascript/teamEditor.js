jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('form.userForm').validate();

	jQuery('input[name="prm_personalId"]').keyup(function() {
		var input = jQuery(this);
		var value = input.val();
		var parent = input.parent().parent();
		var racePK = jQuery('input[name="prm_race_pk"]').val();
		
		var empty = true;
		if (value.length == 10) {
			PheidippidesService.getRaceRegistration(value, racePK, {
				callback: function(participant) {
					if (participant != null) {
						jQuery('span.fullName', parent).text(participant.fullName);
						jQuery('span.email', parent).text(participant.email);
						jQuery('input[name="prm_other_registration_pk"]', parent).val(participant.registrationID);
						empty = false;
					}
				}
			});
		}
		if (empty) {
			jQuery('span.fullName', parent).text('');
			jQuery('span.email', parent).text('');
			jQuery('input[name="prm_other_registration_pk"]', parent).val('');
		}
	});
	
	jQuery('input[name="prm_other_registration_pk"]').keyup(
		function() {
			var input = jQuery(this);
			var value = input.val();
			var parent = input.parent().parent();
			var racePK = jQuery('input[name="prm_race_pk"]').val();
			
			PheidippidesService.getRaceRegistration(value, racePK, {
				callback: function(participant) {
					if (participant != null) {
						jQuery('span.fullName', parent).text(participant.fullName);
						jQuery('span.email', parent).text(participant.email);
					}
					else {
						jQuery('span.fullName', parent).text('');
						jQuery('span.email', parent).text('');
					}
				}
			});

		}
	);

	jQuery('a.store').click(function(event) {
		event.preventDefault();
		var form = jQuery('form#teamEditor');
		if (form.valid()) {
			form.submit();
		}
	});
});