RichFaces.csv.addConverter({"number":
(function($, rf) {
	return function (value, params) {
		var result; value=$.trim(value);
		if (isNaN(value)) {
			throw rf.csv.getMessage(params.customMessage, 'NUMBER_CONVERTER_NUMBER', [value, 0, params.componentId]);
		} else {
			result = parseInt(value, 10);
		}
		return result;
	}
})(jQuery, window.RichFaces || (window.RichFaces={}))
});