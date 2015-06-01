jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery("table.adminTable").tablesorter({ 
		cssChildRow: 'childRow',
		textSorter: sortBy
	});
});
