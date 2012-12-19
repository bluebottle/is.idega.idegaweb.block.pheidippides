jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('form.adminForm').validate();
	
	jQuery('a.next').click(function(event) {
		event.preventDefault();
		
		var link = jQuery(this);
		jQuery('input[name="prm_action"]').val(link.attr('rel'));
		
		var form = jQuery('form.adminForm');
		if (form.valid()) {
			form.submit();
		}
	});
});
