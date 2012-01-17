jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery("table.adminTable").tablesorter({ 
        headers: { 
            8: { 
                sorter: false 
            }, 
            9: { 
                sorter: false 
            } 
        } 
	}); 
	
	jQuery('.dwrEnabled').change(function() {
		var eventPK = jQuery('select[name="prm_event_pk"]').val();
		var year = jQuery('select[name="prm_year"]').val();
		var language = jQuery('input[name="prm_language"]').val();
		var race = jQuery('select[name="prm_race_pk"]');
		var value = race.val();
		
		PheidippidesService.getRaces(eventPK, year, {
			callback: function(races) {
				dwr.util.removeAllOptions(race.attr('id'));
				
				for (var i = 0; i < races.length; i++) {
					PheidippidesService.getLocalizedRaceName(races[i], language, {
						callback: function(property) {
							shirt.append('<option value="' + property.id + '">' + property.value + '</option>');
						}
					});
				}
				dwr.util.setValue(race.attr('id'), value);
			}
		});
	}).trigger('change');
	
	jQuery('a.addItem, a.editItem').fancybox({
		'onComplete': function() {
			jQuery('#registrationEditorForm').validate();
		}
	});
	
	jQuery('a.deleteItem').click(function() {
		var link = jQuery(this);
		var alert = link.attr('rel');
		
		return confirm(alert);
	});
	
	jQuery('a.close').live('click', function(event) {
		event.preventDefault();
		jQuery.fancybox.close();
	});
	
	jQuery('a.store').live('click', function(event) {
		event.preventDefault();
		var form = jQuery('form#registrationEditorForm');
		if (form.valid()) {
			form.submit();
			jQuery.fancybox.close();
		}
	});
});