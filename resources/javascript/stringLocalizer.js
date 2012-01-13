jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('a.editItem').fancybox();
	
	jQuery('a.close').live('click', function(event) {
		event.preventDefault();
		jQuery.fancybox.close();
	});
	
	jQuery('a.store').live('click', function(event) {
		event.preventDefault();
		jQuery('form#stringLocalizerForm').submit();
		jQuery.fancybox.close();
	});
});