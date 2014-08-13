jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('form.userForm').validate();

	jQuery('a.store').live('click', function(event) {
		event.preventDefault();
		var form = jQuery('form#registrationRunnerChanger');
		if (form.valid()) {
			form.submit();
		}
	});
});
