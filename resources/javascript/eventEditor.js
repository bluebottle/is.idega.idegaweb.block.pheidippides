jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('a.addItem, a.editItem').fancybox();
	
	jQuery('a.close').click(function(event) {
		event.preventDefault();
		$.fancybox().close();
	});
	
	jQuery('a.store').click(function(event)) {
		event.preventDefault();
		$('form#eventEditorForm').submit();
		$.fancybox().close();
	});
});