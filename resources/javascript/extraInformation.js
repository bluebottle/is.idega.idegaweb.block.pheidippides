jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('a.store').live('click', function(event) {
		event.preventDefault();
		var form = jQuery('form#extraInformation');
		if (form.valid()) {
			form.submit();
		}
	});
});