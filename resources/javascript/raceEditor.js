jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery("table.adminTable").tablesorter({ 
        headers: { 
        	5: {
        		sorter: 'datetime'
        	},
        	6: {
        		sorter: 'datetime'
        	},
            8: { 
                sorter: false 
            }, 
        	9: {
        		sorter: 'datetime'
        	},
            10: { 
                sorter: false 
            },
            11: { 
                sorter: false 
            } 
        } 
	});
	
	jQuery('input.datePicker').live('click', function () {
        jQuery(this).datepicker('destroy').datepicker({regional: ['is'], dateFormat: 'd.m.yy', showTime: true}).focus();
	});
	
	jQuery('a.addItem, a.editItem').fancybox({
		afterShow: function() {
			jQuery('#raceEditorForm').validate();
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
		var form = jQuery('form#raceEditorForm');
		if (form.valid()) {
			form.submit();
			jQuery.fancybox.close();
		}
	});
});