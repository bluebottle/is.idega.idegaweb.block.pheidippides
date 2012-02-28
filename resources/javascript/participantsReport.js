jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('.dwrEnabled').change(function() {
		var eventPK = jQuery('select[name="prm_event_pk"]').val();
		var year = jQuery('select[name="prm_year"]').val();
		var language = jQuery('input[name="prm_language"]').val();
		var race = jQuery('select[name="prm_race_pk"]');
		var value = race.val();
		
		PheidippidesService.getLocalizedRaces(eventPK, year, language, true, {
			callback: function(races) {
				dwr.util.removeAllOptions(race.attr('id'));
				
				for (var i = 0; i < races.length; i++) {
					race.append('<option value="' + races[i].id + '">' + races[i].value + '</option>')
				}
				
				dwr.util.setValue(race.attr('id'), value);
			}
		});
	}).trigger('change');
	
	jQuery.mask.definitions['Y']='[12]';	
	jQuery('input.maskedYear').mask("Y999");
});