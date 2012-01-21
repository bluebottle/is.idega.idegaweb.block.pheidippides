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
	
	jQuery('a.editItem').fancybox({
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
	
	jQuery('a.store').click(function(event) {
		event.preventDefault();
		jQuery('form#transferPaymentForm').submit();
	});
});