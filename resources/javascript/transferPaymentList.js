jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery("table.adminTable").tablesorter({ 
        headers: { 
            5: { 
                sorter: false 
            }, 
            6: { 
                sorter: false 
            }, 
            7: { 
                sorter: false 
            } 
        } 
	}); 
	
	jQuery('a.viewItem').fancybox();
	
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