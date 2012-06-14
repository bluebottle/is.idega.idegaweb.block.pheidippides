jQuery.noConflict();

jQuery(document).ready(function() {
	var userForm = jQuery('form.user').validate();
	
	jQuery('input[name="prm_estimated_time"]').mask("99:99");

	jQuery('.radioButton input:radio').click(function() {
		var input = jQuery(this);
		var parent = input.parents('fieldset.formSection');
		
		if (input.hasClass('multiple')) {
			jQuery('select', parent).removeAttr('disabled');
		}
		else {
			jQuery('select', parent).attr('disabled', 'disabled');
		}
		
		jQuery('label.error', parent).remove();
		jQuery(".error", parent).removeClass("error");
	})
	
	jQuery('a.store').live('click', function(event) {
		event.preventDefault();
		var form = jQuery('form#extraInformation');
		if (form.valid()) {
			form.submit();
		}
	});
});