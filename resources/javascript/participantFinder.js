jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('a.viewItem').fancybox();
	
	jQuery('a.close').live('click', function(event) {
		event.preventDefault();
		jQuery.fancybox.close();
	});
});