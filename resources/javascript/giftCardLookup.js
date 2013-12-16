jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('a.lookup').click(function(event) {
		event.preventDefault();
		jQuery('form#giftCardLookup').submit();
	});
});