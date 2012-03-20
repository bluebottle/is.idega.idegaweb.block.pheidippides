jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery("table.adminTable").tablesorter({ 
		cssChildRow: 'childRow',
        headers: { 
            4: { 
                sorter: false 
            }, 
            5: { 
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
	
	jQuery('input.datePicker').live('click', function () {
        jQuery(this).datepicker('destroy').datepicker({regional: ['is'], dateFormat: 'd.m.yy', showTime: true}).focus();
	});
	
	jQuery('a.addItem, a.editItem').fancybox({
		afterShow: function() {
			jQuery('#participantEditorForm').validate();
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
		var form = jQuery('form#participantEditorForm');
		if (form.valid()) {
			form.submit();
			jQuery.fancybox.close();
		}
	});
});