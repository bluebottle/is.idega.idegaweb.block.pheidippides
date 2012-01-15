jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery("table.adminTable").tablesorter({ 
        headers: { 
            8: { 
                sorter: false 
            }, 
            10: { 
                sorter: false 
            },
            11: { 
                sorter: false 
            } 
        } 
	});
	
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