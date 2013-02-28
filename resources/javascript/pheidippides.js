jQuery.noConflict();

var sortBy;

jQuery(document).ready(function() {
	/* sortBy functions & helpers extracted and slightly modified from
    * Sugar Library v1.3.7
    * Freely distributable and licensed under the MIT-style license.
    * Copyright (c) 2012 Andrew Plummer
    * http://sugarjs.com/arrays#sorting
    */
    var array = {
        AlphanumericSortIgnoreCase  : true,
        AlphanumericSortEquivalents : {}
    },

    sortBy = function(a,b){
        return typeof a === "string" && typeof b === "string" ? collateStrings(a,b) : a < b ? -1 : a > b ? 1 : 0;
    },

    // Alphanumeric collation helpers
    collateStrings = function(a, b) {
        var aValue, bValue, aChar, bChar, aEquiv, bEquiv, index = 0, tiebreaker = 0;
        a = getCollationReadyString(a);
        b = getCollationReadyString(b);
        do {
            aChar  = getCollationCharacter(a, index);
            bChar  = getCollationCharacter(b, index);
            aValue = getCollationValue(aChar);
            bValue = getCollationValue(bChar);
            if (aValue === -1 || bValue === -1) {
                aValue = a.charCodeAt(index) || null;
                bValue = b.charCodeAt(index) || null;
            }
            aEquiv = aChar !== a.charAt(index);
            bEquiv = bChar !== b.charAt(index);
            if (aEquiv !== bEquiv && tiebreaker === 0) {
                tiebreaker = aEquiv - bEquiv;
            }
            index += 1;
        } while (aValue != null && bValue != null && aValue === bValue);
        if (aValue === bValue) return tiebreaker;
        return aValue < bValue ? -1 : 1;
    },

    getCollationReadyString = function(str) {
        if (array.AlphanumericSortIgnoreCase) {
            str = str.toLowerCase();
        }
        return str.replace(array.AlphanumericSortIgnore, '');
    },

    getCollationCharacter = function(str, index) {
        var chr = str.charAt(index), eq = array.AlphanumericSortEquivalents || {};
        return eq[chr] || chr;
    },

    getCollationValue = function(chr){
        var order = array.AlphanumericSortOrder;
        return chr ? order.indexOf(chr) : null;
    },

    // order = 'AÁÀÂÃĄBCĆČÇDĎÐEÉÈĚÊËĘFGĞHıIÍÌİÎÏJKLŁMNŃŇÑOÓÒÔPQRŘSŚŠŞTŤUÚÙŮÛÜVWXYÝZŹŻŽÞÆŒØÕÅÄÖ',
    // equiv = 'AÁÀÂÃ,CÇ,EÉÈÊË,IÍÌİÎÏ,OÓÒÔ,Sß,UÚÙÛÜ',
    // modified order to match Icelandic sorting - see https://github.com/Mottie/tablesorter/issues/212
    order = 'AÁBCDÐEÉĘFGHIÍJKLMNOÓPQRSTUÚVWXYÝZÞÆÖ',
    equiv = '',
    equivalents = {};

    array.AlphanumericSortOrder = jQuery.map(order.split(''), function(str) {
        return str + str.toLowerCase();
    }).join('');
    
    jQuery.tablesorter.defaults.textSorter = sortBy;

    jQuery.each(equiv.split(','), function(i,set) {
        var equivalent = set.charAt(0);
        jQuery.each(set.slice(1).split(''), function(i,chr) {
            equivalents[chr] = equivalent;
            equivalents[chr.toLowerCase()] = equivalent.toLowerCase();
        });
    });
    
	jQuery.tablesorter.addParser({
	    id: "datetime",
	    is: function(s) {
	        return false; 
	    },
	    format: function(s,table) {
	        s = s.replace(/\-/g,"/");
	        s = s.replace(/(\d{1,2})[\.\/\-](\d{1,2})[\.\/\-](\d{4})/, "$3/$2/$1");
	        return jQuery.tablesorter.formatFloat(new Date(s).getTime());
	    },
	    type: "numeric"
	});

	jQuery.tablesorter.addParser({ 
	    id: 'personalId', 
	    is: function(s) { 
	        return false; 
	    }, 
	    format: function(s) {
	        var date = s;
	        if (date.indexOf('.') != -1) {
	        	date = s.split('.');
		        return new Date(date[2], parseInt(date[1]) - 1, date[0]).getTime();
	        }
	        else {
	        	var day = parseInt(s.substring(0, 2));
	        	var month = parseInt(s.substring(2, 4));
	        	var year = parseInt(s.substring(4, 6));
	        	var decade = s.substring(s.length - 1);
	        	if (decade = 9) {
	        		year += 1900;
	        	}
	        	else {
	        		year += 2000;
	        	}
	        	return new Date(year, month, day).getTime();
	        }
	    },
	    type: 'numeric' 
	});
});