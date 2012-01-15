jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery("table.adminTable").tablesorter({ 
        headers: { 
            12: { 
                sorter: false 
            },
            13: { 
                sorter: false 
            } 
        } 
	});
	
	jQuery('.dwrEnabled').change(function() {
		var eventPK = jQuery('select[name="prm_event_pk"]').val();
		var year = jQuery('select[name="prm_year"]').val();
		var race = jQuery('select[name="prm_race_pk"]');
		
		PheidippidesService.getRaces(eventPK, year, {
			callback: function(races) {
				dwr.util.removeAllOptions(race.attr('id'));
				dwr.util.addOptions(
					race.attr('id'),
					races,
					function(race) {
						return race.id;
					},
					function(race) {
						return race.distance.name;
					}
				);
			}
		});
	}).trigger('change');
	
	jQuery('a.addItem, a.editItem').fancybox({
		'onComplete': function() {
			jQuery('#racePriceEditorForm').validate();
			jQuery('input.datePicker').datepicker({
				regional: ['is']
			});
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
		var form = jQuery('form#racePriceEditorForm');
		if (form.valid()) {
			form.submit();
			jQuery.fancybox.close();
		}
	});
});