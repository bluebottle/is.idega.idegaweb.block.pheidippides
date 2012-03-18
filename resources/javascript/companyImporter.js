jQuery.noConflict();
var availableRaces = false;

jQuery(document).ready(function() {
	jQuery('a.next').click(function(event) {
		event.preventDefault();
		
		var link = jQuery(this);
		jQuery('input[name="prm_action"]').val(link.attr('rel'));
		
		var form = jQuery('form.adminForm');
		if (form.valid()) {
			form.submit();
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
});