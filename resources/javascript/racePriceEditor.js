jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery("table.adminTable").tablesorter({ 
        headers: { 
        	2: {
        		sorter: 'datetime'
        	},
        	3: {
        		sorter: 'datetime'
        	},
        	10: {
        		sorter: 'datetime'
        	},
            11: { 
                sorter: false 
            },
            12: { 
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
		
		PheidippidesService.getLocalizedRaces(eventPK, year, language, false, {
			callback: function(races) {
				dwr.util.removeAllOptions(race.attr('id'));
				
				for (var i = 0; i < races.length; i++) {
					race.append('<option value="' + races[i].id + '">' + races[i].value + '</option>')
				}

				dwr.util.setValue(race.attr('id'), value);
			}
		});
	}).trigger('change');
	
	jQuery('input.datePicker').live('click', function () {
        jQuery(this).datepicker('destroy').datepicker({regional: ['is'], dateFormat: 'd.m.yy'}).focus();
	});
	
	jQuery('a.addItem, a.editItem').fancybox({
		afterShow: function() {
			jQuery('#racePriceEditorForm').validate();
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