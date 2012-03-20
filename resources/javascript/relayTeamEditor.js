jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('form.userForm').validate();
	
	jQuery('[name="prm_email"]').each(function(){
	     jQuery(this).rules("add", {
	       email: true
	     });   
	   });
	
	jQuery.mask.definitions['M']='[01]';	
	jQuery.mask.definitions['D']='[0123]';	
	
	jQuery('input[name="prm_date_of_birth"]').mask('D9.M9.9999');

	jQuery('input[name="prm_personalId"]').keyup(function() {
		var input = jQuery(this);
		var value = input.val();
		var parent = input.parent().parent();
		
		var empty = true;
		if (value.length == 10) {
			PheidippidesService.getParticipant(value, {
				callback: function(participant) {
					if (participant != null) {
						jQuery('span.fullName', parent).text(participant.fullName);
						jQuery('input[name="prm_name"]', parent).val(participant.fullName);
						jQuery('input[name="prm_email"]', parent).val(participant.email);
						empty = false;
					}
				}
			});
		}
		if (empty) {
			jQuery('select[name="prm_shirt_size"]', parent).val('');
			jQuery('span.fullName', parent).text('');
			jQuery('input[name="prm_name"]', parent).val('');
			jQuery('input[name="prm_email"]', parent).val('');
		}
	});
	
	jQuery('a.store').click(function(event) {
		event.preventDefault();
		var form = jQuery('form#relayTeamEditor');
		if (form.valid()) {
			form.submit();
		}
	});
});