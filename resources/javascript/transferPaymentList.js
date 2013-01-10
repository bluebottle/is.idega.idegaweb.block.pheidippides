jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery("table.adminTable").tablesorter({
		dateFormat: 'dd.mm.yyyy',
		textSorter: sortBy,
        headers: {
        	4: {
        		sorter: 'datetime'
        	},
            5: { 
                sorter: false 
            }, 
            6: { 
                sorter: false 
            } 
        } 
	});
	
	jQuery('span.action').click(function() {
		var span = jQuery(this);
		var action = span.attr('rel');
		var input = span.siblings('input[name="prm_registration_header_action"]');
		
		if (action == 'none') {
			span.attr('rel', 'paid');
			input.val('2');
		}
		else if (action == 'paid') {
			span.attr('rel', 'free');
			input.val('3');
		}
		else if (action == 'free') {
			span.attr('rel', 'delete');
			input.val('4');
		}
		else if (action == 'delete') {
			span.attr('rel', 'none');
			input.val('1');
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