jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('a.addItem, a.editItem').fancybox();
	
	jQuery('a.close').live('click', function(event) {
		event.preventDefault();
		jQuery.fancybox.close();
	});
	
	jQuery('a.store').live('click', function(event) {
		event.preventDefault();
		jQuery('form#raceEditorForm').submit();
		jQuery.fancybox.close();
	});
});