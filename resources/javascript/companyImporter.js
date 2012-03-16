jQuery.noConflict();
var availableRaces = false;

jQuery(document).ready(function() {
	jQuery('a.next').click(function(event) {
		event.preventDefault();
		
		var link = jQuery(this);
		jQuery('input[name="prm_action"]').val(link.attr('rel'));
		
		var form = jQuery('form.companyImportForm');
		if (form.valid()) {
			form.submit();
		}
	});
});