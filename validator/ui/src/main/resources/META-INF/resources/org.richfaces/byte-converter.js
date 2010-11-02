RichFaces.csv.addConverter({"byte":
(function($, rf) {
	return function (value, params) {
		var result; value = $.trim(value);
		if (!rf.csv.RE_DIGITS.test(value) || (result=parseInt(value,10))<-128 || result>127) {
			throw rf.csv.getMessage(params.customMessage, 'BYTE_CONVERTER_BYTE', [value, 0, params.componentId]);
		}
		return result;
	}
})(jQuery, window.RichFaces || (window.RichFaces={}))
});