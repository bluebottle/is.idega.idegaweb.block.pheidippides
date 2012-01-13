jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('a.addItem, a.editItem').fancybox();
	
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
		jQuery('form#eventEditorForm').submit();
		jQuery.fancybox.close();
	});
});