jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery("table.adminTable").tablesorter({ 
        headers: { 
            7: { 
                sorter: false 
            } 
        } 
	}); 
	
	jQuery('a.addItem, a.editItem').fancybox({
		afterShow: function() {
			jQuery('#companyEditorForm').validate();
		}
	});
		
	jQuery('a.close').live('click', function(event) {
		event.preventDefault();
		jQuery.fancybox.close();
	});
	
	jQuery('a.store').live('click', function(event) {
		event.preventDefault();
		var form = jQuery('form#companyEditorForm');
		if (form.valid()) {
			form.submit();
			jQuery.fancybox.close();
		}
	});
});