jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('a.addItem').fancybox({
		afterShow: function() {
			jQuery('#giftCardCreatorForm').validate();
		}
	});
	
	jQuery('a.close').live('click', function(event) {
		event.preventDefault();
		jQuery.fancybox.close();
	});
	
	jQuery('a.store').live('click', function(event) {
		event.preventDefault();
		var form = jQuery('form#giftCardCreatorForm');
		if (form.valid()) {
			form.submit();
			jQuery.fancybox.close();
		}
	});
});