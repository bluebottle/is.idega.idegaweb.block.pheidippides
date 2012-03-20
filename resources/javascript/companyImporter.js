jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery('form.adminForm').validate();
	jQuery('[name="prm_race_pk"]').each(function(){
		jQuery(this).rules("add", {
			required: true
		});   
	});
	jQuery('[name="prm_shirt_size"]').each(function(){
		jQuery(this).rules("add", {
			required: true
		});   
	});
	
	jQuery('a.next').click(function(event) {
		event.preventDefault();
		
		var link = jQuery(this);
		jQuery('input[name="prm_action"]').val(link.attr('rel'));
		
		var form = jQuery('form.adminForm');
		if (form.valid()) {
			form.submit();
		}
	});
	
	jQuery('select[name="prm_race_pk"]').change(function() {
		var select = jQuery(this);
		var tr = select.parent().parent();
		var racePK = select.val();
		var language = jQuery('input[name="prm_language"]').val();
		var shirt = jQuery('select[name="prm_shirt_size"]', tr);
		var value = shirt.val();
		
		PheidippidesService.getLocalizedShirts(racePK, language, {
			callback: function(shirts) {
				shirt.empty();
				
				for (var i = 0; i < shirts.length; i++) {
					if (value != null && value == shirts[i].value) {
						shirt.append('<option value="' + shirts[i].id + '" selected="selected">' + shirts[i].value + '</option>')
					}
					else {
						shirt.append('<option value="' + shirts[i].id + '">' + shirts[i].value + '</option>')
					}
				}
			}
		});
	});
});