jQuery.noConflict();

jQuery(document).ready(function() {
	LightTableFilter.init();
	
	jQuery('a.addItem').fancybox({
		afterShow: function() {
			jQuery('#discountCodeCreatorForm').validate();
		}
	});
	
	jQuery('a.close').live('click', function(event) {
		event.preventDefault();
		jQuery.fancybox.close();
	});
	
	jQuery('a.store').live('click', function(event) {
		event.preventDefault();
		var form = jQuery('form#discountCodeCreatorForm');
		if (form.valid()) {
			form.submit();
			jQuery.fancybox.close();
		}
	});
	
	jQuery('a.deleteItem').click(function(event) {
		var item = jQuery(this);
		var message = item.attr('rel');
		
		return confirm(message);
	});
});

var LightTableFilter = (function(Arr) {
	var _input;

	function _onInputEvent(e) {
		_input = e.target;
		var tables = document.getElementsByClassName(_input.getAttribute('data-table'));
		Arr.forEach.call(tables, function(table) {
			Arr.forEach.call(table.tBodies, function(tbody) {
				Arr.forEach.call(tbody.rows, _filter);
			});
		});
	}

	function _filter(row) {
		var text = row.textContent.toLowerCase(), val = _input.value.toLowerCase();
		row.style.display = text.indexOf(val) === -1 ? 'none' : 'table-row';
	}

	return {
		init: function() {
			var inputs = document.getElementsByClassName('light-table-filter');
			Arr.forEach.call(inputs, function(input) {
				input.oninput = _onInputEvent;
			});
		}
	};
})(Array.prototype);
