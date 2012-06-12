jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('form.userForm').validate();

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
	
	jQuery('a.store').live('click', function(event) {
		event.preventDefault();
		var form = jQuery('form#participantDistanceChanger');
		if (form.valid()) {
			form.submit();
		}
	});
});