jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('form.user').validate();
	
	jQuery('input[name="prm_estimated_time"]').mask("99:99");

	jQuery('input.multiple').click(function() {
		var input = jQuery(this);
		var parent = input.parents('fieldset.formSection');
		
		if (input.is(':checked')) {
			jQuery('input:text', parent).removeAttr('disabled');
		}
		else {
			jQuery('input:text', parent).attr('disabled', 'disabled');
		}
	})
	
	jQuery('a.store').live('click', function(event) {
		event.preventDefault();
		var form = jQuery('form#extraInformation');
		if (form.valid()) {
			form.submit();
		}
	});
});