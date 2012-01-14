jQuery.noConflict();

jQuery.validator.setDefaults({
	debug: true,
	success: "valid"
});

jQuery(document).ready(function() {
	jQuery("table.adminTable").tablesorter({ 
        headers: { 
            8: { 
                sorter: false 
            }, 
            11: { 
                sorter: false 
            },
            12: { 
                sorter: false 
            } 
        } 
	});
	
	/*jQuery('#raceEditorForm').validate({
		rules: {
			prm_minimum_age: {
				required: true,
				digits: true
			}
		}
	});*/
	
	jQuery('a.addItem, a.editItem').fancybox();
	
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
		jQuery('form#raceEditorForm').submit();
		jQuery.fancybox.close();
	});
});