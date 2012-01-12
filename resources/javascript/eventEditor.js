jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('a.addItem, a.editItem').fancybox();
	
	jQuery('a.close').click(function(event) {
		event.preventDefault();
		jQuery.fancybox.close();
	});
	
	jQuery('a.store').click(function(event)) {
		event.preventDefault();
		jQuery('form#eventEditorForm').submit();
		jQuery.fancybox.close();
	});
});